package com.smartseals.generic.Modelo;

public class Descargo {

    private String odt, fechaInicio,fechaFin;
    private boolean finalizar = false;

    public boolean isFinalizar() {
        return finalizar;
    }

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }

    public Descargo(String odt, String fechaInicio, String fechaFin ) {
        this.odt = odt;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.finalizar = finalizar;

    }



    public String getOdt() {
        return odt;
    }

    public void setOdt(String odt) {
        this.odt = odt;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }


}
