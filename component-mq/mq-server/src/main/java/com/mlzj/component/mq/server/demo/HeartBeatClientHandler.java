package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.ActuatorUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yhl
 * @date 2020/5/22
 */
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<MlzjMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        ActuatorUtils.isTrue(msg.getType().equals("3"), () -> {
            MlzjMessage mlzjMessage = new MlzjMessage();
            mlzjMessage.setType("3");
            ctx.writeAndFlush(mlzjMessage);
        });
        ActuatorUtils.isFalse(msg.getType().equals("3"), ()-> ctx.fireChannelRead(msg));
    }
}
