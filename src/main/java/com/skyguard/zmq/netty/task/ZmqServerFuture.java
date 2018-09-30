package com.skyguard.zmq.netty.task;

import com.google.common.util.concurrent.FutureCallback;
import com.skyguard.zmq.entity.ResponseEntity;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ZmqServerFuture implements FutureCallback<ResponseEntity> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ChannelHandlerContext ctx;

    public ZmqServerFuture(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onSuccess(ResponseEntity responseEntity) {
        if(ctx.channel().isOpen()&&responseEntity!=null){
            ChannelFuture wf = ctx.channel().writeAndFlush(responseEntity);
            wf.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        LOG.error("server write response error,client  host is: " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostName()+":"+((InetSocketAddress) ctx.channel().remoteAddress()).getPort()+",server Ip:"+getLocalhost());
                        ctx.channel().close();
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        LOG.error("server handler fail!", throwable);
    }

    public String getLocalhost(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("无法获取本地Ip",e);
        }

    }

}
