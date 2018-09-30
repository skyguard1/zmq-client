package com.skyguard.zmq.processor;

import com.google.common.collect.Maps;
import com.skyguard.zmq.consumer.ZmqConsumer;

import java.util.Map;

public class ZmqConsumerProcessor {

    private static Map<String,ZmqConsumer> clientMap = Maps.newConcurrentMap();

    public static void registerClient(String topic,ZmqConsumer consumer){

        clientMap.put(topic,consumer);

    }

    public static ZmqConsumer getConsumer(String topic){

        return clientMap.get(topic);

    }




}
