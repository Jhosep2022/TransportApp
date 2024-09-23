package com.example.apptransport.core.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Luis Hernandez
 * @version 0.0.1
 */
public class Travel implements Serializable {
    @SerializedName("idUser")
    public int idUser;
    @SerializedName("idTravelKf")
    public int idTravelKf;
    @SerializedName("idDriverKf")
    public int idDriverKf;
    @SerializedName("idVeichleAsigned")
    public int idVeichleAsigned;
    @SerializedName("idClientKf")
    public int idClientKf;
    @SerializedName("distanceSave")
    public Double distanceSave;
    @SerializedName("totalAmount")
    public Double totalAmount;
    @SerializedName("location")
    public String location;
    @SerializedName("longLocation")
    public String longLocation;
    @SerializedName("latLocation")
    public String latLocation;
    @SerializedName("distanceGps")
    public Double distanceGps;
    @SerializedName("distanceGpsReturn")
    public Double distanceGpsReturn;
    @SerializedName("distanceGpsLabel")
    public String distanceGpsLabel;
    @SerializedName("amounttoll")
    public Double amounttoll;
    @SerializedName("amountParking")
    public Double amountParking;
    @SerializedName("amountTiemeSlepp")
    public Double amountTiemeSlepp;
    @SerializedName("timeSleppGps")
    public String timeSleppGps;
    @SerializedName("idPaymentFormKf")
    public int idPaymentFormKf;
    @SerializedName("mp_jsonPaymentCard")
    public String mpJsonPaymentCard;
    @SerializedName("mp_paymentMethodId")
    public String mpPaymentMethodId;
    @SerializedName("mp_paymentTypeId")
    public String mpPaymentTypeId;
    @SerializedName("mp_paymentstatus")
    public String mpPaymentstatus;
    @SerializedName("amountFleet")
    public Double amountFleet;
}
