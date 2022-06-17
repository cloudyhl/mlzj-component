package com.mlzj.component.mq.server.processor.impl;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yhl
 * @date 2020/10/30
 */
public class QueueProcessorImpl implements MessageProcessor {

    private static final QueueProcessorImpl queueProcessor = new QueueProcessorImpl();

    public static QueueProcessorImpl getInstance(){
        return queueProcessor;
    }


    @Override
    public void process(MlzjMessage message) {
        ChannelHandlerContext channelHandlerContext = QueueAndTopicCache.getQueueMap().get(message.getQueue());
        channelHandlerContext.writeAndFlush(message);
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        QueueAndTopicCache.getQueueMap().put(mlzjMessage.getQueue(), ctx);
    }
}
