package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by usario on 2/8/2017.
 */

public class ClientEntityAdd {


    @Expose
    @SerializedName("dniClient")
    public String dniClient;

    @Expose
    @SerializedName("phoneClient")
    public String phoneClient;

    @Expose
    @SerializedName("firtNameClient")
    public String firtNameClient;

    @Expose
    @SerializedName("lastNameClient")
    public String lastNameClient;

    @Expose
    @SerializedName("mailClient")
    public String mailClient;

    @Expose
    @SerializedName("passClient")
    public String passClient;

    @Expose
    @SerializedName("idTypeClient")
    public int idTypeClient;


    @Expose
    @SerializedName("idCompanyAcount")
    public Integer idCompanyAcount;


    @Expose
    @SerializedName("phone")
    public String phone;


    @Expose
    @SerializedName("idCostCenter")
    public Integer idCostCenter;



    @Expose
    @SerializedName("idCompanyKf")
    public Integer idCompanyKf;

    @Expose
    @SerializedName("firstNameUser")
    private String firstNameUser;

    @Expose
    @SerializedName("lastNameUser")
    private String lastNameUser;

    @Expose
    @SerializedName("emailUser")
    private String emailUser;

    @Expose
    @SerializedName("socialid")
    private String socialid;

    @Expose
    @SerializedName("acceptedTerms")
    private String acceptedTerms;

    public int getIdCompanyKf() {
        return idCompanyKf;
    }

    public void setIdCompanyKf(Integer idCompanyKf) {
        this.idCompanyKf = idCompanyKf;
    }

    public int getIdCompanyAcount() {
        return idCompanyAcount;
    }

    public void setIdCompanyAcount(Integer idCompanyAcount) {
        this.idCompanyAcount = idCompanyAcount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdCostCenter() {
        return idCostCenter;
    }

    public void setIdCostCenter(Integer idCostCenter) {
        this.idCostCenter = idCostCenter;
    }

    public String getFirtNameClient() {
        return firtNameClient;
    }

    public void setFirtNameClient(String firtNameClient) {
        this.firtNameClient = firtNameClient;
    }

    public String getLastNameClient() {
        return lastNameClient;
    }

    public void setLastNameClient(String lastNameClient) {
        this.lastNameClient = lastNameClient;
    }

    public String getMailClient() {
        return mailClient;
    }

    public void setMailClient(String mailClient) {
        this.mailClient = mailClient;
    }

    public String getPassClient() {
        return passClient;
    }

    public void setPassClient(String passClient) {
        this.passClient = passClient;
    }

    public int getIdTypeClient() {
        return idTypeClient;
    }

    public void setIdTypeClient(int idTypeClient) {
        this.idTypeClient = idTypeClient;
    }

    public ClientEntityAdd(String firtNameClient, String lastNameClient, String mailClient, String passClient, int idTypeClient, Integer idCompanyAcount, String phone,
                           Integer idCostCenter,Integer idCompanyKf, String dniClient, String socialid, String acceptedTerms) {
        this.firtNameClient = firtNameClient;
        this.lastNameClient = lastNameClient;
        this.mailClient = mailClient;
        this.passClient = passClient;
        this.idTypeClient = idTypeClient;
        this.idCompanyAcount = idCompanyAcount;
        this.phone = phone;
        this.phoneClient = phone;
        this.idCostCenter = idCostCenter;
        this.idCompanyKf = idCompanyKf;
        this.dniClient=dniClient;
        this.socialid=socialid;
        this.acceptedTerms=acceptedTerms;

    }

    public String getDniClient() {
        return dniClient;
    }

    public void setDniClient(String dniClient) {
        this.dniClient = dniClient;
    }

    public String getPhoneClient() {
        return phoneClient;
    }

    public void setPhoneClient(String phoneClient) {
        this.phoneClient = phoneClient;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public void setFirstNameUser(String firstNameUser) {
        this.firstNameUser = firstNameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailuser(String emailuser) {
        this.emailUser = emailuser;
    }

    public String getSocialid() {
        return socialid;
    }

    public void setSocialid(String socialid) {
        this.socialid = socialid;
    }

    public String getAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(String acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}
