package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Marlon Viana on 15/03/2019
 * @email 92marlonViana@gmail.com
 */
public class GuardarPagoTcd {
    @SerializedName("response")
    @Expose
    private Boolean response;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }
}
