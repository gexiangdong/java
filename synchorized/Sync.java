

/**
 * 
 * doA 和 doB 两个方法，同一时刻只能运行一个；
 * doC 和 doD 两个方法，同一时刻只能运行一个；
 * 
 * 一个类中，所有非静态的有sychronized关键字的方法同时只能运行一个，并且和 synchronized(this)竞争。
 * 静态方法有synchronized时，和 synchronized(所属类.class) 竞争。
 * 
 * 一个方法定义时增加了synchronized，这个方法最多只有一个线程在运行，但有线程在等待，并不一定是因为有
 * 其他线程在运行这个方法，也可能是在运行其他有synchronize的方法，或synchronized(this)的代码块。
 */
public class Sync {

    public void doA() throws InterruptedException{
       synchronized (this) {
            System.out.println("start doA() " + Thread.currentThread().getName());
            Thread.sleep(1500);
            System.out.println("stop doA() " + Thread.currentThread().getName());
        }
    }

    public synchronized void doB() throws InterruptedException{
        System.out.println("start doB() " + Thread.currentThread().getName());
        Thread.sleep(1000);
        System.out.println("stop doB() " + Thread.currentThread().getName());
    }

    public synchronized static void doC() throws InterruptedException{
        System.out.println("start doC() " + Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("stop doC() " + Thread.currentThread().getName());
    }

    public static void doD() throws InterruptedException{
        synchronized(Sync.class){
            System.out.println("start doD() " + Thread.currentThread().getName());
            Thread.sleep(500);
            System.out.println("stop doD() " + Thread.currentThread().getName());
        }
    }
    public static void main(String[] argvs) throws Exception {
        Sync s = new Sync();
        for(int i=0; i<4; i++){
            final int index = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (index % 4 == 0) {
                            s.doA();
                        } else if (index % 4 == 1){
                            s.doB();
                        }else if (index % 4 == 2){
                            Sync.doC();
                        }else{
                            Sync.doD();
                        }
                    }catch (InterruptedException e){
                        return;
                    }
                }
            }, "T-" + index).start();
        }
    }
}

