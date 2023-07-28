package com.component.monitor.client.hardware;

import lombok.Data;

/**
 * JVM相关信息
 * @author yhl
 * @date 2023/7/25
 */
@Data
public class JvmInfo
{
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

}
