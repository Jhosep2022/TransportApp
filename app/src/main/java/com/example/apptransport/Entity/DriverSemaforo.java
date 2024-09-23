package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverSemaforo {

    @Expose
    @SerializedName("driver")
    public DriverSemaforoItem driver;
}
