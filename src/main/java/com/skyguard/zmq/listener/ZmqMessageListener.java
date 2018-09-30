package com.skyguard.zmq.listener;

public interface ZmqMessageListener {

    public void onSuccess(Object data);

    public void onError(Exception e);

}
