package com.mlzj.component.mq.server.task;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.server.processor.MessageProcessor;
import lombok.AllArgsConstructor;

/**
 * @author yhl
 * @date 2020/10/30
 */
@AllArgsConstructor
public class MessageDistributeTask implements Runnable {

    /**
     * 消息协议
     */
    private MlzjMessage message;

    /**
     * 消息处理器
     */
    private MessageProcessor messageProcess;


    public MessageDistributeTask() {
    }


    @Override
    public void run() {
        messageProcess.process(message);
    }
}
