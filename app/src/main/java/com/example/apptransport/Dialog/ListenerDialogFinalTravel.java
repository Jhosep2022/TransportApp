package com.example.apptransport.Dialog;

public interface ListenerDialogFinalTravel {
    void finalizarViajeEfectivo(double montoTotal,double peajeMonto,double estacionamientoMonto, int waitingTime, double waitingPrice);
    void finalizarViajeTarjeta(double montoTotal,double peajeMonto,double estacionamientoMonto, int waitingTime, double waitingPrice, boolean mustPayClient);
    void finalizarViajeFirma(double montoTotal,double peajeMonto,double estacionamientoMonto, int waitingTime, double waitingPrice);
    void finalizarViajeWeb(double montoTotal,double peajeMonto,double estacionamientoMonto, int waitingTime, double waitingPrice);

}
