
import java.util.concurrent.locks.ReentrantLock;


/**
 * 可重入锁，被一个线程获得后，其他线程只能等待unlock后才可能获得。
 * 已获得锁的线程可以多次lock，多次lock需要多次unlock才能释放给其他线程
 *
 * 如果不确定是否完全unlock，可以用 getHoldCount()查询
 * while(lock.getHoldCount() > 0){
 *     lock.unlock();
 * }
 * 程序中应该用
 * lock.lock();
 * try{
 *     //do something
 * }finally{
 *     lock.unlock()
 * }
 * 的形式来确保每次锁都有对应的解锁
 */
public class LockTest {
    private ReentrantLock lock = new ReentrantLock();

    public void methodUseLock() throws InterruptedException{
        System.out.println("进入方法 methodUseLock() Thread Name:" + Thread.currentThread().getName());
        lock.lock();
        lock.lock();
        System.out.println("已经加锁 Thread Name:" + Thread.currentThread().getName());
        Thread.sleep(500);
        System.out.println("methodUseLock() after sleep 500ms. Thread Name:" + Thread.currentThread().getName());
        doOthers();
        System.out.println("methodUseLock() after doOthers. Thread Name:" + Thread.currentThread().getName());
        Thread.sleep(1000);
        System.out.println("methodUseLock() after sleep 1000. Thread Name:" + Thread.currentThread().getName());
        lock.unlock();
        System.out.println("methodUseLock() 解锁 Thread Name:" + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("methodUseLock(), 再次解锁 Thread Name:" + Thread.currentThread().getName());
        lock.unlock();

    }

    private void doOthers() throws InterruptedException{
        System.out.println("doOthers(), 再次加锁前. Thread Name:" + Thread.currentThread().getName());
        lock.lock();
        System.out.println("doOthers(), 已加锁. Thread Name:" + Thread.currentThread().getName());
        Thread.sleep(2000);
        lock.unlock();
        System.out.println("doOthers(), 解锁 Thread Name:" + Thread.currentThread().getName());
    }

    public static void main(String[] argvs) throws Exception{
        final LockTest lt = new LockTest();
        for(int i=0; i<3; i++){
            final int index = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        lt.methodUseLock();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                        return;
                    }
                }
            }, "T-" + index).start();
        }
    }
}
