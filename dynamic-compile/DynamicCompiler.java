import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class DynamicCompiler{

    /**
     * 动态编译传入的类源码并返回编译好的类
     * @param sourceCode
     * @return
     * @throws Exception
     */
    public Class compile(String sourceCode) throws Exception{
        File distDir = new File("target");
        if (!distDir.exists()) {
            distDir.mkdirs();
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String className = getFullClassNameFromCode(sourceCode);
        JavaFileObject javaFileObject = new CodeJavaFileObject(className, sourceCode);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, null,
                Arrays.asList("-d", distDir.getAbsolutePath()), null,
                Arrays.asList(javaFileObject));
        boolean compileSuccess = task.call();
        if (!compileSuccess) {
            System.out.println("compile failed");
            return null;
        } else {
            //动态执行 (反射执行)
            System.out.println("compile successed " + distDir.getAbsolutePath());
            //URL 需要以 file:// 开始； 如果是目录需要以 / 结束；也可以是jar
            URL[] urls = new URL[] {new URL("file://" + distDir.getAbsolutePath() + "/")};
            URLClassLoader classLoader = new URLClassLoader(urls);
            Class dynamicClass = classLoader.loadClass(className);
            return dynamicClass;
        }
    }


    class CodeJavaFileObject extends SimpleJavaFileObject{
        private String code;

        public CodeJavaFileObject(String className, String code){
            super(URI.create(className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    private String getFullClassNameFromCode(String code){
        String t = code.substring(0, code.indexOf('{'));
        String s = t.replaceAll("[\r\n\t]", " ").trim();
        String[] ary = s.split("[;]");
        String packageName = null;
        String className = getLastPart((ary[ary.length - 1]));
        if(ary[0].startsWith("package ")){
            packageName = getLastPart(ary[0]);
            return packageName + "." + className;
        }else{
            return className;
        }
    }

    private String getLastPart(String s){
        String[] ary = s.trim().split(" ");
        return ary[ary.length - 1];
    }

    public static void main(String[] argvs) throws Exception{
        StringBuffer buf = new StringBuffer();
        buf.append("package a.b.c;\r\npublic class ANumber{")
                .append("public int getNumber() {")
                .append("System.out.println(\"Hello World in getNumber()!\"); return 999;")
                .append("}")
                .append("}");

        DynamicCompiler dc = new DynamicCompiler();
        Class cls = dc.compile(buf.toString());
        System.out.println(cls.getName());
        Object obj = cls.getDeclaredConstructor().newInstance();
        Method method = cls.getDeclaredMethod("getNumber");
        Object r = method.invoke(obj);
        System.out.println("result is " + r);
    }
}