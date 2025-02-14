package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by usario on 1/8/2017.
 */

public class driverAdd {

    @Expose
    @SerializedName("fisrtNameDriver")
    public String fisrtNameDriver;



    @Expose
    @SerializedName("nrDriver")
    public String nrDriver;

    @Expose
    @SerializedName("emailDriver")
    public String emailDriver;

    @Expose
    @SerializedName("passDriver")
    public String passDriver;


    @Expose
    @SerializedName("phoneNumberDriver")
    public String phoneNumberDriver;

    @Expose
    @SerializedName("isVehicleProvider")
    public int isVehicleProvider;

    @Expose
    @SerializedName("isRequestMobil")
    public int isRequestMobil;

    @Expose
    @SerializedName("socialid")
    private String socialid;

    @Expose
    @SerializedName("acceptedTerms")
    private String acceptedTerms;


    public int getIsRequestMobil() {
        return isRequestMobil;
    }

    public void setIsRequestMobil(int isRequestMobil) {
        this.isRequestMobil = isRequestMobil;
    }

    public int getIsVehicleProvider() {
        return isVehicleProvider;
    }

    public void setIsVehicleProvider(int isVehicleProvider) {
        this.isVehicleProvider = isVehicleProvider;
    }

    public driverAdd(String fisrtNameDriver, String nrDriver, String emailDriver, String passDriver, String phoneNumberDriver,int isVehicleProvider,int isRequestMobil, String socialid, String acceptedTerms) {
        this.fisrtNameDriver = fisrtNameDriver;
        this.nrDriver = nrDriver;
        this.emailDriver = emailDriver;
        this.passDriver = passDriver;
        this.phoneNumberDriver = phoneNumberDriver;
        this.isVehicleProvider = isVehicleProvider;
        this.isRequestMobil = isRequestMobil;
        this.socialid=socialid;
        this.acceptedTerms=acceptedTerms;

    }

    public String getFisrtNameDriver() {
        return fisrtNameDriver;
    }

    public void setFisrtNameDriver(String fisrtNameDriver) {
        this.fisrtNameDriver = fisrtNameDriver;
    }

    public String getNrDriver() {
        return nrDriver;
    }

    public void setNrDriver(String nrDriver) {
        this.nrDriver = nrDriver;
    }

    public String getEmailDriver() {
        return emailDriver;
    }

    public void setEmailDriver(String emailDriver) {
        this.emailDriver = emailDriver;
    }

    public String getPassDriver() {
        return passDriver;
    }

    public void setPassDriver(String passDriver) {
        this.passDriver = passDriver;
    }

    public String getPhoneNumberDriver() {
        return phoneNumberDriver;
    }

    public void setPhoneNumberDriver(String phoneNumberDriver) {
        this.phoneNumberDriver = phoneNumberDriver;
    }

    public String getSocialid() {
        return socialid;
    }

    public void setSocialid(String socialid) {
        this.socialid = socialid;
    }

    public String getAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(String acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}
