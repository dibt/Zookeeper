package com.di.utils;

import com.di.zkservice.ZKDataListener;
import com.di.zkservice.ZkChildListener;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * zk操作工具实现类
 */
@Slf4j
public class ZkUtils {
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
            log.error("load zkservices.properties error{}",e);
        }
        String zkServices = properties.getProperty("zk.services");
        zkClient=new ZkClient(zkServices,10000,10000, new SerializableSerializer());
        log.info("load zksernices.properties success");
        return zkServices;
    }

    /**
     * 创建节点
     * @param path 节点路径
     * @param data 节点中存储的data
     * @param nodetype 节点类型
     * @return
     */
    public static String createNode(String path,Object data,CreateMode nodetype){
        String nodepath=zkClient.create(path,data,nodetype);
        return nodepath;
    }

    /**
     * 节点是否存在
     * @param path
     * @return 节点存在返回true
     */
    public static boolean nodeExits(String path){
        return zkClient.exists(path);
    }

    /**
     * 获取节点中的数据
     * @param path
     * @return
     */
    public static Object getNodeData(String path){
        return zkClient.readData(path,new Stat());
    }


    /**
     * 删除单个节点或递归删除
     * @param path
     * @return 返回true表示删除成功
     */
    public static boolean deleteSingleNode(String path){
        return zkClient.delete(path);
    }

    /**
     * 删除含有子节点的节点
     * @param path
     * @return 返回true表示删除成功
     */
    public static boolean deleteNode(String path){
        return zkClient.deleteRecursive(path);
    }

    /**
     * 更新节点数据
     * @param path
     * @param data
     */
    public static void writeData(String path,Object data){
        zkClient.writeData(path,data);
    }

    /**
     * 订阅节点的信息改变（创建节点，删除节点，添加子节点）
     * @param path
     */
    public static List<String> subscribeChildChanges(String path){
        /**
         * path监听的节点，可以是现在存在的也可以是不存在的
         */
        return zkClient.subscribeChildChanges(path,new ZkChildListener());
    }

    /**
     * 订阅节点的数据内容的变化
     * @param path
     */
    public static void subscribeDataChanges(String path){
        /**
         * path监听的节点，可以是现在存在的也可以是不存在的
         */
        zkClient.subscribeDataChanges(path, new ZKDataListener());
    }
}
