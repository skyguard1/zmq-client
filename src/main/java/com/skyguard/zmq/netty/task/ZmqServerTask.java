package com.skyguard.zmq.netty.task;

import com.skyguard.zmq.consumer.ZmqConsumer;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import com.skyguard.zmq.entity.ResponseEntity;
import com.skyguard.zmq.entity.ResponseStatus;
import com.skyguard.zmq.netty.client.ZmqCommonClient;
import com.skyguard.zmq.processor.ZmqConsumerProcessor;

import java.util.concurrent.Callable;

public class ZmqServerTask implements Callable<ResponseEntity>{

    private RequestEntity requestEntity;


    public ZmqServerTask(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    private ZmqConsumer getConsumer(String topic){
        return ZmqConsumerProcessor.getConsumer(topic);
    }

    @Override
    public ResponseEntity call() throws Exception {
        ResponseEntity responseEntity = new ResponseEntity();
        try {

            if(requestEntity.getType()==RequestType.NOTIFICATION.getType()) {
                ZmqConsumer consumer = getConsumer(requestEntity.getTopic());
                if(consumer!=null) {
                    Object data = consumer.consume(requestEntity.getTopic());
                    responseEntity.setData(data);
                    responseEntity.setRequestId(requestEntity.getRequestId());
                    responseEntity.setResponseCode(ResponseStatus.SUCCESS.getCode());
                }
            }
        }catch(Exception e){
            responseEntity.setResponseCode(ResponseStatus.ERROR.getCode());
        }
        return responseEntity;
    }
}
