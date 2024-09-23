package com.example.apptransport.Fragments;

public interface ListenerFragmentClient {
    void showDialogTravelInfo();
    void showMessage(String mensaje,int status);
    void activarGps();
    void ocultarActivarGps();
    void btnFlotingVisible(boolean isVisible);
}
