package com.mlzj.component.mq.server.processor.impl;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yhl
 * @date 2020/10/30
 */
public class QueueProcessorImpl implements MessageProcessor {

    private static final QueueProcessorImpl queueProcessor = new QueueProcessorImpl();

    public static QueueProcessorImpl getInstance(){
        return queueProcessor;
    }

    private final Random random = new Random();

    @Override
    public void process(MlzjMessage message) {
        List<ChannelHandlerContext> channelHandlerContexts = QueueAndTopicCache.getQueueMap().get(message.getQueue());
        channelHandlerContexts.get(random.nextInt(channelHandlerContexts.size())).writeAndFlush(message);
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
        channelHandlerContexts.add(ctx);
        QueueAndTopicCache.getQueueMap().put(mlzjMessage.getQueue(), channelHandlerContexts);
    }
}
