package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelEntityForClientPayment {
    @Expose
    @SerializedName("clientPaymentStatus")
    private int clientPaymentStatus;


    @Expose
    @SerializedName("clienPaymentAmount")
    private double clientPaymentAmount;

    @Expose
    @SerializedName("idTravel")
    private int idTravel;


    public int getClientPaymentStatus() {
        return clientPaymentStatus;
    }

    public void setClientPaymentStatus(int clientPaymentStatus) {
        this.clientPaymentStatus = clientPaymentStatus;
    }

    public double getClientPaymentAmount() {
        return clientPaymentAmount;
    }

    public void setClientPaymentAmount(double clientPaymentAmount) {
        this.clientPaymentAmount = clientPaymentAmount;
    }

    public int getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(int idTravel) {
        this.idTravel = idTravel;
    }
}
