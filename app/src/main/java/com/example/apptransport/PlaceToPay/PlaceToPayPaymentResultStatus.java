package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PlaceToPayPaymentResultStatus {

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("reason")
    private String reason;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("date")
    private Date date;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
