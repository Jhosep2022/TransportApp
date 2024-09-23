package com.example.apptransport.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Marlon Viana on 25/02/2019
 * @email 92marlonViana@gmail.com
 */
public class ItenMercadoPago {

    @Expose
    @SerializedName("idTravel")
    public int idTravel;

    @Expose
    @SerializedName("title")
    public String itemTitle;

    @Expose
    @SerializedName("unit_price")
    public double unit_price;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("consistencia")
    private String constencia;

    @Expose
    @SerializedName("description")
    private String description;

    public String getConstencia() {
        return constencia;
    }

    public void setConstencia(String constencia) {
        this.constencia = constencia;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(int idTravel) {
        this.idTravel = idTravel;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public String makeJson(){
        Gson gson= new Gson();
        String _jsonstring= gson.toJson(this);
        return  _jsonstring;
    }

}
