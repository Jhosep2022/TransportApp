package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsAndConditionsUpdateUser {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("accepted")
    private int accepted;

    public String getId() {
        return id;
    }

    public TermsAndConditionsUpdateUser()
    {
        this.id="";
        this.accepted=0;
    }

    public TermsAndConditionsUpdateUser(String id, int accepted)
    {
        this.id=id;
        this.accepted=accepted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
}
