package com.zochat.models;

/**
 * Created by zukizaro on 18/11/2017.
 */

public class NotificationItem {
    String email, uid;

    @Override
    public String toString() {
        return "NotificationItem{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public NotificationItem() {
    }

    public NotificationItem(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
