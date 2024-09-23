package com.example.apptransport.Fragments;

import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;

public interface ListenerFragmentChofer {
    void showDialogTravelInfoChofer(InfoTravelEntity currentTravelEntity);
    void showMessage(String mensaje, int status);
    void activarServicio();
    void ocultarActivarGPS();
    void activarGps();
}
