package com.component.monitor.client.hardware;

import lombok.Data;

/**
 * 內存相关信息
 * @author yhl
 * @date 2023/7/25
 */
@Data
public class MemoryInfo {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

}

