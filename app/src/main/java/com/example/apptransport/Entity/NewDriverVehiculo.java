package com.example.apptransport.Entity;

public class NewDriverVehiculo {

    private boolean marca;
    private boolean modelo;
    private boolean categoria;
    private boolean dominio;


    private String brandlabel;
    private String modellabel;
    private String typelabel;
    private String domainlabel;


    public NewDriverVehiculo() {
    }

    public NewDriverVehiculo(boolean marca, boolean modelo, boolean categoria, boolean dominio) {
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.dominio = dominio;
    }

    public boolean isMarca() {
        return marca;
    }

    public void setMarca(boolean marca) {
        this.marca = marca;
    }

    public boolean isModelo() {
        return modelo;
    }

    public void setModelo(boolean modelo) {
        this.modelo = modelo;
    }

    public boolean isCategoria() {
        return categoria;
    }

    public void setCategoria(boolean categoria) {
        this.categoria = categoria;
    }

    public boolean isDominio() {
        return dominio;
    }

    public void setDominio(boolean dominio) {
        this.dominio = dominio;
    }


    public String getBrandlabel() {
        return brandlabel;
    }

    public void setBrandlabel(String brandlabel) {
        this.brandlabel = brandlabel;
    }

    public String getModellabel() {
        return modellabel;
    }

    public void setModellabel(String modellabel) {
        this.modellabel = modellabel;
    }

    public String getTypelabel() {
        return typelabel;
    }

    public void setTypelabel(String typelabel) {
        this.typelabel = typelabel;
    }

    public String getDomainlabel() {
        return domainlabel;
    }

    public void setDomainlabel(String domainlabel) {
        this.domainlabel = domainlabel;
    }
}
