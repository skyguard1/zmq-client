package com.skyguard.zmq.netty.server;

public interface ZmqServer {

    public void registerProcessor(String name, Object serviceInstance);

    public void start(int port, int timeout) throws Exception;

    public void stop();

}
