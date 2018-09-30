package com.skyguard.zmq.netty.encoder;

import com.skyguard.zmq.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class KryoEncoder extends MessageToByteEncoder<Object>{


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
            byte[] bytes = KryoUtil.serialize(o);
            ByteBuf byteBuf1 = Unpooled.copiedBuffer(bytes);
            ByteBuf byteBuf2 = Unpooled.copiedBuffer("/t".getBytes());
            ByteBuf req = Unpooled.copiedBuffer(byteBuf1, byteBuf2);
            channelHandlerContext.write(req);
    }
}
