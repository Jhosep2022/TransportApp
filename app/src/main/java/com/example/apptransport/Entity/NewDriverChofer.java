package com.example.apptransport.Entity;

public class NewDriverChofer {

    private boolean nombre;
    private boolean apellido;
    private boolean telefono;
    private boolean numero;

    private String nameTagDNI;
    private String namelabel;
    private String lastnamelabel;
    private String numberlabel;
    private String nameTagPhoto;
    private String nameTagLicense;
    private String nameTagVehicle;
    private String nameTagPlate;
    private String nameTagPoliceRecord;

    public NewDriverChofer() {
    }

    public NewDriverChofer(boolean nombre, boolean apellido, boolean telefono, boolean numero) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.numero = numero;
    }

    public boolean isNombre() {
        return nombre;
    }

    public void setNombre(boolean nombre) {
        this.nombre = nombre;
    }

    public boolean isApellido() {
        return apellido;
    }

    public void setApellido(boolean apellido) {
        this.apellido = apellido;
    }

    public boolean isTelefono() {
        return telefono;
    }

    public void setTelefono(boolean telefono) {
        this.telefono = telefono;
    }

    public boolean isNumero() {
        return numero;
    }

    public void setNumero(boolean numero) {
        this.numero = numero;
    }

    public String getNameTagDNI() {
        return nameTagDNI;
    }

    public void setNameTagDNI(String nameTagDNI) {
        this.nameTagDNI = nameTagDNI;
    }

    public String getNamelabel() {
        return namelabel;
    }

    public void setNamelabel(String namelabel) {
        this.namelabel = namelabel;
    }


    public String getLastNamelabel() {
        return lastnamelabel;
    }

    public void setLastNamelabel(String namelabel) {
        this.lastnamelabel = namelabel;
    }


    public String getNumberlabel() {
        return numberlabel;
    }

    public void setNumberlabel(String numberlabel) {
        this.numberlabel = numberlabel;
    }

    public String getNameTagPhoto() {
        return nameTagPhoto;
    }

    public void setNameTagPhoto(String nameTagPhoto) {
        this.nameTagPhoto = nameTagPhoto;
    }

    public String getNameTagLicense() {
        return nameTagLicense;
    }

    public void setNameTagLicense(String nameTagLicense) {
        this.nameTagLicense = nameTagLicense;
    }

    public String getNameTagVehicle() {
        return nameTagVehicle;
    }

    public void setNameTagVehicle(String nameTagVehicle) {
        this.nameTagVehicle = nameTagVehicle;
    }

    public String getNameTagPlate() {
        return nameTagPlate;
    }

    public void setNameTagPlate(String nameTagPlate) {
        this.nameTagPlate = nameTagPlate;
    }

    public String getNameTagPoliceRecord() {
        return nameTagPoliceRecord;
    }

    public void setNameTagPoliceRecord(String nameTagPoliceRecord) {
        this.nameTagPoliceRecord = nameTagPoliceRecord;
    }
}
