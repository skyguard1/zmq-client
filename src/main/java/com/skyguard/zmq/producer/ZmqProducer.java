package com.skyguard.zmq.producer;

import com.skyguard.zmq.config.ZmqConfig;
import com.skyguard.zmq.consumer.ZmqConsumer;
import com.skyguard.zmq.entity.RequestEntity;
import com.skyguard.zmq.entity.RequestType;
import com.skyguard.zmq.entity.ZmqRecord;
import com.skyguard.zmq.netty.client.CommonTcpClient;
import com.skyguard.zmq.processor.ZmqClientManager;
import com.skyguard.zmq.processor.ZmqServerProcessor;
import com.skyguard.zmq.util.FileUtil;
import com.skyguard.zmq.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ZmqProducer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ZmqClientManager clientManager;

    private static final String DATA_PATH = "/opt/zmq/data";

    private static boolean started;

    private static boolean serverStarted;

    public ZmqProducer() {
        clientManager = new ZmqClientManager(new CommonTcpClient());
    }

    public ZmqProducer(int port, int connectTimeout) {
        this();
        this.port = port;
        this.connectTimeout = connectTimeout;
    }

    int port;
    int connectTimeout;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void start(){
        if(port==0){
            port = Integer.parseInt(ZmqConfig.getValue("client.connect.port"));
            connectTimeout = Integer.parseInt(ZmqConfig.getValue("client.connect.timeout"));
        }
        clientManager.startClient(port,connectTimeout);
        started = true;


    }

    private int startServer(){

        int port = Integer.parseInt(ZmqConfig.getValue("server.producer.connect.port"));
        int connectTimeout = Integer.parseInt(ZmqConfig.getValue("server.producer.connect.timeout"));

        String path = "data.txt";
        try {
            if (Files.exists(Paths.get(DATA_PATH + "/" + path))) {
                port = FileUtil.getPort(path);
            } else {
                FileUtil.setPort(port, DATA_PATH + "/" + path);

            }
        }catch (Exception e){
            LOG.error("get file error",e);
        }

        ZmqServerProcessor processor = new ZmqServerProcessor(port,connectTimeout);
        processor.startServer();

        serverStarted = true;

        return port;

    }

    public void produce(ZmqRecord zmqRecord) throws Exception{

        if(!started){
            start();
        }

        int targetPort = Integer.parseInt(ZmqConfig.getValue("server.producer.connect.port"));

        if(!serverStarted){
            targetPort = startServer();
        }


        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setPort(targetPort);
        requestEntity.setType(RequestType.PRODUCER.getType());
        requestEntity.setTopic(zmqRecord.getTopic());
        requestEntity.setData(zmqRecord.getData());

        String severIp = ZmqConfig.getValue("server.connect.ip");
        int serverPort = Integer.parseInt(ZmqConfig.getValue("server.connect.port"));
        clientManager.sendMessage(severIp,serverPort,requestEntity);

    }

    public void stop(){
        clientManager.stop();
    }



}
