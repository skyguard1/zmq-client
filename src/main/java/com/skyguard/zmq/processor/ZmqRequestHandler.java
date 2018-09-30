package com.skyguard.zmq.processor;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ZmqRequestHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static Map<String,Object> dataMap = Maps.newConcurrentMap();

    public static void putData(String requestId,Object data){
        dataMap.put(requestId,data);
    }

    public static Object getData(String requestId){
        return dataMap.get(requestId);
    }



}
