package com.skyguard.zmq.netty.client;

import com.skyguard.zmq.netty.decoder.KryoDecoder;
import com.skyguard.zmq.netty.encoder.KryoEncoder;
import com.skyguard.zmq.netty.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class CommonTcpClient implements ZmqClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final int PROCESSORS  = 5;

    private static EventLoopGroup workerGroup = new NioEventLoopGroup(PROCESSORS);

    private Bootstrap bootstrap = new Bootstrap();


    @Override
    public void startClient(int connectTimeout) throws Exception{
        // TODO Auto-generated method stub
        LOG.info("----------------客户端开始启动-------------------------------");
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                ByteBuf byteBuf = Unpooled.copiedBuffer("/t".getBytes());
                pipeline.addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                pipeline.addLast(new KryoDecoder());
                pipeline.addLast(new KryoEncoder());
                pipeline.addLast(new ClientHandler());

            }

        });
        LOG.info("----------------客户端启动结束-------------------------------");
    }

    @Override
    public ZmqCommonClient createClient(String ip, int port) throws Exception{
       // TODO Auto-generated method stub

        String key=ip+":"+port;
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
        future.awaitUninterruptibly();
        if (!future.isDone()) {
            LOG.error("Create connection to " + ip + ":" + port + " timeout!");
            throw new Exception("Create connection to " + ip + ":" + port + " timeout!");
        }
        if (future.isCancelled()) {
            LOG.error("Create connection to " + ip + ":" + port + " cancelled by user!");
            throw new Exception("Create connection to " + ip + ":" + port + " cancelled by user!");
        }
        if (!future.isSuccess()) {
            LOG.error("Create connection to " + ip + ":" + port + " error", future.cause());
            throw new Exception("Create connection to " + ip + ":" + port + " error", future.cause());
        }

        ZmqCommonClient zmqCommonClient = new ZmqCommonClient(future);
        ZmqClientFactory.putClient(key,zmqCommonClient);

        return zmqCommonClient;
    }

    @Override
    public void stopClient() {
       workerGroup.shutdownGracefully();
       ZmqClientFactory.clear();
    }
}
