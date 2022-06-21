package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yhl
 * @date 2020/10/30
 */
public class MqClientTest extends SimpleChannelInboundHandler<MlzjMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        System.out.println(msg.getData());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MlzjMessage mlzjMessage = new MlzjMessage();
        mlzjMessage.setType(MessageTypeEnum.LOGIN.getCode());
        mlzjMessage.setMode(MessageModeEnum.QUEUE.getMode());
        mlzjMessage.setQueue("queue");
        ctx.writeAndFlush(mlzjMessage);
    }



}
