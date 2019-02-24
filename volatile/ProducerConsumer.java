import java.util.*;

/**
 * 这是 volatile 使用场景的一个例子
 * 
 * 这个例子在定义成员变量 hasValue 时，如果不增加 volatile 关键字修饰，只是有几率会遇到死锁；
 * 并不是一定遇到。
 * 把 produce 或 consume 方法中 while 语句内的语句都注释掉（输出和sleep，对逻辑无影响），会
 * 大大提高出现死锁的概率，几乎每次都遇到。 这是因为CPU繁忙，没有时间供缓存和主存交换数据。
 * 使用 volatile 关键字修饰 hasValue 成员变量后，即使注释掉while内的语句，也不会造成死锁。因
 * 为 volatile 限定了cpu必须使用主存内数据, 不能仅仅依赖缓存
 * 
 */
public class ProducerConsumer {
    private String value = "";
    private boolean hasValue = false;  //无 volatile 关键字，有一定几率线程无法结束
    // private volatile boolean hasValue = false;  //增加 volatile 关键字；则不会出现死锁

    public void produce(String value) {
        while (hasValue) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Producing " + value + " as the next consumable");
        this.value = value;
        hasValue = true;
    }

    public String consume() {
        while (!hasValue) {
            System.out.println("consume() hasValue=" + hasValue);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String value = this.value;
        hasValue = false;
        System.out.println("Consumed " + value);
        return value;
    }

    public static void main(String[] argvs) throws Exception {
        ProducerConsumer producerConsumer = new ProducerConsumer();
        List<String> values = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");
        Thread writerThread = new Thread(() -> {
            values.stream().forEach(producerConsumer::produce);
            System.out.println("writerThread exit");
        });
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < values.size(); i++) {
                //System.out.println("readerThread wait for value " + producerConsumer.hasValue);
                producerConsumer.consume();
            }
            System.out.println("readerThread exit");
        });
        writerThread.start();
        readerThread.start();
        writerThread.join();    //等待线程结束
        readerThread.join();
    }
}