package com.mlzj.component.mq.server.channelhandler;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.demo.AfterListener;
import com.mlzj.component.mq.server.handler.MqCoreHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultChannelPromise;

/**
 * mq核心逻辑处理器
 * @author yhl
 * @date 2020/10/29
 */
public class MqServerCoreHandler extends ChannelInboundHandlerAdapter {

    private MqCoreHandler mqCoreHandler;

    public MqServerCoreHandler(){}

    public MqServerCoreHandler(MqCoreHandler mqCoreHandler){
        this.mqCoreHandler = mqCoreHandler;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        mqCoreHandler.overTimeProcess(ctx, evt);
    }
}
