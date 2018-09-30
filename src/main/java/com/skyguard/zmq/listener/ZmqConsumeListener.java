package com.skyguard.zmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZmqConsumeListener implements ZmqMessageListener{

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onSuccess(Object data) {
        LOG.info("consume data:"+data.toString());
    }

    @Override
    public void onError(Exception e) {
       LOG.error("get data error",e);
    }
}
