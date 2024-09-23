package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 02-01-2017.
 */

public class userAuthEntity {

    @Expose
    @SerializedName("user")
    public user user;

    @Expose
    @SerializedName("driver")
    public Driver driver;

    @Expose
    @SerializedName("client")
    public Client client;

    @Expose
    @SerializedName("param")
    public List<paramEntity> param;


    @Expose
    @SerializedName("vehicleType")
    public List<VehicleType> listVehicleType;

    @Expose
    @SerializedName("currentTravel")
    public InfoTravelEntity currentTravel;

    @Expose
    @SerializedName("instance")
    public String instance;

    @Expose
    @SerializedName("driverInactive")
    public boolean driverInactive;



    @Expose
    @SerializedName("currency")
    public Currency currency;


    public boolean isDriverInactive() {
        return driverInactive;
    }

    public void setDriverInactive(boolean driverInactive) {
        this.driverInactive = driverInactive;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public List<paramEntity> getParam() {
        return param;
    }

    public void setParam(List<paramEntity> param) {
        this.param = param;
    }

    public InfoTravelEntity getCurrentTravel() {
        return currentTravel;
    }

    public void setCurrentTravel(InfoTravelEntity currentTravel) {
        this.currentTravel = currentTravel;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<VehicleType> getListVehicleType() {
        return listVehicleType;
    }

    public void setListVehicleType(List<VehicleType> listVehicleType) {
        this.listVehicleType = listVehicleType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
