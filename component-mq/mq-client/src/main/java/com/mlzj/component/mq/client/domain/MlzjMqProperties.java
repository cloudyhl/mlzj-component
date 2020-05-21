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
@Component
public class MlzjMqProperties {

    private String serverUrl;
}
