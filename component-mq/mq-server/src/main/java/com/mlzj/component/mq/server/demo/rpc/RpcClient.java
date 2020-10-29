package com.mlzj.component.mq.server.demo.rpc;

import com.mlzj.component.mq.common.coder.ProtostuffCommonDecoder;
import com.mlzj.component.mq.common.coder.ProtostuffCommonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class RpcClient {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
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
                            ch.pipeline().addLast(new ProtostuffCommonDecoder(RpcResponse.class));
                            ch.pipeline().addLast(new ProtostuffCommonEncoder());
                            ch.pipeline().addLast(new RpcClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 10)
                    .option(ChannelOption.SO_REUSEADDR,true);
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress("127.0.0.1", 10001)).sync();
            MethodCall methodCall = new MethodCall();
            RpcRequest request = new RpcRequest();
            request.setRequestId("1");
            request.setMethodName("method");
            request.setParam("param");
//            CacheUtils.METHOD_MAP.put("1",methodCall);
//            RpcResponse rpcResponse = methodCall.pushMessage(request);
            ChannelPromise channelPromise = methodCall.pushMethodUsePromise(request);
            channelPromise.await();


            System.out.println(channelPromise.get());
            sync.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
        }

    }

}
