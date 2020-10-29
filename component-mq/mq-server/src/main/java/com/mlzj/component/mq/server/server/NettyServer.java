package com.mlzj.component.mq.server.server;

import com.mlzj.component.mq.common.coder.ProtostuffMessageDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffMessageEncoder;
import com.mlzj.component.mq.server.config.ServerProperties;
import com.mlzj.component.mq.server.constants.CommonConstants;
import com.mlzj.component.mq.server.demo.HeartbeatHandler;
import com.mlzj.component.mq.server.demo.ServerHandler;
import com.mlzj.component.mq.server.channelhandler.LoginHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * netty的server
 *
 * @author yhl
 */
@Component
@Slf4j
public class NettyServer {

    @Resource
    private ServerProperties serverProperties;

    /**
     * 开启线程启动mq服务
     */
    public NettyServer(){
        new Thread(()->{
            try {
                this.init();
            } catch (Exception e) {
                log.error("mq服务启动异常", e);
            }
        }).start();
    }

    /**
     * 启动mq服务
     * @throws InterruptedException 中断异常
     */
    private void init() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler
                                    (serverProperties.getReaderIdleTimeSeconds(),
                                    serverProperties.getWriterIdleTimeSeconds(),
                                    serverProperties.getAllIdleTimeSeconds()));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new ProtostuffMessageDecoder());
                            ch.pipeline().addLast(new ProtostuffMessageEncoder());
                            ch.pipeline().addLast(CommonConstants.LOGIN_HANDLER_NAME, new LoginHandler());
                            ch.pipeline().addLast(new HeartbeatHandler());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 10)
                    .option(ChannelOption.SO_REUSEADDR, true);
            ChannelFuture sync = serverBootstrap.bind(serverProperties.getServerPort()).sync();
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
