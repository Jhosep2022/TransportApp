package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDataOrigenPactado {

    @Expose
    @SerializedName("idPreOriginCom")
    private String idPreOriginCom;

    @Expose
    @SerializedName("namePreOriginCom")
    private String namePreOriginCom;

    @Expose
    @SerializedName("addresPreOriginCom")
    private String addresPreOriginCom;

    @Expose
    @SerializedName("latPreOriginCom")
    private String latPreOriginCom;

    @Expose
    @SerializedName("longPreOriginCom")
    private String longPreOriginCom;


    public String getIdPreOriginCom() {
        return idPreOriginCom;
    }

    public void setIdPreOriginCom(String idPreOriginCom) {
        this.idPreOriginCom = idPreOriginCom;
    }

    public String getNamePreOriginCom() {
        return namePreOriginCom;
    }

    public void setNamePreOriginCom(String namePreOriginCom) {
        this.namePreOriginCom = namePreOriginCom;
    }

    public String getAddresPreOriginCom() {
        return addresPreOriginCom;
    }

    public void setAddresPreOriginCom(String addresPreOriginCom) {
        this.addresPreOriginCom = addresPreOriginCom;
    }

    public String getLatPreOriginCom() {
        return latPreOriginCom;
    }

    public void setLatPreOriginCom(String latPreOriginCom) {
        this.latPreOriginCom = latPreOriginCom;
    }

    public String getLongPreOriginCom() {
        return longPreOriginCom;
    }

    public void setLongPreOriginCom(String longPreOriginCom) {
        this.longPreOriginCom = longPreOriginCom;
    }
}
