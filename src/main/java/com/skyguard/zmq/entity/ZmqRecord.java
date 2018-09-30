package com.skyguard.zmq.entity;

public class ZmqRecord<T> {

    private String topic;
    private T data;


    public ZmqRecord() {
    }

    public ZmqRecord(String topic, T data) {
        this.topic = topic;
        this.data = data;
    }

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
