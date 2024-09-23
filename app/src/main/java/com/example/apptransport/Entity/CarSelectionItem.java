package com.example.apptransport.Entity;

import com.akexorcist.googledirection.model.Vehicle;

import java.util.List;

public class CarSelectionItem {
    private boolean isSelected;
    private String name;
    private String price;
    private Integer typeVehicle;
    private String cantPassengers;
    private VehicleType vehicleType;
    private double amountOriginPac;
    private int order;
    private List<BeneficioEntity> listbenefitKm;

    public CarSelectionItem() {
    }

    public CarSelectionItem(boolean isSelected, String name, Integer type, String price, String vehicleMaxPeople, VehicleType vehicleType, double amountOriginPac, int order, List<BeneficioEntity> listbenefitKm) {
        this.isSelected = isSelected;
        this.name = name;
        this.price = price;
        this.typeVehicle=type;
        this.cantPassengers= vehicleMaxPeople;
        this.vehicleType = vehicleType;
        this.amountOriginPac=amountOriginPac;
        this.order=order;
        this.listbenefitKm=listbenefitKm;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name !=null?name:"";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price!=null ? price: "";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(Integer typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public String getCantPassengers() {
        return cantPassengers!=null ?cantPassengers: "";
    }

    public void setCantPassengers(String cantPassengers) {
        this.cantPassengers = cantPassengers;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getAmountOriginPac() {
        return amountOriginPac;
    }

    public void setAmountOriginPac(double amountOriginPac) {
        this.amountOriginPac = amountOriginPac;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<BeneficioEntity> getListbenefitKm() {
        return listbenefitKm;
    }

    public void setListbenefitKm(List<BeneficioEntity> listbenefitKm) {
        this.listbenefitKm = listbenefitKm;
    }
}
