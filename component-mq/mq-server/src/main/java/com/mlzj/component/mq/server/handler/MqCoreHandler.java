package com.mlzj.component.mq.server.handler;

import com.mlzj.component.mq.common.constants.MessageModeEnum;
import com.mlzj.component.mq.server.domain.ChannelMark;
import com.mlzj.component.mq.server.utils.QueueAndTopicCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author yhl
 * @date 2020/10/29
 */
@Slf4j
@Component
public class MqCoreHandler {


    /**
     * 处理自定义事件 超时事件
     * @param ctx 通道上下文
     * @param evt 事件本身
     */
    public void overTimeProcess(ChannelHandlerContext ctx, Object evt){
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    log.info("读事件超时");
                    break;
                case WRITER_IDLE:
                    log.info("写事件超时");
                    break;
                case ALL_IDLE:
                    log.info("读写时间总时间超时");
                    break;
                default:
                    log.info("未知的过期事件");
            }
            ChannelMark channelMark = QueueAndTopicCache.getChannelMarkMap().get(ctx.channel().id().asLongText());
            if (MessageModeEnum.TOPIC.getMode().equals(channelMark.getMode())) {
                Map<String, List<ChannelHandlerContext>> topicMap = QueueAndTopicCache.getApplicationTopicMap().get(channelMark.getApplicationName());
                for (Entry<String, List<ChannelHandlerContext>> topicEntry : topicMap.entrySet()) {
                    List<ChannelHandlerContext> value = topicEntry.getValue();
                    Iterator<ChannelHandlerContext> iterator = value.iterator();
                    while (iterator.hasNext()) {
                        if (StringUtils.equals(ctx.channel().id().asLongText(), iterator.next().channel().id().asLongText())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            if (MessageModeEnum.QUEUE.getMode().equals(channelMark.getMode())) {
                Map<String, List<ChannelHandlerContext>> queueMap = QueueAndTopicCache.getApplicationQueueMap().get(channelMark.getApplicationName());
                for (Entry<String, List<ChannelHandlerContext>> queueEntry : queueMap.entrySet()) {
                    List<ChannelHandlerContext> value = queueEntry.getValue();
                    Iterator<ChannelHandlerContext> iterator = value.iterator();
                    while (iterator.hasNext()) {
                        if (StringUtils.equals(ctx.channel().id().asLongText(), iterator.next().channel().id().asLongText())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            ctx.channel().close();
        }
    }
}
