package com.example.apptransport.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Marlon Viana on 01/03/2019
 * @email 92marlonViana@gmail.com
 */
public class InformacionPagoCredit {
    @SerializedName("idTravel")
    @Expose
    private String idTravel;

    @SerializedName("idPayment")
    @Expose
    private String idPayment;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("status")
    @Expose
    private String statusPayment;


    public String getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(String idTravel) {
        this.idTravel = idTravel;
    }

    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String makeJson(){
        Gson gson= new Gson();
        String _jsonstring= gson.toJson(this);
        return  _jsonstring;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }
}
