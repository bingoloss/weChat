package com.lehand.wechat.chat.model;

import com.github.library.entity.MultiItemEntity;
import com.hyphenate.chat.EMMessage;

/**
 * Created by bingo on 2018/1/22.
 * 消息类
 */

public class Message extends MultiItemEntity {

    public static final int SEND_TEXT = 1;
    public static final int FROM_TEXT = 2;

    private String from;
    private String to;
    private String body;

    public Message() {
    }

    public Message(String from, String to, String body) {
        this.from = from;
        this.to = to;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
