package com.mlzj.component.mq.client.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2020/3/31
 */
@Data
@ConfigurationProperties(prefix = "mlzj.mq")
public class MlzjMqProperties {

    /**
     * url
     */
    private String serverUrl;

    /**
     * 端口
     */
    private Integer port;
}
