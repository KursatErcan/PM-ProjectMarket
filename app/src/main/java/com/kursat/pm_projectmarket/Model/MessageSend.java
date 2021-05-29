package com.kursat.pm_projectmarket.Model;

public class MessageSend {
    public String message_sended;
    public String message_detail;
    public String message_date;
    public String message_sended_id;
    public MessageSend(String message_detail,String message_sended_id){
        this.message_sended_id=message_sended_id;
        this.message_detail=message_detail;

    }


    public String getMessage_sended_id() {
        return message_sended_id;
    }

    public String getMessage_detail() {
        return message_detail;
    }

}
