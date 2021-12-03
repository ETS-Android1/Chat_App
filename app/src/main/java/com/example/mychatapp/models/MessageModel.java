package com.example.mychatapp.models;

import java.util.Date;

public class MessageModel {
    private String message;
    private String senderId;
    private String receiverId;
    private String timestamp;
    private Date date;

    public MessageModel() {
    }

    public MessageModel(String message, String senderId, String receiverId, String time, Date date) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = time;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
