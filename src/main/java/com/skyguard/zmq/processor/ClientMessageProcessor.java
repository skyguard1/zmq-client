package com.skyguard.zmq.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientMessageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static Map<String,List<InetSocketAddress>> clientMap = Maps.newConcurrentMap();

    public static void registerClient(String topic,String ip,int port){

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
        if(!clientMap.containsKey(topic)){
            clientMap.put(topic, Lists.newArrayList(inetSocketAddress));
        }else{
            List<InetSocketAddress> socketAddressList = clientMap.get(topic);
            socketAddressList.add(inetSocketAddress);
        }

    }

    public static List<InetSocketAddress> getClient(String topic){

        List<InetSocketAddress> socketAddressList = Lists.newArrayList();

        if(clientMap.containsKey(topic)){
            socketAddressList = clientMap.get(topic);
        }

        return socketAddressList;
    }

    public static void removeClient(String ip,int port){

        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip,port);
        Set<String> set = clientMap.keySet();
        for(String topic:set) {
            if (clientMap.containsKey(topic)) {
                List<InetSocketAddress> socketAddressList = clientMap.get(topic);
                socketAddressList.remove(inetSocketAddress);
            }
        }
        
    }

    public static void clear(){
        clientMap.clear();
    }


}
