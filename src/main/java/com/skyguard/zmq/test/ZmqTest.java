package com.skyguard.zmq.test;

import com.skyguard.zmq.consumer.ZmqConsumer;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import com.skyguard.zmq.entity.ZmqRecord;
import com.skyguard.zmq.listener.ZmqConsumeListener;
import com.skyguard.zmq.processor.ZmqConsumerProcessor;
import com.skyguard.zmq.processor.ZmqServerProcessor;
import com.skyguard.zmq.producer.ZmqProducer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class ZmqTest {

    private final Logger LOG = LoggerFactory.getLogger(ZmqTest.class);

    @Test
    public void test1(){


        ZmqRecord zmqRecord = new ZmqRecord("test1","test");



        try {
            ZmqProducer producer = new ZmqProducer(10013, 20000);
            producer.produce(zmqRecord);
            LOG.info("produce data:"+zmqRecord.getData().toString());
        }catch(Exception e){
            LOG.error("produce message error",e);
        }

        try {
            System.in.read();
        } catch (IOException e) {

        }


    }

    @Test
    public void test2() {



        try {
            ZmqConsumer consumer = new ZmqConsumer(10019,2000);
            ZmqConsumerProcessor.registerClient("test1",consumer);
            Object data = consumer.consume("test1");
            LOG.info("consume data:"+data.toString());
            consumer.addListener(new ZmqConsumeListener());
        }catch(Exception e){
            LOG.error("consume message error",e);
        }


        try {
            System.in.read();
        } catch (IOException e) {

        }


    }







}
