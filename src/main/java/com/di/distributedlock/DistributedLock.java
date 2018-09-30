package com.di.distributedlock;

/**
 * 锁接口
 */
public interface DistributedLock {
    void lock();
    void unLock();
    boolean tryLock();
    boolean tryLock(Long millisecond);
}
