package com.mlzj.component.mq.server.demo.rpc;

import com.mlzj.component.mq.server.demo.CtxUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        //CacheUtils.METHOD_MAP.get(msg.getRequestId()).getResult(msg);
        CacheUtils.PROMISE_MAP.get(msg.getRequestId()).setSuccess();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        CtxUtils.setCtx(ctx);
    }
}
