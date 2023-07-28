package com.component.monitor.client.enums;

import lombok.AllArgsConstructor;

/**
 * @author yhl
 * @date 2023/7/25
 */
@AllArgsConstructor
public enum SystemTypeEnum {

    WINDOWS("WINDOWS", "WINDOWS"),
    LINUX("LINUX", "LINUX");

    /**
     *
     */
    private final String type;

    /**
     *
     */
    private final String desc;
}
