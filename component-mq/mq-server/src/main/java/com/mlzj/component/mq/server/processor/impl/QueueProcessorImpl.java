package com.mlzj.component.mq.server.processor.impl;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.domain.ChannelMark;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.util.CollectionUtils;

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
        Map<String, List<ChannelHandlerContext>> queueMap = QueueAndTopicCache.getApplicationQueueMap().get(message.getApplicationName());
        List<ChannelHandlerContext> channelHandlerContexts = queueMap.get(message.getQueue());
        channelHandlerContexts.get(random.nextInt(channelHandlerContexts.size())).writeAndFlush(message);
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        Map<String, ChannelMark> channelMarkMap = QueueAndTopicCache.getChannelMarkMap();
        ChannelMark channelMark = new ChannelMark();
        channelMark.setApplicationName(mlzjMessage.getApplicationName());
        channelMark.setMode(mlzjMessage.getMode());
        channelMarkMap.put(ctx.channel().id().asLongText(), channelMark);
        Map<String, Map<String, List<ChannelHandlerContext>>> applicationQueueMap = QueueAndTopicCache.getApplicationQueueMap();
        if (applicationQueueMap.containsKey(mlzjMessage.getApplicationName())) {
            Map<String, List<ChannelHandlerContext>> applicationMap = applicationQueueMap.get(mlzjMessage.getApplicationName());
            if (CollectionUtils.isEmpty(applicationMap.get(mlzjMessage.getQueue()))) {
                List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
                channelHandlerContexts.add(ctx);
                applicationMap.put(mlzjMessage.getQueue(), channelHandlerContexts);
            } else {
                List<ChannelHandlerContext> channelHandlerContexts = applicationMap.get(mlzjMessage.getQueue());
                channelHandlerContexts.add(ctx);
            }
            List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
            channelHandlerContexts.add(ctx);
            applicationMap.put(mlzjMessage.getQueue(), channelHandlerContexts);
        } else {
            Map<String, List<ChannelHandlerContext>> map = new HashMap<>(128);
            List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
            channelHandlerContexts.add(ctx);
            map.put(mlzjMessage.getQueue(), channelHandlerContexts);
            applicationQueueMap.put(mlzjMessage.getApplicationName(), map);
        }

    }
}
