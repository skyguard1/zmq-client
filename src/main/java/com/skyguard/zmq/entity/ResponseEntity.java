package com.skyguard.zmq.entity;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {

    private static final long     serialVersionUID = 6003763435130075991L;

    private int responseCode;
    private String requestId;
    private String message;
    private T data;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
