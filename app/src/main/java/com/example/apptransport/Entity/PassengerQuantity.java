package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerQuantity {

    @SerializedName("adults")
    @Expose
    private Integer adults;

    @SerializedName("children")
    @Expose
    private Integer children;

    @SerializedName("disable")
    @Expose
    private Integer disable;

    @SerializedName("babies")
    @Expose
    private Integer babies;

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public Integer getBabies() {
        return babies;
    }

    public void setBabies(Integer babies) {
        this.babies = babies;
    }
}
