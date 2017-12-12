package com.zochat.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by IT on 11/9/2017.
 */

public class Message {
    String content, date;
    boolean type;

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }

    public Message() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        date = sdf.format(now.getTime());
    }

    public Message(String content, boolean type) {
        this.content = content;
        this.type = type;
    }

    public Message(String content, boolean type, String date) {
        this.content = content;
        this.type = type;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
