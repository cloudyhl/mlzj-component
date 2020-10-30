package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.coder.ProtostuffMessageDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffMessageEncoder;
import com.mlzj.component.mq.common.handler.HeartBeatClientHandler;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author yhl
 * @date 2020/5/14
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {

            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new ProtostuffMessageDecoder());
                            ch.pipeline().addLast(new ProtostuffMessageEncoder());
                            ch.pipeline().addLast(new HeartBeatClientHandler());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 10)
                    .option(ChannelOption.SO_REUSEADDR,true);
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress("127.0.0.1", 22300)).sync().addListener((GenericFutureListener<ChannelFuture>) future -> {
                if (future.isSuccess()) {
                    User user = new User("12", 2,User.class);
                    MlzjMessage<User> userMlzjMessage =new MlzjMessage<>();
                    userMlzjMessage.setMessageId("12321");
                    userMlzjMessage.setData(user);
                    userMlzjMessage.setQueue("queue");
                    userMlzjMessage.setType(MessageTypeEnum.MESSAGE.getCode());
//                    for (int index = 0                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ; index < 10; index++) {
//                        future.channel().writeAndFlush(userMlzjMessage);
//                    }
                }
            });
            MlzjMessage<String> mlzjMessage = new MlzjMessage<>();
            mlzjMessage.setQueue("queue");
            mlzjMessage.setMode(MessageModeEnum.QUEUE.getMode());
            mlzjMessage.setData("hello world");
            mlzjMessage.setType(MessageTypeEnum.MESSAGE.getCode());
            CtxUtils.getCtx().writeAndFlush(mlzjMessage);

            TimeUnit.SECONDS.sleep(5);
            User user = new User("13", 2,User.class);
            MlzjMessage<User> userMlzjMessage =new MlzjMessage<>();
            userMlzjMessage.setMessageId("12www321");
            userMlzjMessage.setData(user);
            userMlzjMessage.setQueue("qqq");
            userMlzjMessage.setTopic("topic");
//            CtxUtils.getCtx().writeAndFlush(userMlzjMessage).addListener(future -> System.out.println(future.isSuccess()));
            TimeUnit.SECONDS.sleep(4);
            sync.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }

    }
}
