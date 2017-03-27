package com.xcm91.relation.eventbus;

/**
 * Created by lhy on 2017/3/27.
 */

public class MessageEvent extends BaseEvent {

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
