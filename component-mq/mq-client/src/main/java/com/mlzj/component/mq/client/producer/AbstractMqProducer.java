package com.mlzj.component.mq.client.producer;

import com.mlzj.component.mq.client.domain.MlzjMqProperties;
import com.mlzj.component.mq.common.coder.ProtostuffMessageDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffMessageEncoder;
import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.common.constants.MessageTypeEnum;
import com.mlzj.component.mq.common.handler.HeartBeatClientHandler;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.IdUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * mq消息发送器父类
 * @author yhl
 * @date 2020/11/2
 */
@Data
@Component
public abstract class AbstractMqProducer<T> {

    @Resource
    private MlzjMqProperties mlzjMqProperties;

    /**
     * 主题名称
     */
    private String topic;

    /**
     * 队列名称
     */
    private String queue;

    /**
     * 发送数据使用的通道
     */
    private Channel channel;

    /**
     * id生成器
     */
    private IdUtils idUtils = new IdUtils();

    public AbstractMqProducer() throws InterruptedException {
        initTopicOrQueue();
        initMqChannel();
    }


    /**
     * 初始化topic或者queue
     */
    abstract void initTopicOrQueue();

    /**
     * 获取到需要发送的数据
     * @return 发送的数据
     */
    abstract T getData();

    /**
     * 获取基本的模板消息
     * @return 模板消息
     */
    private MlzjMessage<T> getTemplateMessage(){
        MlzjMessage<T> mlzjMessage = new MlzjMessage<>();
        mlzjMessage.setMessageId(idUtils.nextLongId().toString());
        mlzjMessage.setType(MessageTypeEnum.MESSAGE.getCode());
        mlzjMessage.setTopic(topic);
        mlzjMessage.setQueue(queue);
        Integer mode = StringUtils.isNotBlank(topic) ? MessageModeEnum.TOPIC.getMode() : MessageModeEnum.QUEUE.getMode();
        mlzjMessage.setMode(mode);
        return mlzjMessage;
    }

    /**
     * 发送消息
     */
    public void pushMessage(){
        MlzjMessage<T> message = this.getTemplateMessage();
        T data = this.getData();
        message.setData(data);
        this.channel.writeAndFlush(message);
    }

    private void initMqChannel() throws InterruptedException {
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
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 10)
                    .option(ChannelOption.SO_REUSEADDR,true);
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(mlzjMqProperties.getServerUrl(), mlzjMqProperties.getPort())).sync();
            this.channel = sync.channel();
            sync.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

}
