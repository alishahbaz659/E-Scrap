package com.jason.escrap.Model;

public class Message_users {
    String senderUid;
    String receiverUid;


    public Message_users() {
    }

    public Message_users(String senderUid, String receiverUid) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

}
