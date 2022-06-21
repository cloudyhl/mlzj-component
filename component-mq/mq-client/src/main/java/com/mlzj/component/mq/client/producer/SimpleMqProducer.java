package com.mlzj.component.mq.client.producer;

import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2022/6/20
 */
@Component
public class SimpleMqProducer extends AbstractMqProducer {

    @Resource
    private MlzjMqProperties mlzjMqProperties;

    public SimpleMqProducer() throws InterruptedException {
    }


    @Override
    public MlzjMqProperties getMlzjMqProperties() {
        return this.mlzjMqProperties;
    }

    @PostConstruct
    void init() throws InterruptedException {
        super.initMqChannel();
    }

}
