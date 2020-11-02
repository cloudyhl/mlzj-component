package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yhl
 * @date 2020/5/14
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    Logger logger = LoggerFactory.getLogger(ClientHandler.class);


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        CtxUtils.setCtx(ctx);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MlzjMessage mlzjMessage = new MlzjMessage();
        mlzjMessage.setType(MessageTypeEnum.LOGIN.getCode());
        mlzjMessage.setMode(MessageModeEnum.SEND.getMode());
        ctx.writeAndFlush(mlzjMessage);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println(o);
    }


    @Override
    @SuppressWarnings("deprecation")
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("发生异常", cause);
    }

}
