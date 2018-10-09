package com.di.distributedlock;

import com.di.constant.Constant;
import com.di.enums.LockStatus;
import com.di.utils.MyComParator;
import com.di.utils.ZkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

/**
 * 写锁
 */
public class WriteLock implements DistributedLock {

    private LockStatus lockStatus = LockStatus.UNLOCK;

    private CyclicBarrier lockBarrier = new CyclicBarrier(2);

    private static final long spinForTimeoutThreshold = 1000L;

    private static final long SLEEP_TIME = 100l;

    private String prefix = "distributedlock-write-";

    private MyComParator myComParator;

    private String path;


    @Override
    public void lock() {
        //确保根节点存在
        ensureRootPath(Constant.LOCK_NODE_PARENT_PATH);

        if (lockStatus == LockStatus.LOCK) {
            return;
        }

        //创建锁节点
        if (Objects.isNull(path)) {
            path = ZkUtils.createNode(Constant.LOCK_NODE_PARENT_PATH + "/" + prefix, StringUtils.EMPTY,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
        }
        path = path.substring(path.lastIndexOf("/") + 1);
        System.out.println("创建节点:" + path);

        //获取所节点列表并排序
        List<String> nodeList = ZkUtils.getNodeChildren(Constant.LOCK_NODE_PARENT_PATH);
        nodeList.sort(myComParator);
        //判断当前节点是不是第一个节点
        if (isFirstNode(path, nodeList)) {
            System.out.println(path + "成功获取锁");
            lockStatus = LockStatus.LOCK;
            return;
        }
        //监控上一个锁节点
        int index = Collections.binarySearch(nodeList, path);
        ZkUtils.subscribeChildChanges(Constant.LOCK_NODE_PARENT_PATH + "/" + nodeList.get(index - 1));
        lockStatus = LockStatus.TRYLOCK;
        try {
            lockBarrier.await();
        } catch (Exception e) {

        }
    }

    public boolean canAcquireLock(String path, List<String> nodeList) {
        if (isFirstNode(path, nodeList)) {
            return true;
        }
        Map<String, Boolean> map = new HashMap<>();
        boolean hasWriteOperation = false;
        for (String node : nodeList) {
            if (node.contains("read") && !hasWriteOperation) {
                map.put(node, true);
            } else {
                hasWriteOperation = true;
                map.put(node, false);
            }
        }
        return map.get(path);

    }

    /**
     * 判断当前节点是不是第一个节点
     *
     * @param path
     * @param nodeList
     * @return
     */
    private boolean isFirstNode(String path, List<String> nodeList) {
        return nodeList.get(0).equals(path);
    }

    @Override
    public void unLock() {
        if (lockStatus == LockStatus.UNLOCK) {
            return;
        }
        System.out.println(path + " 释放锁");
        ZkUtils.deleteSingleNode(path);
        lockStatus = LockStatus.UNLOCK;
        lockBarrier.reset();
        path = null;
    }

    @Override
    public boolean tryLock() {
        if (lockStatus == LockStatus.LOCK) {
            return true;
        }
        //创建锁节点
        if (Objects.isNull(path)) {
            path = ZkUtils.createNode(Constant.LOCK_NODE_PARENT_PATH + "/" + path, StringUtils.EMPTY,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            path = path.substring(path.lastIndexOf("-") + 1);
            System.out.println("创建节点:" + path);
        }
        //获取锁节点列表
        List<String> nodeList = ZkUtils.getNodeChildren(Constant.LOCK_NODE_PARENT_PATH);
        nodeList.sort(myComParator);
        //判断自己是否是第一个节点,若是,直接加锁
        if (isFirstNode(path, nodeList)) {
            System.out.println(path + "成功获取锁");
            lockStatus = LockStatus.LOCK;
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(Long millisecond) {
        long millisTimeout = millisecond;
        if (millisTimeout <= 0L) {
            return false;
        }

        final long deadline = System.currentTimeMillis() + millisTimeout;
        for (; ; ) {
            if (tryLock()) {
                return true;
            }

            if (millisTimeout > spinForTimeoutThreshold) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            millisTimeout = deadline - System.currentTimeMillis();
            if (millisTimeout <= 0L) {
                return false;
            }
        }
    }

    /**
     * 确保锁的根路径存在
     *
     * @param rootPath
     */
    private void ensureRootPath(String rootPath) {
        if (!ZkUtils.nodeExits(rootPath)) {
            ZkUtils.createNode(rootPath, StringUtils.EMPTY, CreateMode.PERSISTENT);
        }
    }
}
