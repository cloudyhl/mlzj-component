package com.mlzj.component.mq.client.consumer;

import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public abstract class SimpleMqConsumer extends AbstractMqConsumer{

    @Resource
    private MlzjMqProperties mlzjMqProperties;

    @Override
    public MlzjMqProperties getMlzjMqProperties() {
        return mlzjMqProperties;
    }

    @PostConstruct
    void init() {
        super.initMqChannel();
    }
}
