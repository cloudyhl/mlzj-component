package com.component.monitor.client.utils;

import java.util.Properties;

/**
 * @author yhl
 * @date 2023/7/25
 */
public class SystemUtils {

    private static Properties props = System.getProperties(); //获得系统属性集
    private SystemUtils() {

    }


    /**
     * 获取操作系统名称
     * @return  操作系统名称
     */
    public static String getOSName() {
        return props.getProperty("os.name");
    }

}
