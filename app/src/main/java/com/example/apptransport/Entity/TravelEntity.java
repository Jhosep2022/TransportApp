package com.example.apptransport.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 05/01/2017.
 */

public class TravelEntity {


    @Expose
    @SerializedName("travel")
    public TravelBodyEntity mTravelBody;

    public TravelEntity(TravelBodyEntity travelBody) {
        mTravelBody = travelBody;
    }

    public TravelEntity() {
    }

    public TravelBodyEntity getTravelBody() {
        return mTravelBody;
    }

    public void setTravelBody(TravelBodyEntity travelBody) {
        mTravelBody = travelBody;
    }

    public String makeJson(){
        Gson gson= new Gson();
        String _jsonstring= gson.toJson(this);
        return  _jsonstring;
    }
}
