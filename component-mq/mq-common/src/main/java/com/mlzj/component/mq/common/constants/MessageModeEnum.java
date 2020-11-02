package com.mlzj.component.mq.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yhl
 * @date 2020/10/30
 */
@Getter
@AllArgsConstructor
public enum MessageModeEnum {

    /**
     * 这是注释
     */
    TOPIC(1, "主题模式"),
    QUEUE(2, "队列模式"),
    SEND(3, "发送者模式");

    /**
     * 模式
     */
    private Integer mode;

    /**
     * 描述
     */
    private String desc;

}
