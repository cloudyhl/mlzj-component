package com.mlzj.component.mq.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
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
            ctx.channel().close();
        }
    }
}
