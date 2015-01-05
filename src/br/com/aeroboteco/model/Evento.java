package br.com.aeroboteco.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Evento {
    private String hora;
    private String evento;

    public Evento() {}
    
    public Evento(String hora, String evento){
        this.hora=hora;
        this.evento=evento;
    }
    public Evento(String evento){
        this(new SimpleDateFormat("HH:mm").format(new Date()),evento);
    }
    public String getHora(){return hora;}
    public String getEvento(){return evento;}
}
