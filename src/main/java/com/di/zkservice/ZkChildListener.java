package com.di.zkservice;

import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

/**
 * Created by bentengdi on 2018/5/16.
 */
public class ZkChildListener implements IZkChildListener {

    /**
     * handleChildChange： 用来处理服务器端发送过来的通知
     * parentPath：对应的父节点的路径
     * currentChilds：子节点的相对路径
     */
    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        System.out.println(parentPath);
        System.out.println(currentChilds.toString());
    }
}
