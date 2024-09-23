package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceToPayPaymentResult {

    @Expose
    @SerializedName("requestId")
    private int requestId;

    @Expose
    @SerializedName("processUrl")
    private String processUrl;

    @Expose
    @SerializedName("status")
    private PlaceToPayPaymentResultStatus status;


    @Expose
    @SerializedName("payment")
    private List<PlaceToPayPaymentResultTransaction> payment;

    @Expose
    @SerializedName("request")
    private PlaceToPayPaymentRequest request;


    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getProcessUrl() {
        return processUrl;
    }

    public void setProcessUrl(String processUrl) {
        this.processUrl = processUrl;
    }

    public PlaceToPayPaymentResultStatus getStatus() {
        return status;
    }

    public void setStatus(PlaceToPayPaymentResultStatus status) {
        this.status = status;
    }

    public List<PlaceToPayPaymentResultTransaction> getPayment() {
        return payment;
    }

    public void setPayment(List<PlaceToPayPaymentResultTransaction> payment) {
        this.payment = payment;
    }

    public PlaceToPayPaymentRequest getRequest() {
        return request;
    }

    public void setRequest(PlaceToPayPaymentRequest request) {
        this.request = request;
    }
}
