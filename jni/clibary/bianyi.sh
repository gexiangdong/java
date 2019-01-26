
#gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin" -dynamiclib -o libhello.dylib HelloJNI.c

gcc -I /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include -I /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include/darwin -dynamiclib -o libhello.dylib HelloJNI.c