package com.mlzj.component.mq.server.utils;

import com.mlzj.component.mq.server.domain.ChannelMark;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import java.util.List;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yhl
 * @date 2020/10/30
 */
@Component
public class QueueAndTopicCache {

    private QueueAndTopicCache(){}




    /**
     * 广播模式添加通道组
     */
    private static final Map<String, ChannelGroup> topicRadioMap = new ConcurrentHashMap<>(32);



    private static final Map<String, Map<String, List<ChannelHandlerContext>>> applicationQueueMap = new ConcurrentHashMap<>(16);

    private static final Map<String, Map<String, List<ChannelHandlerContext>>> applicationTopicMap = new ConcurrentHashMap<>(16);

    private static final Map<String, ChannelMark> channelMarkMap = new ConcurrentHashMap<>(1024);



    public static Map<String, Map<String, List<ChannelHandlerContext>>> getApplicationQueueMap(){
        return applicationQueueMap;
    }


    public static Map<String, Map<String, List<ChannelHandlerContext>>> getApplicationTopicMap(){
        return applicationTopicMap;
    }

    public static Map<String, ChannelMark> getChannelMarkMap(){
        return channelMarkMap;
    }
}
