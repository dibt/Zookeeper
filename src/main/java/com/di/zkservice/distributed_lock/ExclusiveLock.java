package com.di.zkservice.distributed_lock;

import org.apache.log4j.Logger;

/**
 * Created by bentengdi on 2018/5/17.
 */
public class ExclusiveLock  implements SharedLock{
    private static final Logger LOGGER = Logger.getLogger(ExclusiveLock.class);
    private static final String LOCK_PATH = "/bentengdi.lock";

    @Override
    public void lock() throws Exception {

    }

    @Override
    public Boolean tryLock() throws Exception {
        return null;
    }

    @Override
    public Boolean tryLock(long millisencond) throws Exception {
        return null;
    }

    @Override
    public void unLock() throws Exception {

    }
}
