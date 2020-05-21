package com.mlzj.component.mq.common.handler;

import com.mlzj.component.mq.common.utils.ProtostuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author yhl
 * @date 2020/5/21
 */
@AllArgsConstructor
public class ProtostuffCommonDecoder extends MessageToMessageDecoder<ByteBuf> {

    private Class<?> aClass;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        out.add(ProtostuffUtils.deserialize(bytes, aClass));
    }
}
