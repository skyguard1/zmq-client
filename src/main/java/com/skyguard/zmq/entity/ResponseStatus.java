package com.skyguard.zmq.entity;

public enum ResponseStatus {

    SUCCESS(200,"成功"),FORBIDEN(403,"权限错误"),ERROR(500,"错误")
    ;

    private int code;
    private String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
