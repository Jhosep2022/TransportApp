package com.example.apptransport.viewmodels;

import com.apreciasoft.mobile.asremis.Entity.Chat;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceive;
import com.google.firebase.database.MutableData;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    private Chat chatItem;
    private static Chat lastReceivedMessage;
    private MutableLiveData<Chat> listChatMutable;
    public HashMap<String, String> hashMapChat;

    public ChatViewModel() {
        super();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        try{
            initChatMutable();
            listChatMutable.postValue(null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initChatMutable(){
        if(listChatMutable==null){
            listChatMutable=new MutableLiveData<>();
        }
    }

    public MutableLiveData<Chat> listChats(){
        if(listChatMutable ==null)
        {
            listChatMutable =new MutableLiveData<>();
            initializeLastReceivedMessage();
            hashMapChat=new HashMap<>();
        }
        return listChatMutable;
    }

    private void initializeLastReceivedMessage(){
        if(lastReceivedMessage==null){
            lastReceivedMessage=new Chat();
        }
    }

    public boolean addChatMessage(Chat value, String id) {
        boolean result=false;
        try {
            if (listChatMutable == null) {
                listChatMutable = new MutableLiveData<>();
                initializeLastReceivedMessage();
                hashMapChat=new HashMap<>();
            }
            if(!hashMapChat.containsKey(id)){
                hashMapChat.put(id, id);
                chatItem = value;
                lastReceivedMessage=value;
                listChatMutable.setValue(chatItem);
                result=true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Chat getLastChatMessageReceived(){
        return lastReceivedMessage;
    }

    public void setLastChatMessageReceived(Chat chat){
        this.lastReceivedMessage=chat;
    }

    public void clearChat(){
        chatItem=null;
        initChatMutable();
        listChatMutable.postValue(null);
    }
}

