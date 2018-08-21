package com.di.zkservice;

import org.I0Itec.zkclient.IZkDataListener;

/**
 *监听dataPath的创建、删除以及内容的改变
 * 无关子节点的任何行为
 */
public class ZKDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {

        System.out.println("变更的节点为:"+dataPath);
        System.out.println("变更的内容为:"+data);

    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        System.out.println("删除的节点为:"+dataPath);
    }
}
