package br.com.aeroboteco.model;

public class Acft {
    private String icao;
    private String fabricante;
    private String modelo;

    public Acft(String icao, String fabricante, String modelo){
        this.icao=icao;
        this.modelo=modelo;
        this.fabricante=fabricante;
    }

    public String getIcao() {
        return icao;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void gravar(){
        Props.setAcft(this);
    }
}
