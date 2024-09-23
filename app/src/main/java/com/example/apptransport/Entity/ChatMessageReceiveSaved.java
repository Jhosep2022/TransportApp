package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatMessageReceiveSaved {

    @Expose
    @SerializedName("saved")
    public List<ChatMessageReceive> listMessagesSaved;
}
