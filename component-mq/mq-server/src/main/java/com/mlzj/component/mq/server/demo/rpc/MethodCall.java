package com.mlzj.component.mq.server.demo.rpc;

import com.mlzj.component.mq.server.demo.CtxUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class MethodCall {

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition condition = null;

    private RpcResponse response;

    public RpcResponse pushMessage(RpcRequest request) throws InterruptedException {
        reentrantLock.lock();
        condition = reentrantLock.newCondition();
        CtxUtils.getCtx().writeAndFlush(request);
        condition.await();
        return response;
    }

    public void getResult(RpcResponse rpcResponse){
        reentrantLock.lock();
        condition.signal();
        this.response = rpcResponse;
        reentrantLock.unlock();
    }

    public ChannelPromise pushMethodUsePromise(RpcRequest request){
        ChannelHandlerContext ctx = CtxUtils.getCtx();
        ChannelPromise channelPromise = ctx.newPromise();
        CacheUtils.PROMISE_MAP.put(request.getRequestId(), channelPromise);
        ctx.writeAndFlush(request);
        System.out.println("xxxx");
        return channelPromise;
    }
}
