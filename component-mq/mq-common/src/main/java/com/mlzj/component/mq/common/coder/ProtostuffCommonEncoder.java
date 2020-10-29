package com.mlzj.component.mq.common.coder;

import com.mlzj.component.mq.common.utils.ProtostuffUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author yhl
 * @date 2020/5/21
 */
public class ProtostuffCommonEncoder  extends MessageToMessageEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        out.add(Unpooled.copiedBuffer(ProtostuffUtils.serialize(msg)));
    }
}
