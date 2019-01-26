#include <jni.h>
 
#ifndef _Included_HelloJNI
#define _Included_HelloJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Package: cn.devmgr.jni
 * Class:     HelloJNI
 * Method:    sayHello
 * Signature: ()V
 * Java_ 后面是 包名类名和方法名，所有的点都用下划线代替
 */
JNIEXPORT int JNICALL Java_cn_devmgr_jni_HelloJNI_sayHello(JNIEnv *, jobject, jstring, jint);
 
#ifdef __cplusplus
}
#endif
#endif