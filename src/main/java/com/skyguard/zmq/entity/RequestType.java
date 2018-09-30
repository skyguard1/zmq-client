package com.skyguard.zmq.entity;

public enum RequestType {

    PRODUCER(1,"producer"),CONSUMER(2,"consumer"),NOTIFICATION(3,"notification")
    ;
    private int type;
    private String message;

    RequestType(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
