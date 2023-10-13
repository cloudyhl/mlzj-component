package com.mlzj.component.monitor.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2020/10/29
 */
@Data
@Component
@ConfigurationProperties(prefix = "mlzj.monitor")
public class ServerProperties {


    /**
     * mq服务的端口
     */
    private Integer serverPort;

    /**
     * workerGroupSize
     */
    private Integer workers;

}
