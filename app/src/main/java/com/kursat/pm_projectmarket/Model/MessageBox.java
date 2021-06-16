package com.kursat.pm_projectmarket.Model;

public class MessageBox {
    public String MessageDate;
    public String Detail;
    public String MessageContent;
    public String Token;
    public String isRead;
    public MessageBox(String MessageDate, String Detail, String MessageContent, String Token,String isRead){
        this.MessageContent=MessageContent;
        this.MessageDate=MessageDate;
        this.Detail=Detail;
        this.Token=Token;
        this.isRead=isRead;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getMessageDate() {
        return MessageDate;
    }

    public String getDetail() {
        return Detail;
    }

    public String getToken() {
        return Token;
    }

    public String getMessageContent() {
        return MessageContent;
    }
}
