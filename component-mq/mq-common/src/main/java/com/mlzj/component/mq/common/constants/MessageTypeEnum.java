package com.mlzj.component.mq.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author yhl
 * @date 2020/10/29
 */
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {

    /**
     * 注释
     */
    LOGIN(1, "通道注册"),
    MESSAGE(2, "普通消息"),
    HEART(3, "心跳");

    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;

}
