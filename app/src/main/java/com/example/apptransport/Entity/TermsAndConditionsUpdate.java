package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsAndConditionsUpdate {

    @Expose
    @SerializedName("user")
    private TermsAndConditionsUpdateUser user;

    public TermsAndConditionsUpdate()
    {
        this.user=new TermsAndConditionsUpdateUser();
    }
    public TermsAndConditionsUpdate(String userId, int accepted)
    {
        this.user= new TermsAndConditionsUpdateUser(userId,accepted);
    }

    public TermsAndConditionsUpdateUser getUser() {
        return user;
    }

    public void setUser(TermsAndConditionsUpdateUser user) {
        this.user = user;
    }
}
