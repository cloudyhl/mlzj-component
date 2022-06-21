package com.mlzj.component.mq.client.config;

import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yhl
 * @date 2020/3/31
 */
@Configuration
@ComponentScan("com.mlzj.component.mq.client")
@EnableConfigurationProperties({MlzjMqProperties.class})
public class MlzjMqConfig {
}
