package com.mlzj.component.mq.client.annotation;

import com.mlzj.component.mq.client.config.MlzjMqConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yhl
 * @date 2020/3/31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({MlzjMqConfig.class})
public @interface EnableMlzjMq {
}
