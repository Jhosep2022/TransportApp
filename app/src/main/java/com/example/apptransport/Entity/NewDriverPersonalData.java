package com.example.apptransport.Entity;

public class NewDriverPersonalData {
    public String nombre;
    public String apellido;
    public String telefono;
    public String email;
    public String nroChofer;
    public String password;
    public String socialId;

    public NewDriverPersonalData(){
        nombre="";
        apellido="";
        telefono="";
        email="";
        nroChofer="";
        password="";
        socialId="";
    }

    public NewDriverPersonalData(String nombre, String apellido, String telefono, String email, String nroChofer, String password, String socialId)
    {
        this.nombre=nombre;
        this.apellido=apellido;
        this.telefono=telefono;
        this.email=email;
        this.nroChofer=nroChofer;
        this.password=password;
        this.socialId=socialId;
    }

}
