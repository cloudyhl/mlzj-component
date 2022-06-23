package com.mlzj.component.mq.client.annotation;

import com.mlzj.component.mq.common.constants.MessageModeEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MlzjMqConsumer {

    /**
     * 模式名称
     * @return 模式名称 MessageModeEnum
     */
    MessageModeEnum mode();

    /**
     * 主题或队列名称
     * @return 主题或队列名称
     */
    String queueOrTopicName() default "";

}
