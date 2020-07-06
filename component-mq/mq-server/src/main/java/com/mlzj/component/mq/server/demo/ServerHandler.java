package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端handler
 * @author yhl
 * @date 2020/5/14
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    EventExecutor eventExecutor = new DefaultEventExecutor();

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MlzjMessage<User> user = (MlzjMessage<User>)msg;
        System.out.println(user);
        DefaultChannelPromise defaultChannelPromise = new DefaultChannelPromise(ctx.channel(),eventExecutor);
        defaultChannelPromise.addListener(new AfterListener(user));
        MlzjMessage mlzjMessage = new MlzjMessage();
        mlzjMessage.setQueue("2222222");
        mlzjMessage.setType("2");
        ctx.writeAndFlush(mlzjMessage,defaultChannelPromise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE: {
                    System.out.println("read time out");
                    break;
                }
                case WRITER_IDLE: {
                    System.out.println("write time out");
                    break;
                }
                case ALL_IDLE: {
                    System.out.println("all time out");
                    break;
                }
                default: {
                    System.out.println("");
                }
            }
            ctx.channel().close();
        }
    }
    @Override
    @SuppressWarnings("deprecation")
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("发生异常", cause);
    }
}
