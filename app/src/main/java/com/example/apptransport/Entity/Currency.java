package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency {


    @Expose
    @SerializedName("currency")
    public String currency;

    @Expose
    @SerializedName("curCode")
    public String curCode;

    @Expose
    @SerializedName("symbol")
    public String symbol;

    @Expose
    @SerializedName("fiscal")
    public String fiscal;

    @Expose
    @SerializedName("format")
    public String format;

    public Currency() {

    }
    public Currency(String currency, String curCode, String symbol, String fiscal, String format) {
        this.currency = currency;
        this.curCode = curCode;
        this.symbol = symbol;
        this.fiscal = fiscal;
        this.format = format;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFiscal() {
        return fiscal;
    }

    public void setFiscal(String fiscal) {
        this.fiscal = fiscal;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
