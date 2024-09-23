package com.example.apptransport.core.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author Luis Hernandez
 * @version 0.0.1
 */
public class SocketLocation implements Serializable {
    @SerializedName("lat")
    public String lat;
    @SerializedName("lng")
    public String lng;
}
