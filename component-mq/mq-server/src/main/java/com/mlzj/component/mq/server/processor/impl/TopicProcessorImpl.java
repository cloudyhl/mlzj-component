package com.mlzj.component.mq.server.processor.impl;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author yhl
 * @date 2020/10/30
 */
public class TopicProcessorImpl implements MessageProcessor {

    private static TopicProcessorImpl topicProcessor = new TopicProcessorImpl();

    public static TopicProcessorImpl getInstance() {
        return topicProcessor;
    }

    @Override
    public void process(MlzjMessage message) {
        ChannelGroup channels = QueueAndTopicCache.getTopicMap().get(message.getTopic());
        channels.writeAndFlush(message);
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        if (QueueAndTopicCache.getTopicMap().containsKey(mlzjMessage.getTopic())) {
            QueueAndTopicCache.getTopicMap().get(mlzjMessage.getTopic()).add(ctx.channel());
        } else {
            QueueAndTopicCache.getTopicMap().put(mlzjMessage.getTopic(), new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        }
    }
}
