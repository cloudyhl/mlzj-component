package com.component.monitor.client.hardware;
import lombok.Data;

/**
 * CPU相关信息
 * @author yhl
 * @date 2023/7/25
 */
@Data
public class CpuInfo {
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;

}

