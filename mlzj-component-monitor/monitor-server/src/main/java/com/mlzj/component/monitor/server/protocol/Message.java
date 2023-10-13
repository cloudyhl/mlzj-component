package com.mlzj.component.monitor.server.protocol;

import lombok.Data;

/**
 * @author yhl
 * @date 2020/5/20
 */
@Data
public class Message {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息队列
     */
    private String ip;

    /**
     * 命令
     */
    private String command;

    /**
     * 其他数据
     */
    private String data;
}
