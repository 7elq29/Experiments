package deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ken on 11/11/17.
 */
public class DeadLock {

    /*
     * Mutual exclusion
     * Hold and wait or resource holding
     * No preemption
     * Circular wait
     */

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public void modifyResource1(String taskName) throws InterruptedException {
        System.out.println(taskName+": acquiring lock1......");
        lock1.lock();
        System.out.println(taskName+": acquired lock1");
        TimeUnit.SECONDS.sleep(5);
        System.out.println(taskName+": acquiring lock2......");
        lock2.lock();
        System.out.println(taskName+": acquired lock2");
        lock1.unlock();
        lock2.unlock();
    }

    public synchronized void modifyResource2(String taskName) throws InterruptedException {
        System.out.println(taskName+": acquiring lock2......");
        lock2.lock();
        System.out.println(taskName+": acquired 2");
        TimeUnit.SECONDS.sleep(5);
        System.out.println(taskName+": acquiring lock1......");
        lock1.lock();
        System.out.println(taskName+": acquired lock1");
        lock2.unlock();
        lock1.unlock();
    }

    public void start(){
        Runnable task1 = () -> {
            try {
                String taskName = Thread.currentThread().getName();
                modifyResource1(taskName);
                System.out.println(taskName+": Finished modification of resource 1, prepare to modify resource2");
                modifyResource2(taskName);
                System.out.println(taskName+": All finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable task2 = () -> {
            try {
                String taskName = Thread.currentThread().getName();
                modifyResource2(taskName);
                System.out.println(taskName+": Finished modification of resource2, prepare to modify resource1");
                modifyResource1(taskName);
                System.out.println(taskName+": All finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
    }

    public static void main(String[] args){
        DeadLock deadLock = new DeadLock();
        deadLock.start();
    }


}
