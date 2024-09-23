package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyData {

    @Expose
    @SerializedName("originClientPactado")
    private List<CompanyDataOrigenPactado> originClientPactado;

    @Expose
    @SerializedName("acountcompany")
    private List<CompanyDataAccount> accountCompany;

    @Expose
    @SerializedName("companyParams")
    private CompanyParams companyParams;

    @Expose
    @SerializedName("listPointToPoint")
    private List<PointToPointItem>  listPointToPoint;


    public List<CompanyDataOrigenPactado> getOriginClientPactado() {
        return originClientPactado;
    }

    public void setOriginClientPactado(List<CompanyDataOrigenPactado> originClientPactado) {
        this.originClientPactado = originClientPactado;
    }

    public List<CompanyDataAccount> getAccountCompany() {
        return accountCompany;
    }

    public void setAccountCompany(List<CompanyDataAccount> accountCompany) {
        this.accountCompany = accountCompany;
    }

    public CompanyParams getCompanyParams() {
        if(companyParams!=null){
            return companyParams;
        }
        else{
            return new CompanyParams();
        }
    }

    public void setCompanyParams(CompanyParams companyParams) {
        this.companyParams = companyParams;
    }

    public List<PointToPointItem> getListPointToPoint() {
        return listPointToPoint;
    }

    public void setListPointToPoint(List<PointToPointItem> listPointToPoint) {
        this.listPointToPoint = listPointToPoint;
    }
}
