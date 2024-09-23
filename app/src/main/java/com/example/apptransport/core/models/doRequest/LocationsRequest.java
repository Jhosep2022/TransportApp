package com.example.apptransport.core.models.doRequest;

import com.apreciasoft.mobile.asremis.core.models.Travel;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author Luis Hernandez
 * @version 0.0.1
 */
public class LocationsRequest implements Serializable {
    @SerializedName("travel")
    private Travel travel;
}
