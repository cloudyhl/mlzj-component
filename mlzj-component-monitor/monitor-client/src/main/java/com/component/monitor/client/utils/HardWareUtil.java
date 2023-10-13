package com.component.monitor.client.utils;




import com.component.monitor.client.hardware.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.util.*;

/**
 * 硬件信息工具类
 * @author yhl
 * @date 2023/7/25
 */
public class HardWareUtil {

    private HardWareUtil() {}

    /**
     * 等待休眠时间，单位ms
     */
    private static final int WAIT_TIME_MS = 1000;

    /**
     * 获取cpu信息
     *
     * @return  cpu信息
     */
    public static CpuInfo getCpuInfo() {
        SystemInfo si = new SystemInfo();
        CentralProcessor processor = si.getHardware().getProcessor();
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(WAIT_TIME_MS);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softIrq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long ioWait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setCpuNum(processor.getLogicalProcessorCount());
        cpuInfo.setTotal(totalCpu);
        cpuInfo.setSys(cSys);
        cpuInfo.setUsed(user);
        cpuInfo.setWait(ioWait);
        cpuInfo.setFree(idle);
        return cpuInfo;
    }

    /**
     * 获取内存信息
     *
     * @param size  大小单位，默认为B
     * @return      内存信息
     */
    public static MemoryInfo getMemoryInfo(SizeEnum size) {
        SystemInfo si = new SystemInfo();
        GlobalMemory memory = si.getHardware().getMemory();
        // 内存信息
        MemoryInfo mem = new MemoryInfo();
        mem.setTotal(Objects.isNull(size) ? memory.getTotal() : (float) memory.getTotal() / size.getSize());
        mem.setUsed(Objects.isNull(size) ? (memory.getTotal() - memory.getAvailable()) : (float) (memory.getTotal() - memory.getAvailable()) / size.getSize());
        mem.setFree(Objects.isNull(size) ? memory.getAvailable() : (float) memory.getAvailable() / size.getSize());
        return mem;
    }

    /**
     * 获取服务器信息
     *
     * @return  服务器信息
     */
    public static SystemDetails getSystemInfo() throws UnknownHostException {
        // 服务器信息
        SystemDetails details = new SystemDetails();
        Properties props = System.getProperties();
        details.setComputerName(InetAddress.getLocalHost().getHostName());
        details.setComputerIp(getRealIp());
        details.setOsName(props.getProperty("os.name"));
        details.setOsArch(props.getProperty("os.arch"));
        details.setUserDir(props.getProperty("user.dir"));
        return details;
    }

    /**
     * 获取Java虚拟机信息
     */
    public static JvmInfo getJvmInfo() {
        JvmInfo jvmInfo = new JvmInfo();
        Properties props = System.getProperties();
        jvmInfo.setTotal(Runtime.getRuntime().totalMemory());
        jvmInfo.setMax(Runtime.getRuntime().maxMemory());
        jvmInfo.setFree(Runtime.getRuntime().freeMemory());
        jvmInfo.setVersion(props.getProperty("java.version"));
        jvmInfo.setHome(props.getProperty("java.home"));
        return jvmInfo;
    }

    /**
     * 获取磁盘信息
     */
    public static List<SysFile> getSysFiles() {
        SystemInfo si = new SystemInfo();
        // 获取操作系统
        OperatingSystem operatingSystem = si.getOperatingSystem();
        // 获取操作系统的文件系统
        FileSystem fileSystem = operatingSystem.getFileSystem();
        // 获取文件存储信息
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        List<SysFile> sysFiles = new ArrayList<>();
        for (OSFileStore fs : fsArray) {
            // 获取可用空间
            long free = fs.getUsableSpace();
            // 获取总空间
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(mul(div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
        return sysFiles;
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        if (size >= SizeEnum.GB.getSize()) {
            return String.format("%.1f GB", (float) size / SizeEnum.GB.getSize());
        } else if (size >= SizeEnum.MB.getSize()) {
            float f = (float) size / SizeEnum.MB.getSize();
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= SizeEnum.KB.getSize()) {
            float f = (float) size / SizeEnum.KB.getSize();
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static String getRealIp() {
        try {
            //获取到所有的网卡
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface =  allNetInterfaces.nextElement();
                // 去除回环接口127.0.0.1，子接口，未运行的接口
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                //获取名称中是否包含 Intel Realtek 的网卡
                if (!netInterface.getDisplayName().contains("Intel")
                        && !netInterface.getDisplayName().contains("Realtek")
                        && !netInterface.getDisplayName().contains("Atheros")
                        && !netInterface.getDisplayName().contains("Broadcom")) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                System.out.println(netInterface.getDisplayName());
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null) {
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
                break;
            }
        } catch (SocketException e) {

        }
        return null;
    }
}
