package com.mlzj.component.mq.server.channelhandler;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.factory.MessageProcessorFactory;
import com.mlzj.component.mq.server.handler.MqCoreHandler;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import com.mlzj.component.mq.server.task.MessageDistributeTask;
import com.mlzj.component.mq.server.utils.TaskPoolExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * mq核心逻辑处理器
 *
 * @author yhl
 * @date 2020/10/29
 */
public class MqServerCoreHandler extends ChannelInboundHandlerAdapter {

    /**
     * mq核心辅助类
     */
    private MqCoreHandler mqCoreHandler;

    private MessageProcessorFactory messageProcessorFactory;

    public MqServerCoreHandler() {
    }

    public MqServerCoreHandler(MqCoreHandler mqCoreHandler, MessageProcessorFactory messageProcessorFactory) {
        this.mqCoreHandler = mqCoreHandler;
        this.messageProcessorFactory = messageProcessorFactory;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MlzjMessage mlzjMessage = (MlzjMessage) msg;
        MessageProcessor messageProcess = messageProcessorFactory.getMessageProcess(mlzjMessage.getMode());
        TaskPoolExecutor.gerThreadPool().execute(new MessageDistributeTask(mlzjMessage, messageProcess));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        mqCoreHandler.overTimeProcess(ctx, evt);
    }
}
