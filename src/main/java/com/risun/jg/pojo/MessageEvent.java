package com.risun.jg.pojo;

/**
 * Created by whd on 2018/1/17.
 */

public class MessageEvent {
    private String message;
    public MessageEvent(String message){
        this.message=message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
