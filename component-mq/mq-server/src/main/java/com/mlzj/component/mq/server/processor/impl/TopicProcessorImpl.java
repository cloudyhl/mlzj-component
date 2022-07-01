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
import java.util.Map.Entry;
import java.util.Random;
import org.springframework.util.CollectionUtils;

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
        Map<String, Map<String, List<ChannelHandlerContext>>> applicationTopicMap = QueueAndTopicCache.getApplicationTopicMap();
        for (Entry<String, Map<String, List<ChannelHandlerContext>>> topicMap : applicationTopicMap.entrySet()) {
            List<ChannelHandlerContext> channelHandlerContexts = topicMap.getValue().get(message.getTopic());
            channelHandlerContexts.get(random.nextInt(channelHandlerContexts.size())).writeAndFlush(message);
        }
    }

    @Override
    public void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx) {
        Map<String, ChannelMark> channelMarkMap = QueueAndTopicCache.getChannelMarkMap();
        ChannelMark channelMark = new ChannelMark();
        channelMark.setApplicationName(mlzjMessage.getApplicationName());
        channelMark.setMode(mlzjMessage.getMode());
        channelMarkMap.put(ctx.channel().id().asLongText(), channelMark);
        Map<String, Map<String, List<ChannelHandlerContext>>> applicationTopicMap = QueueAndTopicCache.getApplicationTopicMap();
        if (applicationTopicMap.containsKey(mlzjMessage.getApplicationName())) {
            Map<String, List<ChannelHandlerContext>> applicationMap = applicationTopicMap.get(mlzjMessage.getApplicationName());
            if (CollectionUtils.isEmpty(applicationMap.get(mlzjMessage.getTopic()))) {
                List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
                channelHandlerContexts.add(ctx);
                applicationMap.put(mlzjMessage.getTopic(), channelHandlerContexts);
            } else {
                List<ChannelHandlerContext> channelHandlerContexts = applicationMap.get(mlzjMessage.getTopic());
                channelHandlerContexts.add(ctx);
            }

        } else {
            Map<String, List<ChannelHandlerContext>> map = new HashMap<>(128);
            List<ChannelHandlerContext> channelHandlerContexts = new ArrayList<>();
            channelHandlerContexts.add(ctx);
            map.put(mlzjMessage.getTopic(), channelHandlerContexts);
            applicationTopicMap.put(mlzjMessage.getApplicationName(), map);
        }
    }
}
