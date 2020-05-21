package com.mlzj.component.mq.common.handler;

import com.mlzj.component.mq.common.protocol.MlzjMessage;
import com.mlzj.component.mq.common.utils.ProtostuffUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author yhl
 * @date 2020/5/19
 */
public class ProtostuffMessageEncoder extends MessageToMessageEncoder<MlzjMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MlzjMessage msg, List<Object> out) throws Exception {
        out.add(Unpooled.copiedBuffer(ProtostuffUtils.serialize(msg)));
    }
}
