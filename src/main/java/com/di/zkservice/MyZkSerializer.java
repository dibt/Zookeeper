package com.di.zkservice;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.TcclAwareObjectIputStream;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.io.Charsets;

import java.io.*;

/**
 * 使用ZkClient获取节点数据的时候如果出现序列化的错误，需要自己重新实现ZkSerializer类，然后调用ZkClient的setZkSerializer方法
 * 默认实现是SerializableSerializer类
 */
public class MyZkSerializer implements ZkSerializer {
    private Object  object;
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            ObjectInputStream inputStream = new TcclAwareObjectIputStream(new ByteArrayInputStream(bytes));
            object = inputStream.readObject(); 
            return object;
        } catch (ClassNotFoundException e) {
            throw new ZkMarshallingError("Unable to find object class.", e);
        } catch (IOException e) {
            System.out.println("EOFException,结束");
        }finally {
            return object;
        }
    }

    @Override
    public byte[] serialize(Object serializable) throws ZkMarshallingError {
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(byteArrayOS);
            stream.writeObject(serializable);
            stream.close();
            return byteArrayOS.toByteArray();
        } catch (IOException e) {
            throw new ZkMarshallingError(e);
        }
    }
}