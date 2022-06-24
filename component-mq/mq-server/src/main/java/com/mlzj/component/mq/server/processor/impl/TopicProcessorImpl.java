package com.mlzj.component.mq.server.processor.impl;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yhl
 * @date 2020/10/30
 */
public class TopicProcessorImpl implements MessageProcessor {

    private static final TopicProcessorImpl topicProcessor = new TopicProcessorImpl();

    public static TopicProcessorImpl getInstance() {
        return topicProcessor;
    }

    private final Random random = new Random();


    @Override
    public void process(MlzjMessage message) {
        List<ChannelHandlerContext> channelHandlerContexts = QueueAndTopicCache.getTopicMap().get(message.getTopic());
        channelHandlerContexts.get(random.nextInt(channelHandlerContexts.size())).writeAndFlush(message);
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        if (QueueAndTopicCache.getTopicMap().containsKey(mlzjMessage.getTopic())) {
            QueueAndTopicCache.getTopicMap().get(mlzjMessage.getTopic()).add(ctx);
        } else {
            List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
            channelHandlerContexts.add(ctx);
            QueueAndTopicCache.getTopicMap().put(mlzjMessage.getTopic(), channelHandlerContexts);
        }
    }
}
