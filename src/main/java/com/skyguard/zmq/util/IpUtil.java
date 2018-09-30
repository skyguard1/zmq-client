package com.skyguard.zmq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class IpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);

    public static String getLocalAddress(){

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String address = inetAddress.getHostAddress();
            return address;
        }catch (Exception e){
            LOG.error("get address error",e);
        }

        return null;
    }



}
