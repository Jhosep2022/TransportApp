package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Passenger {

    @SerializedName("idPasaguer")
    @Expose
    private String idPasaguer;

    @SerializedName("fullNamePanguer")
    @Expose
    private String fullNamePanguer;

    @SerializedName("direcctionPasaguer")
    @Expose
    private String direcctionPasaguer;

    @SerializedName("phonePasaguer")
    @Expose
    private String phonePasaguer;

    @SerializedName("emailPasaguer")
    @Expose
    private String emailPasaguer;

    @SerializedName("listPassenger")
    @Expose
    private String listPassenger;

    @SerializedName("pAQuantity")
    @Expose
    private PassengerQuantity pAQuantity;



    public String getIdPasaguer() {
        return idPasaguer;
    }

    public void setIdPasaguer(String idPasaguer) {
        this.idPasaguer = idPasaguer;
    }

    public String getFullNamePanguer() {
        return fullNamePanguer;
    }

    public void setFullNamePanguer(String fullNamePanguer) {
        this.fullNamePanguer = fullNamePanguer;
    }

    public String getDirecctionPasaguer() {
        return direcctionPasaguer;
    }

    public void setDirecctionPasaguer(String direcctionPasaguer) {
        this.direcctionPasaguer = direcctionPasaguer;
    }

    public String getPhonePasaguer() {
        return phonePasaguer;
    }

    public void setPhonePasaguer(String phonePasaguer) {
        this.phonePasaguer = phonePasaguer;
    }

    public String getEmailPasaguer() {
        return emailPasaguer;
    }

    public void setEmailPasaguer(String emailPasaguer) {
        this.emailPasaguer = emailPasaguer;
    }

    public String getListPassenger() {
        return listPassenger;
    }

    public void setListPassenger(String listPassenger) {
        this.listPassenger = listPassenger;
    }

    public PassengerQuantity getpAQuantity() {
        return pAQuantity;
    }

    public void setpAQuantity(PassengerQuantity pAQuantity) {
        this.pAQuantity = pAQuantity;
    }
}
