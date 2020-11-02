package com.mlzj.component.mq.common.handler;

import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.ActuatorUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * @author yhl
 * @date 2020/5/22
 */
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<MlzjMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        ActuatorUtils.isTrue(Objects.equals(msg.getType(), MessageTypeEnum.HEART.getCode()), () -> {
            MlzjMessage mlzjMessage = new MlzjMessage();
            mlzjMessage.setType(MessageTypeEnum.HEART.getCode());
            ctx.writeAndFlush(mlzjMessage);
        });
        ActuatorUtils.isFalse(Objects.equals(msg.getType(), MessageTypeEnum.HEART.getCode()), ()-> ctx.fireChannelRead(msg));
    }
}
