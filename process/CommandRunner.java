import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 从java运行外部命令的例子
 */
public class CommandRunner {

    public static void main(String[] argvs) throws Exception{
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("arp -a");
        // 下面是获取process.getInputStream()，也就是进程的输出
        // 同样也可以通过process.getOutputStream() 得到进程需要输入的流，有些进程需要从控制台读取一些输入，例如删除文件时，可能需要输入y确认。
        try(
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(isr);
                ){
            String line = bufferedReader.readLine();
            while(line != null){
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        }
        // 调用waitFor，一直等待到进程结束；返回值是进程的退出值
        // 如果是c编写的进程，main函数返回值；
        // 返回值也可以用prcess.exitValue() 获取
        int exitValue = process.waitFor();
        System.out.println("exit with code: " + exitValue);
    }
}
