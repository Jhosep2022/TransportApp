package com.example.apptransport.Fragments;

import com.apreciasoft.mobile.asremis.Entity.NewDriverPersonalData;
import com.apreciasoft.mobile.asremis.Entity.NewDriverPhotos;
import com.apreciasoft.mobile.asremis.Entity.NewDriverVehicleData;

public interface ListenerRegisterChofer {
    void btnSiguiente(int pos);
    void btnAtras(int pos);

    void savePersonalData(NewDriverPersonalData personalData);
    void saveVehicle(NewDriverVehicleData vehicleData);
    void btnRegisterDriver(NewDriverPhotos photos);


}
