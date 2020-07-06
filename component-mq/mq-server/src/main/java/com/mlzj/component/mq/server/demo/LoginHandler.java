package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yhl
 * @date 2020/5/22
 */
public class LoginHandler extends SimpleChannelInboundHandler<MlzjMessage<User>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MlzjMessage<User> msg) throws Exception {
        System.out.println("进入注册handler");
        String type = msg.getType();
        if (type.equals("1")){
            System.out.println("注册成功");
            ctx.fireChannelRead(msg);
            ctx.channel().pipeline().remove("loginHandler");
        }

    }
}
