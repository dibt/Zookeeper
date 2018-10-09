package com.di.countdownlatch;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 通过CountDownLatch实现CyclicBarrier
 * 但是注意的是必须先调用countDown(）方法，才能调用await()方法，
 * 因为一旦调用await()方法，该线程后面的内容便不再执行，那么count值无法改变.
 */
public class CountDownLatchTest {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    //阻塞其他线程,等待主线程调用countDown()方法统一开始
    private static final CountDownLatch startCountDownLatch = new CountDownLatch(1);

    //阻塞主线程,等待其他线程执行完毕
    private static final CountDownLatch endCountDownLatch = new CountDownLatch(10);

    @Test
    public void countDownLatchTest() {
        try {
            for (int i = 0; i < 10; i++) {
                threadPoolExecutor.execute(new MyRunnable());
            }
            System.out.println("---------start-----------");
            startCountDownLatch.countDown();
            endCountDownLatch.await();
            Thread.sleep(10000);
            System.out.println("---------end-----------");
        } catch (InterruptedException e) {

        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                startCountDownLatch.await();
                System.out.println(Thread.currentThread().getName() + "线程准备好");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "线程完成工作");
                endCountDownLatch.countDown();
            } catch (InterruptedException e) {

            }
        }
    }
}
