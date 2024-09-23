package com.example.apptransport.Entity;

public class NewDriverVehicleData {
    public Integer idMarca;
    public Integer idModel;
    public Integer  idCategoria;
    public String dominio;

    public NewDriverVehicleData(){
        idMarca=null;
        idModel=null;
        idCategoria=null;
        dominio="";
    }

    public NewDriverVehicleData(Integer idMarca, Integer idModel, Integer idCategoria, String dominio){
        this.idMarca=idMarca;
        this.idModel = idModel;
        this.idCategoria = idCategoria;
        this.dominio=dominio;
    }
}
