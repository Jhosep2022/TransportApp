package com.example.apptransport.Entity;

public class PriceAndIsMinimum {
    public double amount;
    public boolean isMinimum;
    public double amountOriginPac;

    public PriceAndIsMinimum(){
        amount=0d;
        isMinimum=false;
        amountOriginPac=0d;
    }

    public PriceAndIsMinimum(double _amount, boolean _isMinimum, double _amountOriginPac)
    {
        amount=_amount;
        isMinimum=_isMinimum;
        amountOriginPac=_amountOriginPac;
    }
}
