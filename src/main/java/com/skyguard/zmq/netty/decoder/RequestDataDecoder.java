package com.skyguard.zmq.netty.decoder;

import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RequestDataDecoder extends ByteToMessageDecoder{


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        RequestEntity requestEntity = (RequestEntity) KryoUtil.deserialize(bytes);
        list.add(requestEntity);
    }
}
