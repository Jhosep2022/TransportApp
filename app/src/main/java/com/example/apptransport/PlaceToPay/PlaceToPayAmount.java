package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceToPayAmount {

    @Expose
    @SerializedName("currency")
    private String currency;

    @Expose
    @SerializedName("total")
    private Double total;

    public PlaceToPayAmount(){}

    public PlaceToPayAmount(String currency, Double total){
        this.currency=currency;
        this.total=total;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
