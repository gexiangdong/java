/**
 * 这是 volatile 使用场景的一个例子
 * 
 * 和TestVolatile类类似；只是这里是静态变量
 */
public class VolatileTest2 {
    // 如果没有 volatile 关键字，很容易产生 ChangeLister 没有读取到变化的状况
    // private static int counter = 0;
    private static volatile int counter = 0;

    public static void main(String[] args) throws Exception{
        Thread t1 = new ChangeListener();
        Thread t2 = new ChangeMaker();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class ChangeListener extends Thread {
        @Override
        public void run() {
            int localCounter = counter;
            while (localCounter < 100) {
                if (localCounter != counter) {
                    System.out.println("Got Change for counter : " + counter);
                    localCounter = counter;
                }
                // 如果写了下面这段代码； 即使 counter 没有 volatile 关键字修饰，也很难产生读不到变化的状况
                // 因为cpu有时间从内存同步数据到缓存
                // System.out.println("ChangeListener  localCounter=" + localCounter + "; counter=" + counter);
                // try{
                //     Thread.sleep(40);
                // }catch(InterruptedException e){
                // }
            }
        }
    }

    static class ChangeMaker extends Thread {
        @Override
        public void run() {

            int localCounter = counter;
            while (counter < 100) {
                System.out.println("Incrementing counter to " + (localCounter + 1));
                counter = ++localCounter;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}