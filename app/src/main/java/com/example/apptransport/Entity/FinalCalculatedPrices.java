package com.example.apptransport.Entity;

public class FinalCalculatedPrices {

    private int isPrefinish;
    private Double kilometrosIda;
    private Double kilometrosVuelta;
    private double totalamount;
    private int timeSleepGps;
    private double amountTimeSleep;
    private double amounttoll;
    private double amountParking;
    private String notaOperador;
    private double priceFleet;
    private double bajadaDeBandera;
    private double amountCalculateGps;
    private String timeElpasedInTravelByHour;
    private boolean isMinimumPrice;
    private double amountOriginPac;


    public FinalCalculatedPrices(){
        isPrefinish=0;
        kilometrosIda =0d;
        kilometrosVuelta=0d;
        totalamount=0d;
        timeSleepGps =0;
        amountTimeSleep =0d;
        amounttoll=0d;
        amountParking=0d;
        notaOperador="";
        priceFleet=0d;
        bajadaDeBandera=0d;
        amountCalculateGps=0d;
        timeElpasedInTravelByHour="";
        isMinimumPrice=false;
        amountOriginPac=0d;
    }


    public int getIsPrefinish() {
        return isPrefinish;
    }

    public void setIsPrefinish(int isPrefinish) {
        this.isPrefinish = isPrefinish;
    }

    public Double getKilometrosIda() {
        return kilometrosIda;
    }

    public void setKilometrosIda(Double kilometrosIda) {
        this.kilometrosIda = kilometrosIda;
    }

    public Double getKilometrosVuelta() {
        return kilometrosVuelta;
    }

    public void setKilometrosVuelta(Double kilometrosVuelta) {
        this.kilometrosVuelta = kilometrosVuelta;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public int getTimeSleepGps() {
        return timeSleepGps;
    }

    public void setTimeSleepGps(int timeSleepGps) {
        this.timeSleepGps = timeSleepGps;
    }

    public double getAmountTimeSleep() {
        return amountTimeSleep;
    }

    public void setAmountTimeSleep(double amountTimeSleep) {
        this.amountTimeSleep = amountTimeSleep;
    }

    public double getAmounttoll() {
        return amounttoll;
    }

    public void setAmounttoll(double amounttoll) {
        this.amounttoll = amounttoll;
    }

    public double getAmountParking() {
        return amountParking;
    }

    public void setAmountParking(double amountParking) {
        this.amountParking = amountParking;
    }

    public String getNotaOperador() {
        return notaOperador;
    }

    public void setNotaOperador(String notaOperador) {
        this.notaOperador = notaOperador;
    }

    public double getPriceFleet() {
        return priceFleet;
    }

    public void setPriceFleet(double priceFleet) {
        this.priceFleet = priceFleet;
    }

    public double getBajadaDeBandera() {
        return bajadaDeBandera;
    }

    public void setBajadaDeBandera(double bajadaDeBandera) {
        this.bajadaDeBandera = bajadaDeBandera;
    }

    public double getAmountCalculateGps() {
        return amountCalculateGps;
    }

    public void setAmountCalculateGps(double amountCalculateGps) {
        this.amountCalculateGps = amountCalculateGps;
    }

    public String getTimeElpasedInTravelByHour() {
        return timeElpasedInTravelByHour;
    }

    public void setTimeElpasedInTravelByHour(String timeElpasedInTravelByHour) {
        this.timeElpasedInTravelByHour = timeElpasedInTravelByHour;
    }

    public boolean isMinimumPrice() {
        return isMinimumPrice;
    }

    public void setMinimumPrice(boolean minimumPrice) {
        isMinimumPrice = minimumPrice;
    }

    public double getAmountOriginPac() {
        return amountOriginPac;
    }

    public void setAmountOriginPac(double amountOriginPac) {
        this.amountOriginPac = amountOriginPac;
    }
}
