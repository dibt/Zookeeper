package com.di;

import com.di.pojo.RunningData;
import com.di.zkservice.leaderselect.WorkServer;
import com.di.zkservice.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by bentengdi on 2018/5/17.
 */
public class LeaderSelectorZkClientTest {
    private static final int CLIENT_NUM=10;
    private static final String zkservice= ZkUtils.getZkClient();
    @Test
    public void leaderSelectTest(){
        //保存所有zkClient的列表
        List<ZkClient> zkClientList = new ArrayList<>();
        //保存左右的服务列表
        List<WorkServer> workServerList = new ArrayList<>();

        try{
            for(int i=0;i<CLIENT_NUM;i++) {
                //创建zkClient
                ZkClient zkClient = new ZkClient(zkservice, 10000, 10000, new SerializableSerializer());
                zkClientList.add(zkClient);
                //创建serverData
                RunningData runningData = new RunningData();
                runningData.setCid(Long.valueOf(i));
                runningData.setName("Client #"+i);
                //创建服务
                WorkServer workServer =new WorkServer(runningData);
                workServer.setZkClient(zkClient);
                workServerList.add(workServer);
                workServer.start();
            }
            Thread.sleep(30000);
        }catch (Exception e){}
        finally {
            System.out.println("Shutting down...");

            for ( WorkServer workServer : workServerList ) {
                try {
                    workServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for ( ZkClient client : zkClientList) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
