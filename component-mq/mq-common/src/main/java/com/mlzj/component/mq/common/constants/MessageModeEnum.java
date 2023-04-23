package com.mlzj.component.mq.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private final Integer mode;

    /**
     * 描述
     */
    private final String desc;


}
