package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointToPointItem {

    @Expose  @SerializedName("idPoint")
    private String idPoint;

    @Expose  @SerializedName("namePoint")
    private String namePoint;

    @Expose  @SerializedName("addressOrigin")
    private String addressOrigin;

    @Expose  @SerializedName("addressDestination")
    private String addressDestination;

    @Expose  @SerializedName("latDestination")
    private String latDestination;

    @Expose  @SerializedName("lonDestination")
    private String lonDestination;

    @Expose  @SerializedName("pricePoint")
    private String pricePoint;

    @Expose  @SerializedName("priceDrivePoint")
    private String priceDrivePoint;

    @Expose  @SerializedName("latOrigin")
    private String latOrigin;

    @Expose  @SerializedName("lonOrigin")
    private String lonOrigin;

    public PointToPointItem() {

    }

    public PointToPointItem(String idPoint, String namePoint, String addressOrigin, String addressDestination, String latDestination, String lonDestination, String pricePoint, String priceDrivePoint, String latOrigin, String lonOrigin) {
        this.idPoint = idPoint;
        this.namePoint = namePoint;
        this.addressOrigin = addressOrigin;
        this.addressDestination = addressDestination;
        this.latDestination = latDestination;
        this.lonDestination = lonDestination;
        this.pricePoint = pricePoint;
        this.priceDrivePoint = priceDrivePoint;
        this.latOrigin = latOrigin;
        this.lonOrigin = lonOrigin;
    }

    public String getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(String idPoint) {
        this.idPoint = idPoint;
    }

    public String getNamePoint() {
        return namePoint;
    }

    public void setNamePoint(String namePoint) {
        this.namePoint = namePoint;
    }

    public String getAddressOrigin() {
        return addressOrigin;
    }

    public void setAddressOrigin(String addressOrigin) {
        this.addressOrigin = addressOrigin;
    }

    public String getAddressDestination() {
        return addressDestination;
    }

    public void setAddressDestination(String addressDestination) {
        this.addressDestination = addressDestination;
    }

    public String getLatDestination() {
        return latDestination;
    }

    public void setLatDestination(String latDestination) {
        this.latDestination = latDestination;
    }

    public String getLonDestination() {
        return lonDestination;
    }

    public void setLonDestination(String lonDestination) {
        this.lonDestination = lonDestination;
    }

    public String getPricePoint() {
        return pricePoint;
    }

    public void setPricePoint(String pricePoint) {
        this.pricePoint = pricePoint;
    }

    public String getPriceDrivePoint() {
        return priceDrivePoint;
    }

    public void setPriceDrivePoint(String priceDrivePoint) {
        this.priceDrivePoint = priceDrivePoint;
    }

    public String getLatOrigin() {
        return latOrigin;
    }

    public void setLatOrigin(String latOrigin) {
        this.latOrigin = latOrigin;
    }

    public String getLonOrigin() {
        return lonOrigin;
    }

    public void setLonOrigin(String lonOrigin) {
        this.lonOrigin = lonOrigin;
    }
}
