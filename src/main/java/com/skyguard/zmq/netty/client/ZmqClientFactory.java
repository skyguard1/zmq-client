package com.skyguard.zmq.netty.client;

import com.google.common.collect.Maps;

import java.util.Map;

public class ZmqClientFactory {

    private static Map<String,ZmqCommonClient> clientMap = Maps.newConcurrentMap();

    public static void putClient(String name,ZmqCommonClient commonClient){
        clientMap.put(name,commonClient);
    }

    public static ZmqCommonClient getClient(String name){
        return clientMap.get(name);
    }

    public static void clear(){
        clientMap.clear();
    }


}
