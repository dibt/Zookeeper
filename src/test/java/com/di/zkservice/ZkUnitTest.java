package com.di.zkservice;

import com.di.utils.ZkUtils;
import com.di.pojo.User;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * Created by bentengdi on 2018/5/17.
 */
public class ZkUnitTest {
    @Test
    public void cerateNodeTest() {
        User user = new User();
        user.setId(1);
        user.setName("bentengdi");
        String path="/povosdi";
       System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));

    }
    @Test
    public void nodeExists(){
        String path="/povosdi";
        System.out.println(ZkUtils.nodeExits(path));
    }
    @Test
    public void getNodeDate(){
        String path="/povosdi";
        User user = (User)ZkUtils.getNodeData(path);
        System.out.println(user);
    }
    @Test
    public void deleteSingleNode(){
        String path="/povosdi";
        System.out.println(ZkUtils.deleteSingleNode(path));
    }
    @Test
    public void deleteNode(){
        String path="/povosdi";
        System.out.println(ZkUtils.deleteNode(path));
    }
    @Test
    public void writeData(){
        String path = "/povosdi";
        User user = new User();
        user.setId(1);
        user.setName("di");
        ZkUtils.writeData(path,user);
        System.out.println(ZkUtils.getNodeData(path));
        user.setName("povosdi");
        ZkUtils.writeData(path,user);
        System.out.println(ZkUtils.getNodeData(path));
    }
    @Test
    public void subscribeChildrenChanges() throws InterruptedException {
        String path = "/povosdi";
        ZkUtils.subscribeChildChanges(path);
        System.out.println("-----------");
        ZkUtils.createNode(path,"",CreateMode.PERSISTENT);
        Thread.sleep(1000);
        ZkUtils.createNode(path+ "/test", "test",CreateMode.PERSISTENT);
        Thread.sleep(1000);
        ZkUtils.createNode(path+"/test1","test1",CreateMode.PERSISTENT);
        Thread.sleep(1000);
        System.out.println("-----------");
        /**
         * -----------
         * parentPath: /povosdi
         * currentChilds: []
         * parentPath: /povosdi
         * currentChilds: [test]
         * parentPath: /povosdi
         * currentChilds: [test, test1]
         * -----------
         */
    }
    @Test
    public void subscribeDataChanges() throws InterruptedException {
        String path = "/povosdi";
        ZkUtils.createNode(path,"povosdi",CreateMode.PERSISTENT);
        ZkUtils.subscribeDataChanges(path);
        System.out.println("-----------");
        Thread.sleep(1000);
        ZkUtils.writeData(path,"变更");
        ZkUtils.createNode(path+ "/test", "test",CreateMode.PERSISTENT);
        Thread.sleep(1000);
        ZkUtils.createNode(path+"/test1","test1",CreateMode.PERSISTENT);
        Thread.sleep(1000);
        System.out.println("-----------");
        /**
         * -----------
         * 变更的节点为:/povosdi
         * 变更的内容为:变更
         * -----------
         */

    }

}
