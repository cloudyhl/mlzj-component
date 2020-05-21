package com.mlzj.component.mq.server.demo;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author yhl
 * @date 2020/5/17
 */
public class CtxUtils {

    private static ChannelHandlerContext channelHandlerContext;

    public static void setCtx(ChannelHandlerContext ctx){
        channelHandlerContext = ctx;
    }

    public static ChannelHandlerContext getCtx(){
        return channelHandlerContext;
    }
}
