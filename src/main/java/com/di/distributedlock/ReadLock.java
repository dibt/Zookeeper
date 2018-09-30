package com.di.distributedlock;

import com.di.enums.LockStatus;

import java.util.concurrent.CyclicBarrier;

public class ReadLock implements DistributedLock {

    private LockStatus lockStatus = LockStatus.UNLOCK;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Override
    public void lock() {

    }

    @Override
    public void unLock() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(Long millisecond) {
        return false;
    }
}
