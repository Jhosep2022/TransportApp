package com.example.apptransport.Entity;

import android.graphics.Bitmap;

public class NewDriverPhotos {
    public Bitmap bmpFotoDelConductor;
    public Bitmap bmpLicenciaDelConductor;
    public Bitmap bmpFotoVehiculo;
    public Bitmap bmpFotoChapaVehiculo;
    public Bitmap bmpFotoAntecedentesPoliciales;
    public String isChecked;

    public NewDriverPhotos()
    {
        bmpFotoDelConductor = null;
        bmpLicenciaDelConductor = null;
        bmpFotoVehiculo = null;
        bmpFotoChapaVehiculo = null;
        bmpFotoAntecedentesPoliciales= null;
        this.isChecked="0";
    }

    public NewDriverPhotos(Bitmap _bmpFotoDelConductor, Bitmap _bmpLicenciaDelConductor, Bitmap _bmpFotoVehiculo, Bitmap _bmpFotoChapaVehiculo, Bitmap _bmpFotoAntecedentesPoliciales, String isChecked)
    {
        bmpFotoDelConductor = _bmpFotoDelConductor;
        bmpLicenciaDelConductor = _bmpLicenciaDelConductor;
        bmpFotoVehiculo = _bmpFotoVehiculo;
        bmpFotoChapaVehiculo = _bmpFotoChapaVehiculo;
        bmpFotoAntecedentesPoliciales= _bmpFotoAntecedentesPoliciales;
        this.isChecked=isChecked;
    }
}

