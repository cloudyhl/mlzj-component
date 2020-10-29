package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.coder.ProtostuffMessageDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffMessageEncoder;
import com.mlzj.component.mq.server.channelhandler.LoginHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty的server
 * @author yhl
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(3,3,10));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new ProtostuffMessageDecoder());
                            ch.pipeline().addLast(new ProtostuffMessageEncoder());
                            ch.pipeline().addLast("loginHandler", new LoginHandler());
                            ch.pipeline().addLast(new HeartbeatHandler());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 10)
                    .option(ChannelOption.SO_REUSEADDR,true);
            ChannelFuture sync = serverBootstrap.bind(22230).sync();
            sync.channel().closeFuture().sync();
        }finally {
         bossGroup.shutdownGracefully();
         workGroup.shutdownGracefully();
        }

    }

}
