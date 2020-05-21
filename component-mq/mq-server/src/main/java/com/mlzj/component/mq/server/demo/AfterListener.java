package com.mlzj.component.mq.server.demo;

import com.mlzj.component.mq.common.User;
import com.mlzj.component.mq.common.protocol.MlzjMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * @author yhl
 * @date 2020/5/19
 */
public class AfterListener implements GenericFutureListener<ChannelFuture> {

    private MlzjMessage<User> user;

    public AfterListener(MlzjMessage<User> user){
        this.user = user;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        ChannelPromise channelPromise = (DefaultChannelPromise) future;
        System.out.println(channelPromise.setUncancellable());
        System.out.println(channelPromise.isSuccess());
        System.out.println(user);
        System.out.println(future.channel().id().asLongText());
        channelPromise.channel().writeAndFlush(Unpooled.copiedBuffer("hahaha".getBytes()));
    }
}
