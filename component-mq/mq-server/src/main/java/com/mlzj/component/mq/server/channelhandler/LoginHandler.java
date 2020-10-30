package com.mlzj.component.mq.server.channelhandler;

import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.constants.CommonConstants;
import com.mlzj.component.mq.server.factory.MessageProcessorFactory;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author yhl
 * @date 2020/5/22
 */
@Slf4j
@AllArgsConstructor
public class LoginHandler extends SimpleChannelInboundHandler<MlzjMessage> {

    private MessageProcessorFactory processorFactory;

    public LoginHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage msg) throws Exception {
        if (Objects.equals(msg.getType(), MessageTypeEnum.LOGIN.getCode())){
            log.info("channel注册成功");
            MessageProcessor messageProcess = processorFactory.getMessageProcess(msg.getMode());
            messageProcess.register(msg, ctx);
            ctx.fireChannelRead(msg);
            ctx.channel().pipeline().remove(CommonConstants.LOGIN_HANDLER_NAME);
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
