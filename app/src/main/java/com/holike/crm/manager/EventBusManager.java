package com.holike.crm.manager;

import android.app.Activity;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

public class EventBusManager {

    public EventBusManager() {
    }

    public static EventBusManager getInstance() {
        return EventBusManagerHolder.instance;
    }


    public void postEvent(String str) {
        EventBus.getDefault().post(new MessageEvent(str));
    }

    public void onDestory(Object context) {
        EventBus.getDefault().unregister(context);
    }
    public void regist(Object context) {
        EventBus.getDefault().unregister(context);
    }


    static class EventBusManagerHolder {
        static EventBusManager instance = new EventBusManager();
    }


    public static class MessageEvent {
       public static String message;

        public  MessageEvent(String message) {
            this.message = message;
        }
    }

}
