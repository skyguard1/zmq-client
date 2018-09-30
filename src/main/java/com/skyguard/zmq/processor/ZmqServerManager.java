package com.skyguard.zmq.processor;

import com.skyguard.zmq.netty.server.ZmqServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZmqServerManager {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ZmqServer zmqServer;

    public ZmqServerManager(ZmqServer zmqServer) {
        this.zmqServer = zmqServer;
    }

    public void start(int port,int timeout){
        try {
            zmqServer.start(port, timeout);
        }catch(Exception e){
            LOG.error("start server error",e);
        }
    }

    public void stop(){
        zmqServer.stop();
    }




}
