package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalificationStar {

    @Expose
    @SerializedName("star")
    public int star;


    @Expose
    @SerializedName("idTravel")
    private int idTravel;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }


    public int getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(int idTravel) {
        this.idTravel = idTravel;
    }
}
