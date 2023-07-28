package com.component.monitor.client.hardware;

import lombok.Data;

/**
 * 系统相关信息
 * @author yhl
 * @date 2023/7/25
 */
@Data
public class SystemDetails {
    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

}


