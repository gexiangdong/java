/**
 * 这是 volatile 使用场景的一个例子
 * 
 * 这个例子中，如果不使用 volatile 关键字 定义成员变量 shouldExit， 有一定几率
 * 会出现 waitForExit 方法一直无法退出的状况。
 */
public class VolatileTest{
    private boolean shouldExit = false;             //无 volatile 关键字，有一定几率线程无法结束
    //private volatile boolean shouldExit = false; //增加volatile关键字，则不会出现无法退出线程状况
    int x = 0;

    public void setShouldExit(boolean newValue){
        x = 3;
        System.out.println(Thread.currentThread().getId() + " setShouldExit to " + newValue);
        shouldExit = newValue;
    }

    public void waitForExit(){
        System.out.println(Thread.currentThread().getId() + " waitingForExit() x=" + x);
        while(!shouldExit){
            // 注释掉这个循环体内的其他语句（下面的system.out.print以及sleep等）并且成员变量 shouldExit 没有 volatile 修饰符
            // 则很容出现无法退出的状况； 增加 volatile 后程序会正常退出，不会卡在此处死循环
            // 下面的语句执行时，可能会给CPU和主存交换数据的时间，不会造成CPU过度繁忙，会大大减少读不到已经改变了的变量而进入死循环的几率
            // System.out.println(Thread.currentThread().getId() + " shouldExit = " + shouldExit + "; x=" + x);
            // try{
            //     Thread.sleep(5);
            // }catch(InterruptedException e){
            // }
        }
        System.out.println("x=" + x);
        System.out.println("Thread #" + Thread.currentThread().getId() + " end");
    }

    public static void main(String[] argvs) throws Exception{
        final VolatileTest tv = new VolatileTest();
        for(int i=0; i<5; i++){
            new Thread(new Runnable(){
            
                @Override
                public void run() {
                    tv.waitForExit();
                }
            }).start();
        }

        new Thread(new Runnable(){
        
            @Override
            public void run() {
                //2秒后设置
                try{
                    Thread.sleep(300);
                }catch(InterruptedException e){

                }
                System.out.println(Thread.currentThread().getId() + " will set shouldExit to true");
                tv.setShouldExit(true);
            }
        }).start();

        
    }
}