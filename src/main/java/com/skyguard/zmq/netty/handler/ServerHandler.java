package com.skyguard.zmq.netty.handler;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.netty.task.TaskExecutor;
import com.skyguard.zmq.netty.task.ZmqServerFuture;
import com.skyguard.zmq.netty.task.ZmqServerTask;
import com.skyguard.zmq.processor.ClientMessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerHandler extends ChannelInboundHandlerAdapter{

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static int PROCESSORS = 5;
    private ExecutorService executorService = new ThreadPoolExecutor(PROCESSORS,PROCESSORS*2,20,TimeUnit.SECONDS,new LinkedBlockingDeque<>(PROCESSORS*2));

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
            throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            // only log
            LOG.error("exception not IOException", e);
        }
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        try{
        // TODO Auto-generated method stub
        RequestEntity requestEntity = (RequestEntity) msg;


        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ClientMessageProcessor.registerClient(requestEntity.getTopic(),inetSocketAddress.getHostName(),inetSocketAddress.getPort());
        TaskExecutor taskExecutor = new TaskExecutor();
        ListeningExecutorService service = taskExecutor.getService();
        ListenableFuture<ResponseEntity> future = service.submit(new ZmqServerTask(requestEntity));


            Futures.addCallback(future, new ZmqServerFuture(ctx), executorService);
        }catch(Exception e){
            LOG.error("get data error",e);
        }

    }


}
