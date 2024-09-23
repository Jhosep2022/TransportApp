package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceToPayPayment {

    @Expose
    @SerializedName("reference")
    private String reference;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("amount")
    private PlaceToPayAmount amount;


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlaceToPayAmount getAmount() {
        return amount;
    }

    public void setAmount(PlaceToPayAmount amount) {
        this.amount = amount;
    }
}
