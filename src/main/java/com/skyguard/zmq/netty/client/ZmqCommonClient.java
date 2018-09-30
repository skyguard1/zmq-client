package com.skyguard.zmq.netty.client;


import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ZmqCommonClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ChannelFuture cf;

    public ZmqCommonClient(ChannelFuture cf) {
        this.cf = cf;
    }

    public String getServerIP() {
        // TODO Auto-generated method stub
        return ((InetSocketAddress) cf.channel().remoteAddress()).getHostName();
    }

    public int getServerPort() {
        // TODO Auto-generated method stub
        return ((InetSocketAddress) cf.channel().remoteAddress()).getPort();
    }

    public void sendRequest(RequestEntity requestEntity) throws Exception {
        // TODO Auto-generated method stub
        if(cf.channel().isOpen()){
            InetSocketAddress inetSocketAddress = (InetSocketAddress) cf.channel().localAddress();
            String ip = inetSocketAddress.getHostName();
            requestEntity.setIp(ip);
            ChannelFuture writeFuture = cf.channel().writeAndFlush(requestEntity);
            // use listener to avoid wait for write & thread context switch
            writeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        return;
                    }
                    String errorMsg = "";
                    // write timeout

                    if (future.isCancelled()) {
                        errorMsg = "Send request to " + cf.channel().toString()
                                + " cancelled by user"
                                ;
                    }else if (!future.isSuccess()) {
                        if (cf.channel().isOpen()) {
                            // maybe some exception,so close the channel
                            cf.channel().close();
                        }
                        errorMsg = "Send request to " + cf.channel().toString() + " error" + future.cause();
                    }
                    LOG.error(errorMsg);
                }
            });
        }

    }


}
