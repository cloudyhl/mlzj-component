package com.mlzj.component.mq.server.demo.rpc;

import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class CacheUtils {

    public static final Map<String, MethodCall> METHOD_MAP = new ConcurrentHashMap<>(32);

    public static final Map<String, ChannelPromise> PROMISE_MAP = new ConcurrentHashMap<>(32);

    private CacheUtils(){}
}
