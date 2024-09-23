package com.example.apptransport.Entity;

import com.apreciasoft.mobile.asremis.Activity.NewDriverActivity;

public class NewDriverOptionalValueData {

    private NewDriverChofer chofer;
    private NewDriverVehiculo vehiculo;

    public NewDriverOptionalValueData() {
    }

    public NewDriverOptionalValueData(NewDriverChofer chofer, NewDriverVehiculo vehiculo) {
        this.chofer = chofer;
        this.vehiculo = vehiculo;
    }

    public NewDriverChofer getChofer() {
        return chofer;
    }

    public void setChofer(NewDriverChofer chofer) {
        this.chofer = chofer;
    }

    public NewDriverVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(NewDriverVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}
