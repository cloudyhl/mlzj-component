package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yhl
 * @date 2020/5/14
 */
public class ClientHandler extends SimpleChannelInboundHandler {


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        CtxUtils.setCtx(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User user = new User("12", 2,User.class);
        MlzjMessage<User> userMlzjMessage =new MlzjMessage<>();
        userMlzjMessage.setMessageId("12321");
        userMlzjMessage.setData(user);
        userMlzjMessage.setQueue("qqq");
        userMlzjMessage.setTopic("topic");
        for (int index = 0                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ; index < 10; index++) {
            ctx.writeAndFlush(userMlzjMessage);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        String s = (String) o;
        System.out.println(s);
    }




}
