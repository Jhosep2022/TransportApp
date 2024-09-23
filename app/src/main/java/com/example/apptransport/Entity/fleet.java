package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by usario on 1/8/2017.
 */

public class fleet {


    @Expose
    @SerializedName("idVeichleBrandAsigned")
    public Integer idVeichleBrandAsigned;


    @Expose
    @SerializedName("idVehicleModelAsigned")
    public Integer idVehicleModelAsigned;

    @Expose
    @SerializedName("idVehiclenTypeAsigned")
    public Integer idVehiclenTypeAsigned;


    @Expose
    @SerializedName("domain")
    public String domain;


    public fleet(Integer idVeichleBrandAsigned, Integer idVehicleModelAsigned, Integer idVehiclenTypeAsigned, String domain) {
        this.idVeichleBrandAsigned = idVeichleBrandAsigned;
        this.idVehicleModelAsigned = idVehicleModelAsigned;
        this.idVehiclenTypeAsigned = idVehiclenTypeAsigned;
        this.domain = domain;
    }

    public int getIdVeichleBrandAsigned() {
        return idVeichleBrandAsigned;
    }

    public void setIdVeichleBrandAsigned(Integer idVeichleBrandAsigned) {
        this.idVeichleBrandAsigned = idVeichleBrandAsigned;
    }

    public int getIdVehicleModelAsigned() {
        return idVehicleModelAsigned;
    }

    public void setIdVehicleModelAsigned(Integer idVehicleModelAsigned) {
        this.idVehicleModelAsigned = idVehicleModelAsigned;
    }

    public int getIdVehiclenTypeAsigned() {
        return idVehiclenTypeAsigned;
    }

    public void setIdVehiclenTypeAsigned(Integer idVehiclenTypeAsigned) {
        this.idVehiclenTypeAsigned = idVehiclenTypeAsigned;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
