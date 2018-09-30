package com.skyguard.zmq.processor;

import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.netty.client.ZmqClient;
import com.skyguard.zmq.netty.client.ZmqClientFactory;
import com.skyguard.zmq.netty.client.ZmqCommonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ZmqClientManager {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ZmqClient zmqClient;

    public ZmqClientManager(ZmqClient zmqClient) {
        this.zmqClient = zmqClient;
    }

    public void startClient(int port,int connectTimeout){

        try {
            zmqClient.startClient(connectTimeout);
        }catch(Exception e){
            LOG.error("start client error",e);
        }

    }

    public ZmqCommonClient createClient(String ip, int port){

        try {
            ZmqCommonClient zmqCommonClient = zmqClient.createClient(ip, port);
            return zmqCommonClient;
        }catch(Exception e){
            LOG.error("create client error",e);
            ClientMessageProcessor.removeClient(ip,port);
        }

        return null;
    }

    public void sendMessage(String ip,int port,RequestEntity requestEntity) throws Exception{

        String key = ip+":"+port;
        ZmqCommonClient commonClient = ZmqClientFactory.getClient(key);
        if(commonClient==null){
            commonClient = createClient(ip,port);
        }
        String uuid = UUID.randomUUID().toString();
        requestEntity.setRequestId(uuid);
        if(commonClient!=null) {
            commonClient.sendRequest(requestEntity);
        }

    }


    public void stop(){
        zmqClient.stopClient();
    }



}
