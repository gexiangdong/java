# NullPointerException 异常的对战丢失问题

 当程序在短时间内同一代码位置抛出大量空指针异常时；JVM会使用预先创建好的NullPointerException, 这会导致此异常不携带出错代码的对战信息。 （大量才会出现，而且前面几个NPE时正常的）。这是JVM的一个优化机制。

 如果需要禁止，可以给JVM增加参数 -XX:-OmitStackTraceInFastThrow 。

 NpeThief.java  这段代码可以重现这个现象。