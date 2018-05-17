package com.di.zkservice.distributed_lock;

/**
 * Created by bentengdi on 2018/5/17.
 */
public interface SharedLock {
    void lock() throws Exception;
    Boolean tryLock()throws Exception;
    Boolean tryLock(long millisencond) throws Exception;
    void unLock() throws Exception;
}
