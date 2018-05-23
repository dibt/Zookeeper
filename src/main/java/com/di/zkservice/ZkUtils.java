package com.di.zkservice;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by bentengdi on 2018/5/16.
 */
public class ZkUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkUtils.class);
    private static ZkClient zkClient = null;
    static {
        getZkClient();
    }

    public static String getZkClient(){
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dev/zkservices.properties");
        try{
            properties.load(inputStream);
        }catch (Exception e){
            LOGGER.error("load zkservices.properties error",e);
        }
        String zkServices = properties.getProperty("zk.services");
        zkClient=new ZkClient(zkServices,10000,10000,new SerializableSerializer());
        //zkClient.setZkSerializer(new MyZkSerializer());
        LOGGER.info("load zksernices.properties success");
        return  zkServices;
    }

    //创建节点
    public static  String createNode(String path,Object data,CreateMode nodetype){
        String nodepath=zkClient.create(path,data,nodetype);
        return nodepath;
    }
    //节点是否存在
    public static boolean exits(String path){
        return zkClient.exists(path);
    }
    //获取节点中的数据
    public static Object getData(String path){
        Stat stat =new Stat();
        return zkClient.readData(path,stat);
    }
    //删除单个节点或递归删除
    public static boolean deleteNode(String path){
        //删除单独一个节点，返回true表示成功
        //boolean e1 = zkClient.delete(path);
        //删除含有子节点的节点
        boolean e2 = zkClient.deleteRecursive(path);
        return e2;
    }
    //更新节点数据
    public static void writeData(String path,Object data){
        zkClient.writeData(path,data);
    }
    //订阅节点的信息改变（创建节点，删除节点，添加子节点）
    public static void subscribeChildChanges(String path){
        /**
         * path监听的节点，可以是现在存在的也可以是不存在的
         */
        zkClient.subscribeChildChanges(path,new ZkChildListener());
    }
    //订阅节点的数据内容的变化
    public static void subscribeDataChanges(String path){
        zkClient.subscribeDataChanges(path, new ZKDataListener());
    }


}
