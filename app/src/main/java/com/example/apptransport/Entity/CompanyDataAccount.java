package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDataAccount {
    @Expose
    @SerializedName("isaddress")
    private String isAddress;

    public String getIsAddress() {
        return isAddress;
    }

    public void setIsAddress(String isAddress) {
        this.isAddress = isAddress;
    }
}
