package com.example.apptransport.core.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author Luis Hernandez
 * @version 0.0.1
 */
public class SocketRoot implements Serializable {
    @SerializedName("location")
    public SocketLocation location;
    @SerializedName("fullNameDriver")
    public String fullNameDriver;
    @SerializedName("nrDriver")
    public String nrDriver;
}
