package com.example.apptransport.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 06/01/2017.
 */

public class InfoTravelEntityLite implements Serializable {

    @Expose
    @SerializedName("idTravel")
    public int idTravel;

    @Expose
    @SerializedName("isBenefitKmList")
    public int isBenefitKmList;

    @Expose
    @SerializedName("isBenefitKmClientList")
    public int isBenefitKmClientList;

    @Expose
    @SerializedName("idDriverOld")
    public int idDriverOld;

    @Expose
    @SerializedName("start")
    public int start;

    @Expose
    @SerializedName("idTypeTravelKf")
    public int idTypeTravelKf;

    @Expose
    @SerializedName("isFleetTravelAssistance")
    public int isFleetTravelAssistance;

    @Expose
    @SerializedName("isPointToPoint")
    public int isPointToPoint;

    @Expose
    @SerializedName("isConfirmAceptaByClient")
    public int isConfirmAceptaByClient;

    @Expose
    @SerializedName("MultiDestinationDesc")
    public String MultiDestinationDesc;

    @Expose
    @SerializedName("idSatatusTravel")
    public int idSatatusTravel;

    @Expose
    @SerializedName("phoneNumber")
    public String phoneNumber;

    @Expose
    @SerializedName("isProcesCurrentAcount")
    public int isProcesCurrentAcount;

    @Expose
    @SerializedName("isTravelByHour")
    public int isTravelByHour;

    @Expose
    @SerializedName("phoneNumberDriver")
    public String phoneNumberDriver;

    @Expose
    @SerializedName("department")
    public String department;

    @Expose
    @SerializedName("FLOOR")
    public String FLOOR;

    @Expose
    @SerializedName("nameStatusTravel")
    public String nameStatusTravel;

    @Expose
    @SerializedName("lonOrigin")
    public String lonOrigin;

    @Expose
    @SerializedName("latOrigin")
    public String latOrigin;

    @Expose
    @SerializedName("lot")
    public String lot;

    @Expose
    @SerializedName("distanceLabel")
    public String distanceLabel;


    @Expose
    @SerializedName("idStatusTravel")
    public int idStatusTravel;


    @Expose
    @SerializedName("lonDestination")
    public String lonDestination;

    @Expose
    @SerializedName("latDestination")
    public String latDestination;

    @Expose
    @SerializedName("amountCalculate")
    public String amountCalculate;

    @Expose
    @SerializedName("totalAmount")
    public String totalAmount;


    @Expose
    @SerializedName("infocar")
    public String infocar;


    @Expose
    @SerializedName("amountGps")
    public String amountGps;


    @Expose
    @SerializedName("codTravel")
    public String codTravel;

    @Expose
    @SerializedName("nameOrigin")
    public String nameOrigin;

    @Expose
    @SerializedName("pasajero")
    public String pasajero;

    @Expose
    @SerializedName("observationFromDriver")
    public String observationFromDriver;

    @Expose
    @SerializedName("nameDestination")
    public String nameDestination;

    @Expose
    @SerializedName("driver")
    public String driver;

    @Expose
    @SerializedName("client")
    public String client;

    @Expose
    @SerializedName("idClientKf")
    public int idClientKf;


    @Expose
    @SerializedName("classColorTwo")
    public String classColorTwo;

    @Expose
    @SerializedName("isTravelMultiOrigin")
    public String isTravelMultiOrigin;

    @Expose
    @SerializedName("isMultiDestination")
    public String isMultiDestination;


    @Expose
    @SerializedName("OriginMultipleDesc1")
    public String OriginMultipleDesc1;

    @Expose
    @SerializedName("OriginMultipleLat1")
    public String OriginMultipleLat1;

    @Expose
    @SerializedName("OriginMultipleLon1")
    public String OriginMultipleLon1;

    @Expose
    @SerializedName("OriginMultipleDesc2")
    public String OriginMultipleDesc2;

    @Expose
    @SerializedName("OriginMultipleLat2")
    public String OriginMultipleLat2;

    @Expose
    @SerializedName("OriginMultipleLon2")
    public String OriginMultipleLon2;

    @Expose
    @SerializedName("OriginMultipleDesc3")
    public String OriginMultipleDesc3;
    @Expose
    @SerializedName("OriginMultipleLat3")
    public String OriginMultipleLat3;

    @Expose
    @SerializedName("OriginMultipleLon3")
    public String OriginMultipleLon3;

    @Expose
    @SerializedName("OriginMultipleDesc4")
    public String OriginMultipleDesc4;

    @Expose
    @SerializedName("OriginMultipleLat4")
    public String OriginMultipleLat4;

    @Expose
    @SerializedName("OriginMultipleLon4")
    public String OriginMultipleLon4;

    @Expose
    @SerializedName("MultiDestination")
    public String MultiDestination;

    @Expose
    @SerializedName("idUserClient")
    public int idUserClient;

    @Expose
    @SerializedName("idUserDriver")
    public int idUserDriver;

    @Expose
    @SerializedName("isRoundTrip")
    public Boolean isRoundTrip;

    @Expose
    @SerializedName("isTravelSendMovil")
    public Boolean isTravelSendMovil;


    @Expose
    @SerializedName("isResignet")
    public int isResignet;



    @Expose
    @SerializedName("priceDitanceCompany")
    public double priceDitanceCompany;

    @Expose
    @SerializedName("priceMinSleepCompany")
    public double priceMinSleepCompany;


    @Expose
    @SerializedName("priceReturn")
    public double priceReturn;


    @Expose
    @SerializedName("priceHourDriverMultiLan")
    public double priceHourDriverMultiLan;


    @Expose
    @SerializedName("priceHour")
    public double priceHour;


    @Expose
    @SerializedName("priceContract")
    public double priceContract;

    @Expose
    @SerializedName("priceTravelSms")
    public double priceTravelSms;

    @Expose
    @SerializedName("benefitsFromKm")
    public double benefitsFromKm;

    @Expose
    @SerializedName("benefitsToKm")
    public double benefitsToKm;

    @Expose
    @SerializedName("benefitsPreceKm")
    public double benefitsPreceKm;


    @Expose
    @SerializedName("isTravelComany")
    public int isTravelComany;


    @Expose
    @SerializedName("isAceptReservationByDriver")
    public int isAceptReservationByDriver;

    @Expose
    @SerializedName("benefitsPerKm")
    public int benefitsPerKm;

    @Expose
    @SerializedName("amountOriginPac")
    public double amountOriginPac;


    @Expose
    @SerializedName("dateTravel")
    public String mdate;


    @Expose
    @SerializedName("domain")
    public String domain;


    @Expose
    @SerializedName("isPaymentCash")
    public int isPaymentCash;


    @Expose
    @SerializedName("idBenefitKmClientKf")
    public int idBenefitKmClientKf;

    @Expose
    @SerializedName("isConfirReservationFromWeb")
    public int isConfirReservationFromWeb;

    @Expose
    @SerializedName("isConfirTravelAppFromWeb")
    public int isConfirTravelAppFromWeb;


    @Expose
    @SerializedName("distanceSave")
    public double distanceSave;

    @Expose
    @SerializedName("pricePoint")
    public double pricePoint;


    @Expose
    @SerializedName("priceMinTravel")
    public double priceMinTravel;

    @Expose
    @SerializedName("Kmex")
    public double Kmex;

    @Expose
    @SerializedName("pricePerKmex")
    public double pricePerKmex;


    @Expose
    @SerializedName("passenger1")
    public String passenger1;

    @Expose
    @SerializedName("passenger2")
    public String passenger2;

    @Expose
    @SerializedName("passenger3")
    public String passenger3;

    @Expose
    @SerializedName("passenger4")
    public String passenger4;

    @Expose
    @SerializedName("reason")
    public String reason;

    @Expose
    @SerializedName("obsertavtionFlight")
    public String obsertavtionFlight;

    @Expose
    @SerializedName("sound")
    public String sound;

    @Expose
    @SerializedName("mOriginObs")
    private String mOriginObs;

    @Expose
    @SerializedName("isDriverClose")
    private String isDriverClose;

    @Expose
    @SerializedName("idDriverKf")
    public int idDriverKf;

    @Expose
    @SerializedName("minSlepNoAply")
    private int minSlepNoAply;

    @Expose
    @SerializedName("statusPayment")
    private String statusPayment;

    @Expose
    @SerializedName("clientPaymentStatus")
    private int clientPaymentStatus;


    @Expose
    @SerializedName("clienPaymentAmount")
    private Double clientPaymentAmount;

    @Expose
    @SerializedName("idReasonCancelKf")
    private int idReasonCancelKf;


    /*Constructors*/

    public InfoTravelEntityLite(){}

    /*Created by:   Sergio Franco
     * Date Created: 15/08/2019
     * Description:  Crea una entidad a partir de otra del tipo InfoTravelEntity
     * */
    public InfoTravelEntityLite(InfoTravelEntity in) {
        this.idTravel = in.idTravel;
        this.isBenefitKmList = in.isBenefitKmList;
        this.isBenefitKmClientList = in.isBenefitKmClientList;
        this.idDriverOld = in.idDriverOld;
        this.start = in.start;
        this.idTypeTravelKf = in.idTypeTravelKf;
        this.isFleetTravelAssistance = in.isFleetTravelAssistance;
        this.isPointToPoint = in.isPointToPoint;
        this.isConfirmAceptaByClient = in.isConfirmAceptaByClient;
        this.MultiDestinationDesc = in.MultiDestinationDesc;
        this.idSatatusTravel = in.idSatatusTravel;
        this.phoneNumber = in.phoneNumber;
        this.isProcesCurrentAcount = in.isProcesCurrentAcount;
        this.isTravelByHour = in.isTravelByHour;
        this.phoneNumberDriver = in.phoneNumberDriver;
        this.department = in.department;
        this.FLOOR = in.FLOOR;
        this.nameStatusTravel = in.nameStatusTravel;
        this.lonOrigin = in.lonOrigin;
        this.latOrigin = in.latOrigin;
        this.lot = in.lot;
        this.distanceLabel = in.distanceLabel;
        this.idStatusTravel = in.idStatusTravel;
        this.lonDestination = in.lonDestination;
        this.latDestination = in.latDestination;
        this.amountCalculate = in.amountCalculate;
        this.totalAmount = in.totalAmount;
        this.infocar = in.infocar;
        this.amountGps = in.amountGps;
        this.codTravel = in.codTravel;
        this.nameOrigin = in.nameOrigin;
        this.pasajero = in.pasajero;
        this.observationFromDriver = in.observationFromDriver;
        this.nameDestination = in.nameDestination;
        this.driver = in.driver;
        this.client = in.client;
        this.idClientKf = in.idClientKf;
        this.classColorTwo = in.classColorTwo;
        this.isTravelMultiOrigin = in.isTravelMultiOrigin;
        this.isMultiDestination = in.isMultiDestination;
        this.OriginMultipleDesc1 = in.OriginMultipleDesc1;
        this.OriginMultipleLat1 = in.OriginMultipleLat1;
        this.OriginMultipleLon1 = in.OriginMultipleLon1;
        this.OriginMultipleDesc2 = in.OriginMultipleDesc2;
        this.OriginMultipleLat2 = in.OriginMultipleLat2;
        this.OriginMultipleLon2 = in.OriginMultipleLon2;
        this.OriginMultipleDesc3 = in.OriginMultipleDesc3;
        this.OriginMultipleLat3 = in.OriginMultipleLat3;
        this.OriginMultipleLon3 = in.OriginMultipleLon3;
        this.OriginMultipleDesc4 = in.OriginMultipleDesc4;
        this.OriginMultipleLat4 = in.OriginMultipleLat4;
        this.OriginMultipleLon4 = in.OriginMultipleLon4;
        this.MultiDestination = in.MultiDestination;
        this.idUserClient = in.idUserClient;
        this.idUserDriver = in.idUserDriver;
        this.isRoundTrip = in.isRoundTrip==0?false:true;
        this.isTravelSendMovil = in.isTravelSendMovil;
        this.isResignet = in.isResignet;
        this.priceDitanceCompany = in.priceDitanceCompany;
        this.priceMinSleepCompany = in.priceMinSleepCompany;
        this.priceReturn = in.priceReturn;
        this.priceHourDriverMultiLan = in.priceHourDriverMultiLan;
        this.priceHour = in.priceHour;
        this.priceContract = in.priceContract;
        this.priceTravelSms = in.priceTravelSms;
        this.benefitsFromKm = in.benefitsFromKm;
        this.benefitsToKm = in.benefitsToKm;
        this.benefitsPreceKm = in.benefitsPreceKm;
        this.isTravelComany = in.isTravelComany;
        this.isAceptReservationByDriver = in.isAceptReservationByDriver;
        this.benefitsPerKm = in.benefitsPerKm;
        this.amountOriginPac = in.amountOriginPac;
        this.mdate = in.mdate;
        this.domain = in.domain;
        this.isPaymentCash = in.isPaymentCash;
        this.idBenefitKmClientKf = in.idBenefitKmClientKf;
        this.isConfirReservationFromWeb = in.isConfirReservationFromWeb;
        this.isConfirTravelAppFromWeb = in.isConfirTravelAppFromWeb;
        this.distanceSave = in.distanceSave;
        this.pricePoint = in.pricePoint;
        this.priceMinTravel = in.priceMinTravel;
        this.Kmex = in.Kmex;
        this.pricePerKmex = in.pricePerKmex;
        this.passenger1 = in.passenger1;
        this.passenger2 = in.passenger2;
        this.passenger3 = in.passenger3;
        this.passenger4 = in.passenger4;
        this.reason = in.reason;
        this.obsertavtionFlight = in.obsertavtionFlight;
        this.sound = in.sound;
        this.mOriginObs = in.mOriginObs;
        this.idDriverKf = in.getIdDriverKf();
        this.minSlepNoAply = in.getMinSlepNoAply();
        this.statusPayment = in.getStatusPayment();
        this.clientPaymentAmount=in.getClientPaymentAmount();
        this.clientPaymentStatus=in.getClientPaymentStatus();
        this.idReasonCancelKf=in.getIdReasonCancelKf();
    }

    /*End Constructors*/


    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getObsertavtionFlight() {
        return obsertavtionFlight;
    }

    public void setObsertavtionFlight(String obsertavtionFlight) {
        this.obsertavtionFlight = obsertavtionFlight;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getPriceMinTravel() {
        return priceMinTravel;
    }

    public void setPriceMinTravel(double priceMinTravel) {
        this.priceMinTravel = priceMinTravel;
    }

    public int getIsPaymentCash() {
        return isPaymentCash;
    }

    public void setIsPaymentCash(int isPaymentCash) {
        this.isPaymentCash = isPaymentCash;
    }

    public double getDistanceSave() {
        return distanceSave;
    }

    public void setDistanceSave(double distanceSave) {
        this.distanceSave = distanceSave;
    }

    public int getPaymentCash() {
        return isPaymentCash;
    }

    public void setPaymentCash(int paymentCash) {
        isPaymentCash = paymentCash;
    }

    public double getAmountOriginPac() {
        return amountOriginPac;
    }

    public void setAmountOriginPac(double amountOriginPac) {
        this.amountOriginPac = amountOriginPac;
    }

    public int getIsProcesCurrentAcount() {
        return isProcesCurrentAcount;
    }

    public void setIsProcesCurrentAcount(int isProcesCurrentAcount) {
        this.isProcesCurrentAcount = isProcesCurrentAcount;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public int getIsAceptReservationByDriver() {
        return isAceptReservationByDriver;
    }

    public void setIsAceptReservationByDriver(int isAceptReservationByDriver) {
        this.isAceptReservationByDriver = isAceptReservationByDriver;
    }

    public int getIsTravelComany() {
        return isTravelComany;
    }

    public void setIsTravelComany(int isTravelComany) {
        this.isTravelComany = isTravelComany;
    }

    public double getPriceDitanceCompany() {
        return priceDitanceCompany;
    }

    public void setPriceDitanceCompany(double priceDitanceCompany) {
        this.priceDitanceCompany = priceDitanceCompany;
    }

    public double getPriceMinSleepCompany() {
        return priceMinSleepCompany;
    }

    public void setPriceMinSleepCompany(double priceMinSleepCompany) {
        this.priceMinSleepCompany = priceMinSleepCompany;
    }

    public double getPriceReturn() {
        return priceReturn;
    }

    public void setPriceReturn(double priceReturn) {
        this.priceReturn = priceReturn;
    }

    public double getPriceHourDriverMultiLan() {
        return priceHourDriverMultiLan;
    }

    public void setPriceHourDriverMultiLan(double priceHourDriverMultiLan) {
        this.priceHourDriverMultiLan = priceHourDriverMultiLan;
    }

    public double getPriceContract() {
        return priceContract;
    }

    public void setPriceContract(double priceContract) {
        this.priceContract = priceContract;
    }

    public double getPriceTravelSms() {
        return priceTravelSms;
    }

    public void setPriceTravelSms(double priceTravelSms) {
        this.priceTravelSms = priceTravelSms;
    }

    public Boolean getTravelSendMovil() {
        return isTravelSendMovil;
    }

    public void setTravelSendMovil(Boolean travelSendMovil) {
        isTravelSendMovil = travelSendMovil;
    }

    public Boolean getRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(Boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public int getIdUserDriver() {
        return idUserDriver;
    }

    public void setIdUserDriver(int idUserDriver) {
        this.idUserDriver = idUserDriver;
    }

    public String getObservationFromDriver() {
        return observationFromDriver;
    }

    public void setObservationFromDriver(String observationFromDriver) {
        this.observationFromDriver = observationFromDriver;
    }

    public void setMultiDestination(String multiDestination) {
        MultiDestination = multiDestination;
    }

    public String getMultiDestination() {
        return MultiDestination;
    }

    public String getOriginMultipleDesc1() {
        return OriginMultipleDesc1;
    }

    public void setOriginMultipleDesc1(String originMultipleDesc1) {
        OriginMultipleDesc1 = originMultipleDesc1;
    }

    public String getOriginMultipleLat1() {
        return OriginMultipleLat1;
    }

    public void setOriginMultipleLat1(String originMultipleLat1) {
        OriginMultipleLat1 = originMultipleLat1;
    }

    public String getOriginMultipleLon1() {
        return OriginMultipleLon1;
    }

    public void setOriginMultipleLon1(String originMultipleLon1) {
        OriginMultipleLon1 = originMultipleLon1;
    }

    public String getOriginMultipleDesc2() {
        return OriginMultipleDesc2;
    }

    public void setOriginMultipleDesc2(String originMultipleDesc2) {
        OriginMultipleDesc2 = originMultipleDesc2;
    }

    public String getOriginMultipleLat2() {
        return OriginMultipleLat2;
    }

    public void setOriginMultipleLat2(String originMultipleLat2) {
        OriginMultipleLat2 = originMultipleLat2;
    }

    public String getOriginMultipleLon2() {
        return OriginMultipleLon2;
    }

    public void setOriginMultipleLon2(String originMultipleLon2) {
        OriginMultipleLon2 = originMultipleLon2;
    }

    public String getOriginMultipleDesc3() {
        return OriginMultipleDesc3;
    }

    public void setOriginMultipleDesc3(String originMultipleDesc3) {
        OriginMultipleDesc3 = originMultipleDesc3;
    }

    public String getOriginMultipleLat3() {
        return OriginMultipleLat3;
    }

    public void setOriginMultipleLat3(String originMultipleLat3) {
        OriginMultipleLat3 = originMultipleLat3;
    }

    public String getOriginMultipleLon3() {
        return OriginMultipleLon3;
    }

    public void setOriginMultipleLon3(String originMultipleLon3) {
        OriginMultipleLon3 = originMultipleLon3;
    }

    public String getOriginMultipleDesc4() {
        return OriginMultipleDesc4;
    }

    public void setOriginMultipleDesc4(String originMultipleDesc4) {
        OriginMultipleDesc4 = originMultipleDesc4;
    }

    public String getOriginMultipleLat4() {
        return OriginMultipleLat4;
    }

    public void setOriginMultipleLat4(String originMultipleLat4) {
        OriginMultipleLat4 = originMultipleLat4;
    }

    public String getOriginMultipleLon4() {
        return OriginMultipleLon4;
    }

    public void setOriginMultipleLon4(String originMultipleLon4) {
        OriginMultipleLon4 = originMultipleLon4;
    }


    public String getIsTravelMultiOrigin() {
        return isTravelMultiOrigin;
    }

    public void setIsTravelMultiOrigin(String isTravelMultiOrigin) {
        this.isTravelMultiOrigin = isTravelMultiOrigin;
    }

    public String getIsMultiDestination() {
        return isMultiDestination;
    }

    public void setIsMultiDestination(String isMultiDestination) {
        this.isMultiDestination = isMultiDestination;
    }

    public String getPasajero() {
        return pasajero;
    }

    public void setPasajero(String pasajero) {
        this.pasajero = pasajero;
    }

    public int getIdClientKf() {
        return idClientKf;
    }

    public void setIdClientKf(int idClientKf) {
        this.idClientKf = idClientKf;
    }

    public String getClassColorTwo() {
        return classColorTwo;
    }

    public void setClassColorTwo(String classColorTwo) {
        this.classColorTwo = classColorTwo;
    }

    public String getNameDestination() {
        return nameDestination;
    }

    public void setNameDestination(String nameDestination) {
        this.nameDestination = nameDestination;
    }

    public int getIdSatatusTravel() {
        return idSatatusTravel;
    }

    public void setIdSatatusTravel(int idSatatusTravel) {
        this.idSatatusTravel = idSatatusTravel;
    }

    public String getNameStatusTravel() {
        return nameStatusTravel;
    }

    public void setNameStatusTravel(String nameStatusTravel) {
        this.nameStatusTravel = nameStatusTravel;
    }

    public int getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(int idTravel) {
        this.idTravel = idTravel;
    }

    public String getCodTravel() {
        return codTravel;
    }

    public void setCodTravel(String codTravel) {
        this.codTravel = codTravel;
    }

    public String getNameOrigin() {
        return nameOrigin;
    }

    public void setNameOrigin(String nameOrigin) {
        this.nameOrigin = nameOrigin;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }


    public String getLonOrigin() {
        return lonOrigin;
    }

    public void setLonOrigin(String lonOrigin) {
        this.lonOrigin = lonOrigin;
    }

    public String getLatOrigin() {
        return latOrigin;
    }

    public void setLatOrigin(String latOrigin) {
        this.latOrigin = latOrigin;
    }

    public String getLonDestination() {
        return lonDestination;
    }

    public void setLonDestination(String lonDestination) {
        this.lonDestination = lonDestination;
    }

    public String getLatDestination() {
        return latDestination;
    }

    public void setLatDestination(String latDestination) {
        this.latDestination = latDestination;
    }

    public String getAmountCalculate() {
        return amountCalculate;
    }

    public void setAmountCalculate(String amountCalculate) {
        this.amountCalculate = amountCalculate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAmountGps() {
        return amountGps;
    }

    public void setAmountGps(String amountGps) {
        this.amountGps = amountGps;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDistanceLabel() {
        return distanceLabel;
    }

    public void setDistanceLabel(String distanceLabel) {
        this.distanceLabel = distanceLabel;
    }

    public int getIdUserClient() {
        return idUserClient;
    }

    public void setIdUserClient(int idUserClient) {
        this.idUserClient = idUserClient;
    }

    public double getBenefitsFromKm() {
        return benefitsFromKm;
    }

    public void setBenefitsFromKm(double benefitsFromKm) {
        this.benefitsFromKm = benefitsFromKm;
    }

    public double getBenefitsToKm() {
        return benefitsToKm;
    }

    public void setBenefitsToKm(double benefitsToKm) {
        this.benefitsToKm = benefitsToKm;
    }

    public double getBenefitsPreceKm() {
        return benefitsPreceKm;
    }

    public void setBenefitsPreceKm(double benefitsPreceKm) {
        this.benefitsPreceKm = benefitsPreceKm;
    }

    public int getBenefitsPerKm() {
        return benefitsPerKm;
    }

    public void setBenefitsPerKm(int benefitsPerKm) {
        this.benefitsPerKm = benefitsPerKm;
    }

    public String getMultiDestinationDesc() {
        return MultiDestinationDesc;
    }

    public void setMultiDestinationDesc(String multiDestinationDesc) {
        MultiDestinationDesc = multiDestinationDesc;
    }

    public int getIsConfirReservationFromWeb() {
        return isConfirReservationFromWeb;
    }

    public void setIsConfirReservationFromWeb(int isConfirReservationFromWeb) {
        this.isConfirReservationFromWeb = isConfirReservationFromWeb;
    }




    public int getIdBenefitKmClientKf() {
        return idBenefitKmClientKf;
    }

    public void setIdBenefitKmClientKf(int idBenefitKmClientKf) {
        this.idBenefitKmClientKf = idBenefitKmClientKf;
    }

    public int getIsResignet() {
        return isResignet;
    }

    public void setIsResignet(int isResignet) {
        this.isResignet = isResignet;
    }

    public int getIdDriverOld() {
        return idDriverOld;
    }

    public void setIdDriverOld(int idDriverOld) {
        this.idDriverOld = idDriverOld;
    }


    public String getPhoneNumberDriver() {
        return phoneNumberDriver;
    }

    public void setPhoneNumberDriver(String phoneNumberDriver) {
        this.phoneNumberDriver = phoneNumberDriver;
    }

    public String getInfocar() {
        return infocar;
    }

    public void setInfocar(String infocar) {
        this.infocar = infocar;
    }


    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public String getPassenger4() {
        return passenger4;
    }

    public void setPassenger4(String passenger4) {
        this.passenger4 = passenger4;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }


    public int getIsFleetTravelAssistance() {
        return isFleetTravelAssistance;
    }

    public void setIsFleetTravelAssistance(int isFleetTravelAssistance) {
        this.isFleetTravelAssistance = isFleetTravelAssistance;
    }

    public int getIdTypeTravelKf() {
        return idTypeTravelKf;
    }

    public void setIdTypeTravelKf(int idTypeTravelKf) {
        this.idTypeTravelKf = idTypeTravelKf;
    }

    public int getIsConfirmAceptaByClient() {
        return isConfirmAceptaByClient;
    }

    public void setIsConfirmAceptaByClient(int isConfirmAceptaByClient) {
        this.isConfirmAceptaByClient = isConfirmAceptaByClient;
    }

    public int getIdStatusTravel() {
        return idStatusTravel;
    }

    public void setIdStatusTravel(int idStatusTravel) {
        this.idStatusTravel = idStatusTravel;
    }

    public int getIsConfirTravelAppFromWeb() {
        return isConfirTravelAppFromWeb;
    }

    public void setIsConfirTravelAppFromWeb(int isConfirTravelAppFromWeb) {
        this.isConfirTravelAppFromWeb = isConfirTravelAppFromWeb;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFLOOR() {
        return FLOOR;
    }

    public void setFLOOR(String FLOOR) {
        this.FLOOR = FLOOR;
    }

    public int getIsPointToPoint() {
        return isPointToPoint;
    }

    public void setIsPointToPoint(int isPointToPoint) {
        this.isPointToPoint = isPointToPoint;
    }

    public double getPricePoint() {
        return pricePoint;
    }

    public void setPricePoint(double pricePoint) {
        this.pricePoint = pricePoint;
    }

    public int getIsTravelByHour() {
        return isTravelByHour;
    }

    public void setIsTravelByHour(int isTravelByHour) {
        this.isTravelByHour = isTravelByHour;
    }

    public double getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(double priceHour) {
        this.priceHour = priceHour;
    }

    public double getKmex() {
        return Kmex;
    }

    public void setKmex(double kmex) {
        Kmex = kmex;
    }

    public double getPricePerKmex() {
        return pricePerKmex;
    }

    public void setPricePerKmex(double pricePerKmex) {
        this.pricePerKmex = pricePerKmex;
    }

    public int getIsBenefitKmList() {
        return isBenefitKmList;
    }

    public void setIsBenefitKmList(int isBenefitKmList) {
        this.isBenefitKmList = isBenefitKmList;
    }

    public int getIsBenefitKmClientList() {
        return isBenefitKmClientList;
    }

    public void setIsBenefitKmClientList(int isBenefitKmClientList) {
        this.isBenefitKmClientList = isBenefitKmClientList;
    }

    public String makeJson(){
        Gson gson= new Gson();
        String _jsonstring= gson.toJson(this);
        return  _jsonstring;
    }


    public String getmOriginObs() {
        return mOriginObs;
    }

    public void setmOriginObs(String mOriginObs) {
        this.mOriginObs = mOriginObs;
    }

    public String getIsDriverClose() {
        return isDriverClose;
    }

    public void setIsDriverClose(String isDriverClose) {
        this.isDriverClose = isDriverClose;
    }

    public int getIdDriverKf() {
        return idDriverKf;
    }

    public void setIdDriverKf(int idDriverKf) {
        this.idDriverKf = idDriverKf;
    }

    public int getMinSlepNoAply() {
        return minSlepNoAply;
    }

    public void setMinSlepNoAply(int minSlepNoAply) {
        this.minSlepNoAply = minSlepNoAply;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    public int getClientPaymentStatus() {
        return clientPaymentStatus;
    }

    public void setClientPaymentStatus(int clientPaymentStatus) {
        this.clientPaymentStatus = clientPaymentStatus;
    }

    public Double getClientPaymentAmount() {
        return clientPaymentAmount;
    }

    public void setClientPaymentAmount(Double clientPaymentAmount) {
        this.clientPaymentAmount = clientPaymentAmount;
    }

    public int getIdReasonCancelKf() {
        return idReasonCancelKf;
    }

    public void setIdReasonCancelKf(int idReasonCancelKf) {
        this.idReasonCancelKf = idReasonCancelKf;
    }
}
