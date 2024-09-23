package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelBodyForClientPayment {
    @Expose
    @SerializedName("travel")
    private TravelEntityForClientPayment travel;

    public TravelEntityForClientPayment getTravel() {
        return travel;
    }

    public void setTravel(TravelEntityForClientPayment travel) {
        this.travel = travel;
    }
}
