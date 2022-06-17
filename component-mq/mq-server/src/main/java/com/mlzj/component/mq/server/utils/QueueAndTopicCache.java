package com.mlzj.component.mq.server.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
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
    private static final Map<String, ChannelGroup> topicMap = new ConcurrentHashMap<>(32);


    /**
     * 用于保存直连队列的通道缓存
     */
    private static final Map<String, ChannelHandlerContext> queueMap = new ConcurrentHashMap<>(128);


    /**
     * 获取主题类型缓存
     * @return 主题类型的缓存
     */
    public static Map<String, ChannelGroup> getTopicMap(){
        return topicMap;
    }

    /**
     * 获取直连队列类型的缓存
     * @return 直连队列类型的缓存
     */
    public static Map<String, ChannelHandlerContext> getQueueMap(){
        return queueMap;
    }
}
