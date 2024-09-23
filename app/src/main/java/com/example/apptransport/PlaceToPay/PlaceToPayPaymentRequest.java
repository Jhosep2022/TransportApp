package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceToPayPaymentRequest {

    @Expose
    @SerializedName("auth")
    private PlaceToPayAuth auth;

    @Expose
    @SerializedName("buyer")
    private PlaceToPayBuyer buyer;



    @Expose
    @SerializedName("payer")
    private PlaceToPayBuyer payer;

    @Expose
    @SerializedName("payment")
    private PlaceToPayPayment payment;

    @Expose
    @SerializedName("expiration")
    private String expiration;

    @Expose
    @SerializedName("ipAddress")
    private String ipAddress;

    @Expose
    @SerializedName("returnUrl")
    private String returnUrl;


    @Expose
    @SerializedName("cancelUrl")
    private String cancelUrl;

    @Expose
    @SerializedName("skipResult")
    private boolean skipResult;


    @Expose
    @SerializedName("userAgent")
    private String userAgent;

    @Expose
    @SerializedName("internalReference")
    private int internalReference;



    public PlaceToPayAuth getAuth() {
        return auth;
    }

    public void setAuth(PlaceToPayAuth auth) {
        this.auth = auth;
    }

    public PlaceToPayBuyer getBuyer() {
        return buyer;
    }

    public void setBuyer(PlaceToPayBuyer buyer) {
        this.buyer = buyer;
    }

    public PlaceToPayBuyer getPayer() {
        return payer;
    }

    public void setPayer(PlaceToPayBuyer payer) {
        this.payer = payer;
    }

    public PlaceToPayPayment getPayment() {
        return payment;
    }

    public void setPayment(PlaceToPayPayment payment) {
        this.payment = payment;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean getSkipResult() {
        return skipResult;
    }

    public boolean isSkipResult() {
        return skipResult;
    }

    public void setSkipResult(boolean skipResult) {
        this.skipResult = skipResult;
    }


    public int getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(int internalReference) {
        this.internalReference = internalReference;
    }



    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
