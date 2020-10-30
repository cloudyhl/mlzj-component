package com.mlzj.component.mq.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2020/10/29
 */
@Data
@Component
@ConfigurationProperties(prefix = "mlzj.mq")
public class ServerProperties {

    /**
     * 读超时时间
     */
    private Integer readerIdleTimeSeconds;

    /**
     * 写超时时间
     */
    private Integer writerIdleTimeSeconds;

    /**
     * 总超时时间
     */
    private Integer allIdleTimeSeconds;

    /**
     * mq服务的端口
     */
    private Integer serverPort;

    /**
     * workerGroupSize
     */
    private Integer workers;

}
