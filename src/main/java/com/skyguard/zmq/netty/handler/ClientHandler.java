package com.skyguard.zmq.netty.handler;

import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.entity.ResponseStatus;
import com.skyguard.zmq.processor.ZmqRequestHandler;
import com.skyguard.zmq.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientHandler extends ChannelInboundHandlerAdapter{

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        try {

            ResponseEntity responseEntity = (ResponseEntity) msg;


            if (responseEntity.getResponseCode() != ResponseStatus.SUCCESS.getCode()) {
                LOG.error("send request failed error:" + responseEntity.getMessage());
            }

            if(responseEntity!=null&& StringUtils.isNotEmpty(responseEntity.getRequestId())) {
                ZmqRequestHandler.putData(responseEntity.getRequestId(), responseEntity);
            }
        }catch(Exception e){
            LOG.error("get data error",e);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info(CommonRpcTcpClientFactory.getInstance().containClient(ctx.channel().remoteAddress().toString()));
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
            throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            // only log
            LOG.error("exception not IOException", e);
        }

        if(ctx.channel().isOpen()){
            ctx.channel().close();
        }


    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOG.error("connection closed: " + ctx.channel().remoteAddress());
        if(ctx.channel().isOpen()){
            ctx.channel().close();
        }
    }




}
