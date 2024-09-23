package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyParams {

    @Expose
    @SerializedName("travelMinAmount")
    private String travelMinAmount;

    @Expose
    @SerializedName("travelMinMode")
    private String travelMinMode;

    @Expose
    @SerializedName("extraTmStart")
    private String extraTmStart;

    @Expose
    @SerializedName("extraTmEnd")
    private String extraTmEnd;

    @Expose
    @SerializedName("extraTmFixed")
    private String extraTmFixed;

    @Expose
    @SerializedName("extraTmAmount")
    private String extraTmAmount;


    public String getTravelMinAmount() {
        return travelMinAmount;
    }

    public void setTravelMinAmount(String travelMinAmount) {
        this.travelMinAmount = travelMinAmount;
    }

    public String getTravelMinMode() {
        return travelMinMode;
    }

    public void setTravelMinMode(String travelMinMode) {
        this.travelMinMode = travelMinMode;
    }

    public String getExtraTmStart() {
        return extraTmStart;
    }

    public void setExtraTmStart(String extraTmStart) {
        this.extraTmStart = extraTmStart;
    }

    public String getExtraTmEnd() {
        return extraTmEnd;
    }

    public void setExtraTmEnd(String extraTmEnd) {
        this.extraTmEnd = extraTmEnd;
    }

    public String getExtraTmFixed() {
        return extraTmFixed;
    }

    public void setExtraTmFixed(String extraTmFixed) {
        this.extraTmFixed = extraTmFixed;
    }

    public String getExtraTmAmount() {
        return extraTmAmount;
    }

    public void setExtraTmAmount(String extraTmAmount) {
        this.extraTmAmount = extraTmAmount;
    }
}
