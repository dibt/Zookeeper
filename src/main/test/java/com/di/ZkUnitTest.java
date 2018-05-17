package com.di;

import com.di.pojo.User;
import com.di.zkservice.ZkUtils;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * Created by bentengdi on 2018/5/17.
 */
public class ZkUnitTest {
    @Test
    public void cerateNodeTest() throws InterruptedException {
        User user = new User();
        user.setId(1);
        user.setName("bentengdi");
        String path="/bentengdi";
       System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));
        //System.out.println(ZkUtils.deleteNode(path));

        //System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));
//        System.out.println(ZkUtils.exits(path));
//        User user1 =(User) ZkUtils.getData(path);
//        System.out.println(user1.toString());

//        user1 =(User) ZkUtils.getData(path);
//        System.out.println(user1.toString());

        //ZkUtils.subscribeChildChanges(path);
        //System.out.println(ZkUtils.deleteNode(path));
        //System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));
        //System.out.println(ZkUtils.createNode("/bentengdi/test",user, CreateMode.PERSISTENT));
//        ZkUtils.subscribeDataChanges(path);
//        System.out.println(ZkUtils.createNode("/bentengdi/test/test",user, CreateMode.PERSISTENT));
//        user.setId(23);
//        ZkUtils.writeData(path,user);
//        Thread.sleep(10000);
    }
}
