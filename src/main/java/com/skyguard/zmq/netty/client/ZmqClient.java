package com.skyguard.zmq.netty.client;

public interface ZmqClient {

      public void startClient(int connectTimeout) throws Exception;

      public ZmqCommonClient createClient(String ip, int port) throws Exception;

      public void stopClient();




}
