package com.example.apptransport.Entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatMessageReceive {
    private String idMessage;
    private String chatDriver;
    private String chatWebUser;
    private String chatMessage;
    private String chatDate;
    private String chatFromWeb;



    public ChatMessageReceive() {
    }

    public ChatMessageReceive(String idMessage, String chatDriver, String chatWebUser, String chatMessage, String chatDate, String chatFromWeb) {
        this.idMessage = idMessage;
        this.chatDriver = chatDriver;
        this.chatWebUser = chatWebUser;
        this.chatMessage = chatMessage;
        this.chatDate = chatDate;
        this.chatFromWeb=chatFromWeb;
    }

    public ChatMessageReceive(JSONObject obj) {
        try {
            this.idMessage = obj.getString("idMessage");
            this.chatDriver = obj.getString("chatDriver");
            this.chatWebUser = obj.getString("chatWebUser");
            this.chatMessage = obj.getString("chatMessage");
            this.chatDate = obj.getString("chatDate");
            this.chatFromWeb = obj.getString("chatFromWeb");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String serialize(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("idMessage",idMessage);
            obj.put("chatDriver",chatDriver);
            obj.put("chatWebUser",chatWebUser);
            obj.put("chatMessage",chatMessage);
            obj.put("chatDate",chatDate);
            obj.put("chatFromWeb",chatFromWeb);
            return obj.toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "";
        }

    }


    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getChatDriver() {
        return chatDriver;
    }

    public void setChatDriver(String chatDriver) {
        this.chatDriver = chatDriver;
    }

    public String getChatWebUser() {
        return chatWebUser;
    }

    public void setChatWebUser(String chatWebUser) {
        this.chatWebUser = chatWebUser;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public String getChatFromWeb() {
        return chatFromWeb;
    }

    public void setChatFromWeb(String chatFromWeb) {
        this.chatFromWeb = chatFromWeb;
    }

}
