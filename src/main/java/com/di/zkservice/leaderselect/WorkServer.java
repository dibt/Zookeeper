package com.di.zkservice.leaderselect;

/**
 * Created by bentengdi on 2018/5/16.
 */

import com.di.pojo.RunningData;
import com.di.zkservice.ZKDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 工作服务器
 */
public class WorkServer {
    private static final Logger LOGGER = Logger.getLogger(WorkServer.class);

    // 记录服务器状态
    private volatile boolean running = false;

    private ZkClient zkClient;
    // Master节点对应zookeeper中的节点路径
    private static final String MASTER_PATH = "/bentengdi";
    // 监听Master节点删除事件
    private ZKDataListener dataListener;
    // 记录当前节点的基本信息
    private RunningData serverData;
    // 记录集群中Master节点的基本信息
    private RunningData masterData;

    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    private int delayTime = 10;

    public WorkServer(RunningData rd) {
        this.serverData = rd; // 记录服务器基本信息
        this.dataListener = new ZKDataListener() {
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                if (masterData != null && masterData.getName().equals(serverData.getName())){
                    // 上一轮的Master服务器是自己，则直接抢
                    //takeMaster();
                    System.out.println("打印master信息："+masterData.toString());
                } else {
                    // 否则，延迟10秒后再抢。主要是应对网络抖动，给上一轮的Master服务器优先抢占master的权利，避免不必要的数据迁移开销
                    delayExector.schedule(new Runnable(){
                        public void run(){
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    // 启动服务器
    public void start() throws Exception {
        if (running) {
            throw new Exception("server has startup...");
        }
        running = true;
        // 订阅Master节点删除事件
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        // 争抢Master
        takeMaster();

    }

    // 停止服务器
    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stoped");
        }
        running = false;

        delayExector.shutdown();
        // 取消Master节点事件订阅
        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
        // 释放Master权利
        releaseMaster();
    }

    // 争抢Master
    private void takeMaster() {
        if (!running)
            return;
        try {
            // 尝试创建Master临时节点
            zkClient.create(MASTER_PATH,serverData,CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println("争抢Master成功"+serverData.getName()+" is master");
            // 服务器每隔10秒释放一次Master
            delayExector.schedule(new Runnable() {//执行一次
                public void run() {
                    // TODO Auto-generated method stub
                    if (checkMaster()){
                        releaseMaster();
                    }
                }
            }, 10, TimeUnit.SECONDS);

        } catch (ZkNodeExistsException e) { // 已被其他服务器创建了
            // 读取Master节点信息
            RunningData runningData = zkClient.readData(MASTER_PATH);
            if (runningData == null) {
                takeMaster(); // 没读到，读取瞬间Master节点宕机了，有机会再次争抢
            } else {
                masterData = runningData;
            }
        } catch (Exception e) {
            LOGGER.error("create master node error:",e);
        }

    }

    // 释放Master
    private void releaseMaster() {
        if (checkMaster()) {
            zkClient.delete(MASTER_PATH);
        }
    }

    // 检测自己是否为Master
    private boolean checkMaster() {
        try {
            RunningData eventData=zkClient.readData(MASTER_PATH);
            masterData = eventData;
            if (masterData.getName().equals(serverData.getName())) {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e) {
            return false; // 节点不存在，自己肯定不是Master
        } catch (ZkInterruptedException e) {
            return checkMaster();//重新检测
        } catch (ZkException e) {
            return false;
        }
    }

}