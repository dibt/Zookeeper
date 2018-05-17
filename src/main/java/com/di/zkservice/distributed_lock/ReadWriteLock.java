package com.di.zkservice.distributed_lock;

/**
 * Created by bentengdi on 2018/5/17.
 */
public interface ReadWriteLock {
    SharedLock readLock();
    SharedLock writeLock();
}
