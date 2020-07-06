package com.mlzj.component.mq.common.protocol;

import lombok.Data;

/**
 * @author yhl
 * @date 2020/5/20
 */
@Data
public class MlzjMessage<T> {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息队列
     */
    private String queue;

    /**
     * 数据域
     */
    private T data;
}
