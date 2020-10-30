package com.mlzj.component.mq.server.processor;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yhl
 * @date 2020/10/30
 */
public interface MessageProcessor {

    /**
     * 处理消息
     * @param message 消息
     */
    void process(MlzjMessage message);


    /**
     * 注册通道
     * @param mlzjMessage mlzj消息
     * @param ctx 通道
     */
    void register(MlzjMessage mlzjMessage, ChannelHandlerContext ctx);

}
