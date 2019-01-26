package cn.devmgr.jni;

/**
 * 此处的类名(package, method)必须和 libary中定义方法时方法名对应
 */
public class HelloJNI {  

   static {
      System.loadLibrary("hello"); // windows 会加载 hello.dll UNIX加载 libhello.so MacOS加载 libhello.dylib
   }
 
   private native int sayHello(String s, int i);
 
   public static void main(String[] args) {
      HelloJNI helloJNI = new HelloJNI();
      System.out.println("return " + helloJNI.sayHello("Java String", 9876));
   }
}