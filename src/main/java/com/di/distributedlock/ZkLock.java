package com.di.distributedlock;

public class ZkLock {

    private static final String LOCK_NODE_PARENT_PATH = "/share_lock";
    /** 自旋测试超时阈值，考虑到网络的延时性，这里设为1000毫秒 */
    private static final long spinForTimeoutThreshold = 1000L;


}
