
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition.await() 会释放掉lock的锁，并让线程进入等待状态；
 * 被condition.signal() 唤醒后， 之前await释放掉的锁，会自动尝试获取回了
 *
 * Condition.await() 和 object.wait()类似， condition.signal()和object.notify()类似
 *
 */
public class LockWithConditionTest<E> {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[10];
    int putptr, takeptr, count;

    public void put(E x) throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " put() locked.");
        try {
            //这里是比较容易纠结的地方
            //这里需要有个循环重新判断状态，因为其他线程通过signal()欢迎线程a的await后（拿到锁），可能在a拿到锁之前
            //有线程b也进入了put先行在数组中又放了数据，把数组再次放满了。b拿到锁是由于其他线程unlock后拿到的锁
            //根据condition signal 被唤醒的await肯定是只有一个线程执行
            //但是可能之前又别的线程先执行完了，把数据拿走了，判断条件count==items.length又成立了
            while (count == items.length) {
                //已经放满了
                System.out.println(Thread.currentThread().getName() + " put before await().");
                notFull.await();
                System.out.println(Thread.currentThread().getName() + " put after await().");
            }
            //把X放入items数组
            items[putptr] = x;
            if (++putptr == items.length){
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            System.out.println(Thread.currentThread().getName() + " put() unlocked.");
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " take() locked.");
        try {
            while (count == 0) {
                //已经没数据了
                System.out.println(Thread.currentThread().getName() + " take before await().");
                notEmpty.await();
                System.out.println(Thread.currentThread().getName() + " take after await().");
            }
            E x = (E) items[takeptr];
            if (++takeptr == items.length){
                takeptr = 0;
            }
            System.out.println(Thread.currentThread().getName() + " take() got " + x);
            --count;
            notFull.signal();
            return x;
        } finally {
            System.out.println(Thread.currentThread().getName() + " take() unlocked.");
            lock.unlock();
        }
    }

    public static void main(String[] argvs) throws Exception{
        Thread[] threads = new Thread[100];
        LockWithConditionTest<String> lwc = new LockWithConditionTest<>();
        for(int i=0; i<threads.length; i++){
            final int index = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (index <= threads.length / 2) {
                            lwc.put(String.valueOf(index));
                        }else{
                            lwc.take();
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }, "T" + index);
            threads[i].start();
        }
    }
}
