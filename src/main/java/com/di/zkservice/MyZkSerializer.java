package com.di.zkservice;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.TcclAwareObjectIputStream;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.io.Charsets;

import java.io.*;

/**
 * Created by bentengdi on 2018/5/23.
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