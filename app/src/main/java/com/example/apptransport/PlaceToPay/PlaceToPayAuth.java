package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceToPayAuth {

    @Expose
    @SerializedName("login")
    private String login;

    @Expose
    @SerializedName("seed")
    private String seed;

    @Expose
    @SerializedName("nonce")
    private String nonce;

    @Expose
    @SerializedName("tranKey")
    private String tranKey;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTranKey() {
        return tranKey;
    }

    public void setTranKey(String tranKey) {
        this.tranKey = tranKey;
    }
}
