# JNI sample



## clibary


如果C语言编写的方法中的程序crash，会导致java程序一起crash，因此需要在C中捕获错误并处理。

### 异常、出错

C语言没有异常机制，可通过信号量获得出错。

``` C
// 设置一个出错监控的函数
void handler(int sign) {
    printf("error sign  %d found.\n", sign);
    // 出错后会执行到这，但是这里不能用 exit(-1)等退出，退出会导致整个进程结束（含java部分）
}

// 登记，发生SIGSEGV信号时，交由上面的函数处理
signal(SIGSEGV, handler);

```

### setjum 相关

首先inclue <setjum.h>

``` C
#include <setjmp.h>

// 定义变量
static jmp_buf buf;

// 设置一个setjmmp点，设置后，当运行到 longjmp 函数时，会跳转到setjmp行
if ( setjmp(buf) ) {
    printf("error jump\n");
    return -100;
}


// 跳转语句
longjmp(buf, 1);  
```
    
## java

注意几点：

* Java的 package, class, native method 的名称必须和C里定义的保持一致，而且方法参数也需要一致


## 其他









## 参考

https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaNativeInterface.html
