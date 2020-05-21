package com.mlzj.component.mq.server.demo;

import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.EventExecutor;

/**
 * @author yhl
 * @date 2020/5/20
 */
public class AfterChannelPromise extends DefaultChannelPromise {
    public AfterChannelPromise(Channel channel) {
        super(channel);
    }

    public AfterChannelPromise(Channel channel, EventExecutor executor) {
        super(channel, executor);
    }

}
