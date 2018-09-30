package com.skyguard.zmq.netty.server;

import com.skyguard.zmq.netty.decoder.KryoDecoder;
import com.skyguard.zmq.netty.decoder.RequestDataDecoder;
import com.skyguard.zmq.netty.encoder.KryoEncoder;
import com.skyguard.zmq.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class CommonTcpServer implements ZmqServer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private EventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private static final int PROCESSORS = 5;



    @Override
    public void registerProcessor(String name, Object serviceInstance) {

    }

    @Override
    public void start(int port,int timeout) throws Exception{
        // TODO Auto-generated method stub
        bossGroup = new NioEventLoopGroup(PROCESSORS);
        workerGroup = new NioEventLoopGroup(PROCESSORS * 2);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                ByteBuf byteBuf = Unpooled.copiedBuffer("/t".getBytes());
                pipeline.addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                pipeline.addLast(new RequestDataDecoder());
                pipeline.addLast(new KryoEncoder());
                pipeline.addLast(new ServerHandler());

            }

        });
        LOG.info("-----------------开始启动--------------------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOG.info("端口号："+port+"的服务端已经启动");
        LOG.info("-----------------启动结束--------------------------");
    }

    @Override
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
