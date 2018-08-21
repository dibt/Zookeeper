package com.di.zkservice;

import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

/**
 * IZKChildListener事件说明针对于下面三个事件触发：
 * - 新增子节点
 * - 减少子节点
 * - 删除节点
 * 注意： 不监听节点内容的变化
 */
public class ZkChildListener implements IZkChildListener {

    /**
     * handleChildChange： 用来处理服务器端发送过来的通知
     * parentPath：对应的父节点的路径
     * currentChilds：子节点的相对路径
     */
    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        System.out.println("parentPath: " +parentPath);
        System.out.println("currentChilds: " +currentChilds);
    }
}
