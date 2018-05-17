package com.di.zkservice;

import org.I0Itec.zkclient.IZkDataListener;

/**
 * Created by bentengdi on 2018/5/16.
 */
public class ZKDataListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        //System.out.println("handleDataChange:"+dataPath+":"+data.toString());
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        System.out.println(dataPath);
    }
}
