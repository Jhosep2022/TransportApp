package com.example.apptransport.PlaceToPay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceToPayPaymentResultTransaction {

    @Expose
    @SerializedName("internalReference")
    private int internalReference;

    @Expose
    @SerializedName("paymentMethodName")
    private String paymentMethodName ;


    @Expose
    @SerializedName("paymentMethod")
    private String paymentMethod ;

    @Expose
    @SerializedName("issuerName")
    private String issuerName  ;

    @Expose
    @SerializedName("authorization")
    private String authorization;

    @Expose
    @SerializedName("reference")
    private String reference;



    public int getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(int internalReference) {
        this.internalReference = internalReference;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
