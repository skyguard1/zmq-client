package com.skyguard.zmq.entity;

import java.io.Serializable;

public class CommonMessage<T> implements Serializable {

    private static final long     serialVersionUID = 6003763435130075991L;

    private String topic;
    private T data;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
