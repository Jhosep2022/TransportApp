package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientRecoveryPass {
    @SerializedName("user")
    @Expose
    private ClientEmail client;

    public ClientEmail getClient() {
        return client;
    }

    public void setClient(ClientEmail client) {
        this.client = client;
    }

}
