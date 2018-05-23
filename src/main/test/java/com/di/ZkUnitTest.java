package com.di;

import com.di.pojo.User;
import com.di.zkservice.ZkUtils;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by bentengdi on 2018/5/17.
 */
public class ZkUnitTest {
    @Test
    public void cerateNodeTest() {
//        User user = new User();
//        user.setId(1);
//        user.setName("bentengdi");
        String path="/bentengdi";
       //System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));
        //System.out.println(ZkUtils.deleteNode(path));

        //System.out.println(ZkUtils.createNode(path,user, CreateMode.PERSISTENT));
       //System.out.println(ZkUtils.exits(path));
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
//        Object o = ZkUtils.getData(path);
//        Object s = ZkUtils.getData(path);
//        if(null != s ){
//            System.out.println(s.toString());
//        }else{
//            System.out.println("节点空数据");
//        }
        System.out.println(ZkUtils.getData(path).toString());
    }
}
