package com.di.cyclicbarrier;

import org.junit.Test;
import sun.nio.ch.ThreadPool;

import java.util.Queue;
import java.util.concurrent.*;

public class CyclicBarrierTest {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static final ThreadPoolExecutor th = new ThreadPoolExecutor(10,20,10,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
        @Override
        public void run() {
            System.out.println("拦截到3个线程之后再顺序执行");
        }
    });
    @Test
    public void cyclicBarrierTest(){


    }
}
