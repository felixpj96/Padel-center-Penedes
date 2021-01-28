package com.example.pc;

public class Reserva {

    String pista;
    String fecha;

    public Reserva(String pista, String fecha) {
        this.pista = pista;
        this.fecha = fecha;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
