package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jorge gutierrez on 29/03/2017.
 */

public class    VehicleType {

    @Expose
    @SerializedName("idVehicleType")
    public int idVehicleType;

    @Expose
    @SerializedName("vehiclenType")
    public String vehiclenType;

    @Expose
    @SerializedName("vehicleMaxPeople")
    public String vehicleMaxPeople;

    @Expose
    @SerializedName("vehiclePriceKm")
    public String vehiclePriceKm;

    @Expose
    @SerializedName("vehiclePriceHour")
    public String vehiclePriceHour;

    @Expose
    @SerializedName("vehicleDescription")
    public String vehicleDescription;

    @Expose
    @SerializedName("vehicleMotorcycle")
    public String vehicleMotorcycle;

    @Expose
    @SerializedName("vehicleContract")
    public String vehicleContract;

    @Expose
    @SerializedName("idStatusVehicleType")
    public String idStatusVehicleType;

    @Expose
    @SerializedName("conditionContract")
    public String conditionContract;

    @Expose
    @SerializedName("vehiclePriceMin")
    public String vehiclePriceMin;

    @Expose
    @SerializedName("vehiclePriceHourDR")
    public String vehiclePriceHourDR;

    @Expose
    @SerializedName("vehiclePriceKmDR")
    public String vehiclePriceKmDR;

    @Expose
    @SerializedName("vehiclePriceMinDR")
    public String vehiclePriceMinDR;

    @Expose
    @SerializedName("defaultVehicleType")
    public String defaultVehicleType;

    @Expose
    @SerializedName("isFleet")
    public String isFleet;

    @Expose
    @SerializedName("isGroup")
    public String isGroup;

    @Expose
    @SerializedName("vehicleMinHourService")
    public String vehicleMinHourService;

    @Expose
    @SerializedName("dryLoadValue")
    public String dryLoadValue;

    @Expose
    @SerializedName("refrigeratedLoadValue")
    public String refrigeratedLoadValue;

    @Expose
    @SerializedName("limitKmTraveledHr")
    public String limitKmTraveledHr;

    @Expose
    @SerializedName("vehicleValorMin")
    public String vehicleValorMin;

    @Expose
    @SerializedName("vehicleImage")
    public String vehicleImage;

    @Expose
    @SerializedName("Kmex")
    public String kmEx;

    @Expose
    @SerializedName("pricePerKmex")
    public String pricePerKmEx;

    @Expose
    @SerializedName("pricePerMinutex")
    public String pricePerMinutEx;

    @Expose
    @SerializedName("idIvaTypeKf")
    public String idIvaTypeKf;

    @Expose
    @SerializedName("idBenefitKm")
    private String idBenefitKm;

    @Expose
    @SerializedName("benefitList")
    private List<BeneficioEntity> listbenefitKm;

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;


    public VehicleType(int idVehicleType, String vehiclenType) {
        this.idVehicleType = idVehicleType;
        this.vehiclenType = vehiclenType;
    }

    public int getIdVehicleType() {
        return idVehicleType;
    }

    public void setIdVehicleType(int idVehicleType) {
        this.idVehicleType = idVehicleType;
    }

    public String getVehiclenType() {
        return vehiclenType;
    }

    public void setVehiclenType(String vehiclenType) {
        this.vehiclenType = vehiclenType;
    }


    public String getVehicleMaxPeople() {
        return vehicleMaxPeople;
    }

    public void setVehicleMaxPeople(String vehicleMaxPeople) {
        this.vehicleMaxPeople = vehicleMaxPeople;
    }

    public String getVehiclePriceKm() {
        return vehiclePriceKm;
    }

    public void setVehiclePriceKm(String vehiclePriceKm) {
        this.vehiclePriceKm = vehiclePriceKm;
    }

    public String getVehiclePriceHour() {
        return vehiclePriceHour;
    }

    public void setVehiclePriceHour(String vehiclePriceHour) {
        this.vehiclePriceHour = vehiclePriceHour;
    }

    public String getVehicleDescription() {
        return vehicleDescription;
    }

    public void setVehicleDescription(String vehicleDescription) {
        this.vehicleDescription = vehicleDescription;
    }

    public String getVehicleMotorcycle() {
        return vehicleMotorcycle;
    }

    public void setVehicleMotorcycle(String vehicleMotorcycle) {
        this.vehicleMotorcycle = vehicleMotorcycle;
    }

    public String getVehicleContract() {
        return vehicleContract;
    }

    public void setVehicleContract(String vehicleContract) {
        this.vehicleContract = vehicleContract;
    }

    public String getIdStatusVehicleType() {
        return idStatusVehicleType;
    }

    public void setIdStatusVehicleType(String idStatusVehicleType) {
        this.idStatusVehicleType = idStatusVehicleType;
    }

    public String getConditionContract() {
        return conditionContract;
    }

    public void setConditionContract(String conditionContract) {
        this.conditionContract = conditionContract;
    }

    public String getVehiclePriceMin() {
        return vehiclePriceMin;
    }

    public void setVehiclePriceMin(String vehiclePriceMin) {
        this.vehiclePriceMin = vehiclePriceMin;
    }

    public String getVehiclePriceHourDR() {
        return vehiclePriceHourDR;
    }

    public void setVehiclePriceHourDR(String vehiclePriceHourDR) {
        this.vehiclePriceHourDR = vehiclePriceHourDR;
    }

    public String getVehiclePriceKmDR() {
        return vehiclePriceKmDR;
    }

    public void setVehiclePriceKmDR(String vehiclePriceKmDR) {
        this.vehiclePriceKmDR = vehiclePriceKmDR;
    }

    public String getVehiclePriceMinDR() {
        return vehiclePriceMinDR;
    }

    public void setVehiclePriceMinDR(String vehiclePriceMinDR) {
        this.vehiclePriceMinDR = vehiclePriceMinDR;
    }

    public String getDefaultVehicleType() {
        return defaultVehicleType;
    }

    public void setDefaultVehicleType(String defaultVehicleType) {
        this.defaultVehicleType = defaultVehicleType;
    }

    public String getIsFleet() {
        return isFleet;
    }

    public void setIsFleet(String isFleet) {
        this.isFleet = isFleet;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getVehicleMinHourService() {
        return vehicleMinHourService;
    }

    public void setVehicleMinHourService(String vehicleMinHourService) {
        this.vehicleMinHourService = vehicleMinHourService;
    }

    public String getDryLoadValue() {
        return dryLoadValue;
    }

    public void setDryLoadValue(String dryLoadValue) {
        this.dryLoadValue = dryLoadValue;
    }

    public String getRefrigeratedLoadValue() {
        return refrigeratedLoadValue;
    }

    public void setRefrigeratedLoadValue(String refrigeratedLoadValue) {
        this.refrigeratedLoadValue = refrigeratedLoadValue;
    }

    public String getLimitKmTraveledHr() {
        return limitKmTraveledHr;
    }

    public void setLimitKmTraveledHr(String limitKmTraveledHr) {
        this.limitKmTraveledHr = limitKmTraveledHr;
    }

    public String getVehicleValorMin() {
        return vehicleValorMin;
    }

    public void setVehicleValorMin(String vehicleValorMin) {
        this.vehicleValorMin = vehicleValorMin;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getKmEx() {
        return kmEx;
    }

    public void setKmEx(String kmEx) {
        this.kmEx = kmEx;
    }

    public String getPricePerKmEx() {
        return pricePerKmEx;
    }

    public void setPricePerKmEx(String pricePerKmEx) {
        this.pricePerKmEx = pricePerKmEx;
    }

    public String getPricePerMinutEx() {
        return pricePerMinutEx;
    }

    public void setPricePerMinutEx(String pricePerMinutEx) {
        this.pricePerMinutEx = pricePerMinutEx;
    }

    public String getIdIvaTypeKf() {
        return idIvaTypeKf;
    }

    public void setIdIvaTypeKf(String idIvaTypeKf) {
        this.idIvaTypeKf = idIvaTypeKf;
    }

    public String getIdBenefitKm() {
        return idBenefitKm;
    }

    public void setIdBenefitKm(String idBenefitKm) {
        this.idBenefitKm = idBenefitKm;
    }

    public List<BeneficioEntity> getListbenefitKm() {
        return listbenefitKm;
    }

    public void setListbenefitKm(List<BeneficioEntity> listbenefitKm) {
        this.listbenefitKm = listbenefitKm;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
