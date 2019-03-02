/**
 * synchorized(obj){
 *     //这里的代码都只能有一个线程执行
 *     obj.wait();
 *     //执行到这里时，其他线程已经用notify/nofityAll唤醒了wait，并且其他执行此段的线程已经退出了
 * }
 *
 * doA中 synchronized (obj) 进入后加锁，其他线程无法进入doA的此段，也无法进入doB中的synchronized (obj)部分；
 * 但doA中的 obj.wait 释放了锁，正因为这里释放后，其他线程才得以进入 doB 的synchronized (obj)部分，并执行
 * obj.notify ， notify方法会唤醒一个正在wait的线程（只唤醒一个，如果要唤醒所有用notifyAll)
 */
public class SyncObject {
    private Object obj = new Object();

    public void doA() throws InterruptedException{
        synchronized (obj) {
            System.out.println("inside doA synchronized (obj). " + Thread.currentThread().getName());
            System.out.print("{");
            Thread.sleep(2);
            System.out.println("inside doA synchronized (obj) before wait. " + Thread.currentThread().getName());
            obj.wait(); //执行wait()后，会释放掉锁并进入等待状态
            //执行到这里时，又获得了锁，
            System.out.println("inside doA synchronized (obj) after wait. " + Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.print("}");
        }
        System.out.println("stop doA() " + Thread.currentThread().getName());
    }

    public void doB() throws InterruptedException{
        Thread.sleep(1500);
        synchronized (obj) {
            obj.notify();   //notify, wait, notifyAll等必须在被对象的同步代码段中执行，否则抛出异常java.lang.IllegalMonitorStateException
            obj.notify();
            System.out.println("notify 2 objects " + Thread.currentThread().getName());
        }

        System.out.println("stop doB() " + Thread.currentThread().getName());
    }

    public static void main(String[] argvs) throws Exception {
        SyncObject so = new SyncObject();
        for(int i=0; i<15; i++){
            final int index = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (index < 10 ) {
                            so.doA();
                        } else {
                            so.doB();
                        }
                    }catch (InterruptedException e){
                        return;
                    }
                }
            }, "T" + index).start();
        }
    }
}


