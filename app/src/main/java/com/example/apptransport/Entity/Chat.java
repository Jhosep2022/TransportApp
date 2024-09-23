package com.example.apptransport.Entity;

import com.apreciasoft.mobile.asremis.Util.Globales;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String dateSent;
    private boolean wasSent;

    public Chat() {
        this.wasSent=true;
    }

    public Chat(String sender, String receiver, String message, String dateSent) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.dateSent=dateSent;
        this.wasSent=true;
    }

    public Chat(ChatMessageReceive received) {
        this.sender = "0".equals(received.getChatFromWeb())? Globales.ChatMessageType.DRIVER_SENDER: Globales.ChatMessageType.WEB_SENDER;
        this.receiver = "0".equals(received.getChatFromWeb())?  Globales.ChatMessageType.WEB_SENDER: Globales.ChatMessageType.DRIVER_SENDER;
        this.message = received.getChatMessage();
        this.dateSent=received.getChatDate();
        this.wasSent=true;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isWasSent() {
        return wasSent;
    }

    public void setWasSent(boolean wasSent) {
        this.wasSent = wasSent;
    }

}
