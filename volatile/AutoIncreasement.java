/**
 * 这是一个 volatile 关键字功能有限的例子；不适合使用 volatile 的例子
 * 
 * volatile 修饰的成员变量在多线程下，被读取并赋值，赋值依赖读取的值会出错。
 * 此例中add方法的结果会少于1000000；而使用了关键字 synchronized 修饰的 syncAdd 方法则会得到正确的结果。
 * 这时 volatile 相对于 synchronized 局限性的一个例子。
 * 
 * add() 结果不正确发生有一定几率，如果一直不发生，可尝试更改add中for循环的次数，次数高时，CPU更加
 * 繁忙，而没有时间同步CPU的缓存和主存数据；容易出现使用的缓存数据和主存不同步，进而结果不是期望的。
 */
public class AutoIncreasement{
    private volatile int counter = 0;       //即使有volatile也没用；这个不是volatile能解决的问题
    private int syncCounter = 0;            //方法上有synchronized关键字；这里不需要volatile

    public void add(){
        //System.out.println(Thread.currentThread().getId() + " add from " + count);
        for(int i=0; i<10000; i++){
            counter ++;
        }
        //System.out.println(Thread.currentThread().getId() + " done with " + count);
    }

    public synchronized void syncAdd(){
        for(int i=0; i<10000; i++){
            syncCounter ++;
        }
    }

    public static void main(String[] argvs) throws Exception{
        long t1 = System.currentTimeMillis();
        final AutoIncreasement ai1 = new AutoIncreasement();
        runAdd(new Runnable(){
                @Override
                public void run() {
                    ai1.add();
                }
            });
        System.out.println("add cost: " + (System.currentTimeMillis() - t1) + "ms, result=" + ai1.counter);

        t1 = System.currentTimeMillis();
        final AutoIncreasement ai2 = new AutoIncreasement();
        runAdd(new Runnable(){
                @Override
                public void run() {
                    ai2.syncAdd();
                }
            });
        System.out.println("syncAdd cost: " + (System.currentTimeMillis() - t1) + "ms, result=" + ai2.syncCounter);
    }

    private static void runAdd(Runnable runnable) throws Exception{
        Thread[] threads = new Thread[100];
        for(int i=0; i<threads.length; i++){
            threads[i] = new Thread(runnable);
        }
        for(int i=0; i<threads.length; i++){
            threads[i].start();
        }
        for(int i=0; i<threads.length; i++){
            threads[i].join();
        }
    }
}