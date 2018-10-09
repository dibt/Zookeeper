package com.di.cyclicbarrier;

import org.junit.Test;

import java.util.concurrent.*;

public class CyclicBarrierTest {

    //创建线程池
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "线程通知:所有线程都准备好了,可以继续工作了");
        }
    });

    @Test
    public void cyclicBarrierTest() {
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(new MyRunnable());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "准备好了,等待去除cyclicbarrier");
                cyclicBarrier.await();
                System.out.println("去除cyclicbarrier," + Thread.currentThread().getName() + "继续工作");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
