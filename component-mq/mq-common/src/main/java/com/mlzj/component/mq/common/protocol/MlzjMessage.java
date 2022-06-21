package com.mlzj.component.mq.common.protocol;

import lombok.Data;

/**
 * @author yhl
 * @date 2020/5/20
 */
@Data
public class MlzjMessage {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息队列
     */
    private String queue;

    /**
     * 模式
     */
    private Integer mode;

    /**
     * 数据域
     */
    private String data;
}
