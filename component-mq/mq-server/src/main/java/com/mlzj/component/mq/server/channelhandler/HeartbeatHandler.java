package com.mlzj.component.mq.server.channelhandler;

import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.ActuatorUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yhl
 * @date 2020/5/22
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<MlzjMessage> {

    private final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        if (Objects.equals(msg.getType(), MessageTypeEnum.HEART.getCode())){
            ActuatorUtils.isTrue(logger.isDebugEnabled(),()->logger.debug("{}-----------> HeartBeat",ctx.channel().remoteAddress()));
        } else if (Objects.equals(msg.getType(), MessageTypeEnum.MESSAGE.getCode())){
            ctx.fireChannelRead(msg);
        }
        if (Objects.equals(msg.getType(), MessageTypeEnum.LOGIN.getCode())){
            ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx),0,3, TimeUnit.SECONDS);
        }

    }

    @AllArgsConstructor
    class HeartBeatTask implements Runnable{

        private ChannelHandlerContext ctx;

        @Override
        public void run() {
            MlzjMessage mlzjMessage = new MlzjMessage();
            mlzjMessage.setType(MessageTypeEnum.HEART.getCode());
            ctx.writeAndFlush(mlzjMessage);
        }
    }

}
