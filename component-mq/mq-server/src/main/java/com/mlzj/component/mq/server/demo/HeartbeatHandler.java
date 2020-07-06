package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.ActuatorUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author yhl
 * @date 2020/5/22
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<MlzjMessage> {

    Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        if (msg.getType().equals("3")){
            System.out.println("get heartBeat");
            ActuatorUtils.isTrue(logger.isDebugEnabled(),()->logger.debug(ctx.channel().remoteAddress()+"-----------> HeartBeat"));
        } else if (msg.getType().equals("2")){
            ctx.fireChannelRead(msg);
        }
        if (msg.getType().equals("1")){
            System.out.println("heartBeat start");
            ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx),0,1, TimeUnit.SECONDS);
        }

    }

    @AllArgsConstructor
    class HeartBeatTask implements Runnable{

        private ChannelHandlerContext ctx;

        @Override
        public void run() {
            MlzjMessage mlzjMessage = new MlzjMessage();
            mlzjMessage.setType("3");
            ctx.writeAndFlush(mlzjMessage);
        }
    }

}
