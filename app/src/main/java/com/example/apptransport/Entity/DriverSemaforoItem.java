package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverSemaforoItem {
    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("active")
    public int active;
}
