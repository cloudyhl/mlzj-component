package com.mlzj.component.mq.client.consumer;

import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import com.mlzj.component.mq.common.coder.ProtostuffMessageDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffMessageEncoder;
import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.handler.HeartBeatClientHandler;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.net.InetSocketAddress;

/**
 * @author yhl
 * @date 2022/6/20
 */
public abstract class AbstractMqConsumer {

    private MlzjMqProperties mlzjMqProperties;

    private String topicOrQueueName;

    private MessageModeEnum mode;

    abstract public void onMessage(MlzjMessage message);

    public void setMode(MessageModeEnum mode) {
        this.mode = mode;
    }

    public void setTopicOrQueueName(String topicOrQueueName) {
        this.topicOrQueueName = topicOrQueueName;
    }

    public void setMlzjMqProperties(MlzjMqProperties mlzjMqProperties) {
        this.mlzjMqProperties = mlzjMqProperties;
    }

    public void initMqChannel() {
        new Thread(()->{
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
                                ch.pipeline().addLast(new OnMessageHandler());
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 10)
                        .option(ChannelOption.SO_REUSEADDR,true);
                ChannelFuture sync = bootstrap.connect(new InetSocketAddress(mlzjMqProperties.getServerUrl(), mlzjMqProperties.getPort())).sync();
                Channel channel = sync.channel();
                MlzjMessage mlzjMessage = new MlzjMessage();
                mlzjMessage.setType(MessageTypeEnum.LOGIN.getCode());
                mlzjMessage.setMode(mode.getMode());
                mlzjMessage.setApplicationName(mlzjMqProperties.getApplicationName());
                if (MessageModeEnum.TOPIC.getMode().equals(mode.getMode())) {
                    mlzjMessage.setTopic(topicOrQueueName);
                } else {
                    mlzjMessage.setQueue(topicOrQueueName);
                }
                channel.writeAndFlush(mlzjMessage);
                MlzjMessage heartBeatLogin = new MlzjMessage();
                heartBeatLogin.setType(MessageTypeEnum.HEART_LOGIN.getCode());
                channel.writeAndFlush(heartBeatLogin);
                sync.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workGroup.shutdownGracefully();
                initMqChannel();
            }
        }).start();

    }
    class OnMessageHandler extends SimpleChannelInboundHandler<MlzjMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, MlzjMessage o) throws Exception {
            onMessage(o);
        }
    }
}
