package com.mlzj.component.mq.server.domain;

import lombok.Data;

/**
 * @author yhl
 * @date 2022/7/1
 */
@Data
public class ChannelMark {


    /**
     * 应用id
     */
    private String applicationName;

    /**
     * 类型
     */
    private Integer mode;

}
