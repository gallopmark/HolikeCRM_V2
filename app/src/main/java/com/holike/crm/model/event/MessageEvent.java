package com.holike.crm.model.event;

public class MessageEvent {
    String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
