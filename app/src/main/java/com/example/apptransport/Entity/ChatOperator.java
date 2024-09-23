package com.example.apptransport.Entity;

public class ChatOperator {
    private String idUser;
    private String userNameUser;
    private String firstNameUser;
    private String lastNameUser;
    private String emailUser;
    private String idSocketMap;
    private String idProfileUser;

    //properties for ReciclerView
    private String lastMessage;
    private int countNewMessages;

    public ChatOperator() {
    }

    public ChatOperator(String idUser, String userNameUser, String firstNameUser, String lastNameUser, String emailUser, String idSocketMap, String idProfileUser, String lastMessage, int countNewMessages) {
        this.idUser = idUser;
        this.userNameUser = userNameUser;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
        this.emailUser = emailUser;
        this.idSocketMap = idSocketMap;
        this.idProfileUser = idProfileUser;
        this.lastMessage=lastMessage;
        this.countNewMessages=countNewMessages;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserNameUser() {
        return userNameUser;
    }

    public void setUserNameUser(String userNameUser) {
        this.userNameUser = userNameUser;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public void setFirstNameUser(String firstNameUser) {
        this.firstNameUser = firstNameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getIdSocketMap() {
        return idSocketMap;
    }

    public void setIdSocketMap(String idSocketMap) {
        this.idSocketMap = idSocketMap;
    }

    public String getIdProfileUser() {
        return idProfileUser;
    }

    public void setIdProfileUser(String idProfileUser) {
        this.idProfileUser = idProfileUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getCountNewMessages() {
        return countNewMessages;
    }

    public void setCountNewMessages(int countNewMessages) {
        this.countNewMessages = countNewMessages;
    }
}
