package com.mlzj.component.mq.server.utils;

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
     * 用于保存主题类型的通道缓存
     */
    private static final Map<String, List<ChannelHandlerContext>> topicMap = new ConcurrentHashMap<>(32);


    /**
     * 广播模式添加通道组
     */
    private static final Map<String, ChannelGroup> topicRadioMap = new ConcurrentHashMap<>(32);


    /**
     * 用于保存直连队列的通道缓存
     */
    private static final Map<String, List<ChannelHandlerContext>> queueMap = new ConcurrentHashMap<>(128);


    /**
     * 获取主题类型缓存
     * @return 主题类型的缓存
     */
    public static Map<String, List<ChannelHandlerContext>> getTopicMap(){
        return topicMap;
    }

    /**
     * 获取直连队列类型的缓存
     * @return 直连队列类型的缓存
     */
    public static Map<String, List<ChannelHandlerContext>> getQueueMap(){
        return queueMap;
    }
}
