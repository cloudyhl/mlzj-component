package com.mlzj.component.mq.server.demo.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest) msg;
        System.out.println(request.getMethodName());
        RpcResponse rpcResponse  = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        rpcResponse.setResponse("成功收到"+ request.getRequestId()+ "消息");
        ctx.writeAndFlush(rpcResponse);
    }

}
