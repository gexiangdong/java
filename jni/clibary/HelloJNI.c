#include <jni.h>     
#include <stdio.h>    
#include <signal.h>
#include <setjmp.h>
#include "HelloJNI.h"   
 
static jmp_buf buf;

void handler(int sign) {
    printf("error sign  %d found.\n", sign);
    longjmp(buf, 1);   
}

// Implementation of the native method sayHello()
JNIEXPORT int JNICALL Java_cn_devmgr_jni_HelloJNI_sayHello(JNIEnv *env, jobject thisObj, jstring inStr, jint inInt) {
   signal(SIGFPE, handler); 
   signal(SIGSEGV, handler);
   if ( setjmp(buf) ) {
      printf("error jump\n");
      return -100;
   }
  
   const char *inCStr = (*env)->GetStringUTFChars(env, inStr, NULL);
   printf("Hello World from C with package! args: string [%s] int [%d] \n", inCStr, inInt);
   
   int ary[2];
   int i = 3 / ary[0];
   if( (*env)->ExceptionCheck(env) ){
        printf("error found.\n");
        return -1;
   }

   return i;
}

