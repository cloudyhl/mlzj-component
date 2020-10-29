package com.mlzj.component.mq.server.channelhandler;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.constants.CommonConstants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author yhl
 * @date 2020/5/22
 */
@Slf4j
public class LoginHandler extends SimpleChannelInboundHandler<MlzjMessage<User>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage<User> msg) throws Exception {
        if (Objects.equals(msg.getType(), MessageTypeEnum.LOGIN.getCode())){
            log.info("通道注册成功");
            ctx.fireChannelRead(msg);
            ctx.channel().pipeline().remove(CommonConstants.LOGIN_HANDLER_NAME);
        }

    }
}
