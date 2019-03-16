/**
 * 当程序在短时间内同一代码位置抛出大量空指针异常时；JVM会使用预先创建好的NullPointerException；
 * 着会导致此异常不携带出错代码的对战信息。 （大量才会出现，而且前面几个NPE时正常的）
 * 
 * 这是JVM的一个优化机制，抛出的是事先创建好的NPE
 * 可以给JVM增加参数 -XX:-OmitStackTraceInFastThrow 禁止JVM这么做
 * 
 */
public class NpeThief {
    public void callManyNPEInLoop() {
        int preLen = -1;
        for (int i = 0; i < 5000000; i++) {
            if(i % 100000 == 0){
                //System.out.println(i + " ...");
            }
            String location = "";
            try {
                if( i < 1000000){
                    location = "routeA()";
                    //System.out.println("a");
                    routeA();
                }else if( i < 2000000){
                    location = "routeB()";
                    //System.out.println("a");
                    routeB();
                }else if( i < 3000000){
                    location = "routeC()";
                    //System.out.println("a");
                    routeC();
                }else if( i < 4000000){
                    location = "routeD()";
                    //System.out.println("a");
                    routeD();
                }else{
                    location = "routeE()";
                    //System.out.println("     b");
                    routeE();
                }
            } catch (Exception e) {
                if(e.getStackTrace().length != preLen){
                    // e.getStackTrace().length == 0 的是无对战信息的空指针； 
                    // 如果堆栈深度发生变化，会打印出来发生变化时 i 的值和当前堆栈深度
                    System.out.println(location + " #" + i + "   " + e.getStackTrace().length);
                    preLen = e.getStackTrace().length;
                }
            }
        }
    }
    private void routeA() {
        causeNpe();
    }
    private void routeB() {
        causeNpe();
    }    
    private void routeC() {
        causeNpe();
    }
    private void routeD() {
        causeNpe();
    }    
    private void routeE() {
        causeNpe();
    }
    private void routeF(){
        causeNpe();
    }
    private void causeNpe(){
        ((Object)null).getClass();

    }

    public static void main(String[] args) {
        NpeThief thief = new NpeThief();
        long t1 = System.currentTimeMillis();
        thief.callManyNPEInLoop();
        System.out.println("cost " + (System.currentTimeMillis() - t1) + "ms.");
    }
}