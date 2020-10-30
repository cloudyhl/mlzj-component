package com.mlzj.component.mq.server.factory;

import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.processor.impl.QueueProcessorImpl;
import com.mlzj.component.mq.server.processor.impl.TopicProcessorImpl;

/**
 * mq消息处理器生成工厂
 *
 * @author yhl
 * @date 2020/10/30
 */
public class MessageProcessorFactory {


    /**
     * 获取消息处理工厂
     *
     * @param messageMode 消息模式
     * @return 消息处理器
     */
    public MessageProcessor getMessageProcess(Integer messageMode) {
        if (MessageModeEnum.TOPIC.getMode().equals(messageMode)) {
            return TopicProcessorImpl.getInstance();
        } else {
            return QueueProcessorImpl.getInstance();
        }
    }

}
