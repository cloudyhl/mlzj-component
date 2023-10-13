package com.mlzj.component.monitor.server;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


class MonitorServerApplicationTests {

    @Test
    void contextLoads() {
    }

    //ssh远程执行命令
    @Test
    public void testA () {
        String host = "your_remote_host"; // 远程主机IP或主机名
        String user = "your_username";     // 远程用户名
        String password = "your_password"; // 密码

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("root", "127.0.0.1", 22);
            session.setPassword("root");

            // Skip host key check (not recommended for production)
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            // Command to get the process ID (you can modify this as per your requirements)
            String cmdToGetProcessId = "ipconfig"; // replace 'process_name' with the actual process name
            String pid = executeCommand(session, cmdToGetProcessId).trim();

            if (!pid.isEmpty()) {
                System.out.println("Killing process with PID: " + pid);
                String killCmd = "reboot";
                executeCommand(session, killCmd);
            }

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(Session session, String command) {
        String output = "";

        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                output += line + "\n";
            }

            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
    @Test
    public void testRestart () {
        String host = "your_remote_host"; // 远程主机IP或主机名
        String user = "your_username";     // 远程用户名
        String password = "your_password"; // 密码

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("root", "192.168.26.101", 22);
            session.setPassword("root");

            // Skip host key check (not recommended for production)
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            // Command to get the process ID (you can modify this as per your requirements)
            String cmdToGetProcessId = "shutdown -h now"; // replace 'process_name' with the actual process name
            String pid = executeCommand(session, cmdToGetProcessId).trim();


            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
