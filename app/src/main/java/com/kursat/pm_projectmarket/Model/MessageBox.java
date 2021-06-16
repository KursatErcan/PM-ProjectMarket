package com.kursat.pm_projectmarket.Model;

public class MessageBox {
    public String date;
    public String senderName;
    public String content;
    public String token;
    public String isRead;
    public String senderId;

    public MessageBox(String date, String content, String senderId,String senderName, String isRead, String token){
        this.date=date;
        this.content=content;
        this.senderId=senderId;
        this.senderName=senderName;
        this.isRead=isRead;
        this.token=token;
    }

    public String getSenderId() { return senderId; }

    public String getIsRead() {
        return isRead;
    }

    public String getDate() {
        return date;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getToken() {
        return token;
    }

    public String getContent() {
        return content;
    }
}
