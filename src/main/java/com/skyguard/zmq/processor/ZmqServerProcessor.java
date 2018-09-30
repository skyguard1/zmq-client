package com.skyguard.zmq.processor;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.netty.server.CommonTcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZmqServerProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private int port;
    private int timeout;

    public ZmqServerProcessor() {
    }

    public ZmqServerProcessor(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void startServer(){
        ZmqServerManager zmqServerManager = new ZmqServerManager(new CommonTcpServer());
        if(port==0){
            port = Integer.parseInt(ZmqConfig.getValue("server.connect.port"));
            timeout = Integer.parseInt(ZmqConfig.getValue("server.connect.timeout"));
        }
        zmqServerManager.start(port,timeout);
    }





}
