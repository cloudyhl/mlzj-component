package com.mlzj.component.mq.server.demo.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yhl
 * @date 2020/5/18
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(msg.uri());
        if (msg.method() == HttpMethod.GET){
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(msg.uri());
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            parameters.forEach((key, value) -> {
                System.out.println(key);
                System.out.println(value);
            });
        }
        if (msg.method() == HttpMethod.POST){
            HttpHeaders headers = msg.headers();
            String s = headers.get("Content-Type");
            System.out.println(s);
            HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(msg);
            if (msg.content().isReadable()){
                byte[] bytes = new byte[msg.content().readableBytes()];
                msg.content().readBytes(bytes);
                System.out.println(msg.content().toString(CharsetUtil.UTF_8));
                System.out.println(new String(bytes, CharsetUtil.UTF_8));

            }
            postRequestDecoder.offer(msg);
            List<InterfaceHttpData> bodyHttpDatas = postRequestDecoder.getBodyHttpDatas();
            bodyHttpDatas.forEach(interfaceHttpData -> {
                Attribute data = (Attribute) interfaceHttpData;
                System.out.println(data.getName());
                try {
                    System.out.println(data.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set("content-Type", "text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        ByteBuf responseBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
        response.content().writeBytes(responseBuf);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        responseBuf.release();
    }
}
