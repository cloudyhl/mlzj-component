package com.component.monitor.client;


import com.component.monitor.client.hardware.*;
import com.component.monitor.client.utils.HardWareUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Slf4j
public class CommonTest {

    @Test
    public void testCommand() {
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        String osArch = props.getProperty("os.arch"); //操作系统构架
        String osVersion = props.getProperty("os.version"); //操作系统版本
    }

    @Test
    public void HardWare() throws UnknownHostException {
        SystemDetails systemInfo = HardWareUtil.getSystemInfo();
        System.out.println(systemInfo);
        CpuInfo cpuInfo = HardWareUtil.getCpuInfo();
        System.out.println(cpuInfo);
        MemoryInfo memoryInfo = HardWareUtil.getMemoryInfo(SizeEnum.MB);
        System.out.println(memoryInfo);
        JvmInfo jvmInfo = HardWareUtil.getJvmInfo();
        System.out.println(jvmInfo);
        List<SysFile> sysFiles = HardWareUtil.getSysFiles();
        System.out.println(sysFiles);


    }

    //java执行命令
    @Test
    public void execCommand() throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec("cmd.exe /c dir D:");
        StreamGobbler streamGobbler =
                new StreamGobbler(exec.getInputStream(), System.out::println);
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        Thread.sleep(1000);
    }


    //监控日志文件
    @Test
    public void tailFile () {
        TailerListener listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                try {
                    log.info("日志新增的内容为：" + line);
                } catch (Exception e) {
                    log.error("发生异常：" + e.getMessage());
                }
            }
        };
        Tailer tailer = new Tailer(new File("E:\\logs.txt"),
                listener, Integer.valueOf(1000), true);
        tailer.run();
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @SneakyThrows
        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream, "GBK")).lines()
                    .forEach(consumer);
        }
    }

    //检查端口是否通的
    @Test
    public void telNet() throws IOException {
        Socket socket = new Socket("127.0.0.1", 306);
        System.out.println(true);
    }
}
