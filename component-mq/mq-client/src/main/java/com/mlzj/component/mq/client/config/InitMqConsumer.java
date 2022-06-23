package com.mlzj.component.mq.client.config;

import com.mlzj.component.mq.client.annotation.MlzjMqConsumer;
import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import com.mlzj.component.mq.common.constants.MessageModeEnum;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2022/6/23
 */
@Order(1)
@Component
public class InitMqConsumer {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private MlzjMqProperties mlzjMqProperties;

    /**
     * 设置主题和名称的方法
     */
    public final String setTopicOrQueueName = "setTopicOrQueueName";

    /**
     * 设置模式的方法
     */
    public final String setMode = "setMode";

    /**
     * 设置连接基本参数
     */
    public final String setMlzjMqProperties = "setMlzjMqProperties";

    /**
     * 初始化监听连接方法
     */
    public final String initMqChannel = "initMqChannel";

    @PostConstruct
    void initMqConsumer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MlzjMqConsumer.class);
        for (Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            MlzjMqConsumer annotation = aClass.getAnnotation(MlzjMqConsumer.class);
            //设置主题或队列名称
            Method setTopicOrQueueNameMethod = aClass.getMethod(setTopicOrQueueName, String.class);
            setTopicOrQueueNameMethod.invoke(entry.getValue(), annotation.queueOrTopicName());
            //设置监听模式
            Method setModeMethod = aClass.getMethod(setMode, MessageModeEnum.class);
            setModeMethod.invoke(entry.getValue(), annotation.mode());
            //设置连接属性
            Method setMlzjMqPropertiesMethod = aClass.getMethod(setMlzjMqProperties, MlzjMqProperties.class);
            setMlzjMqPropertiesMethod.invoke(entry.getValue(), mlzjMqProperties);
            //启动连接
            Method initMqChannelMethod = aClass.getMethod(initMqChannel);
            initMqChannelMethod.invoke(entry.getValue());
        }
    }

}
