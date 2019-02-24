# volatile 关键字

volatile 关键字用于：当一个线程修改了变量后，该修改立即对其他线程可见。基本类型，值即刻可见；引用类型，引用立刻可见，引用对象内的值还需要继续增加 volatile 才能保证后续值立即可见。

## 内存模型

程序在运行时，CPU从主存（RAM，平常称为内存）读取到CPU的缓存（一级缓存、二级缓存等），当多线程在不同的CPU内核执行时，变量就被拷贝到了不同的CPU缓存内；造成了一个变量有多份存在，CPU在运算时使用的是本地缓存的值。这可能会造成线程A修改了值，而线程B依旧在用之前的值。volatile就是解决这种问题，CPU在使用volatile修饰的变量时，会去主存读取。

但volatile也有局限性，线程A读取到某个被volatile修饰的变量i后，执行i++操作；而在执行i++前，读取后的时刻，线程B也做了同样的操作，这样一个修改会丢失。

## 使用场景

[VolatileTest.java](./VolatileTest.java) [VolatileTest2.java](./VolatileTest2.java) [ProducerConsumer.java](./ProducerConsumer.java) 三个类展示了几个 volatile 适合的场景。

## 使用局限

[AutoIncreasement.java](./AutoIncreasement.java) 展示了一个不能通过volatile实现的场景；这种情况需要用 syncsynchronized 来实现。
