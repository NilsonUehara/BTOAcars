package br.com.aeroboteco.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PV {

    private String bto;
    private String dep;
    private String depNome;
    private String arr;
    private String arrNome;
    private String alt;
    private String acft;
    private int fl;
    private int tour;
    private String route;
    private long touchDownRate;
    private long touchDownIAS;
    private Date dataHoraInicio;
    private Date dataHoraFim;
    private double latDe;
    private double lonDe;
    private double latAte;
    private double lonAte;
    private String duracao;
    private double distancia;
    private List<Evento>log=new ArrayList<Evento>();

    public String getBto() {
        return bto;
    }

    public void setBto(String bto) {
        this.bto = bto;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAcft() {
        return acft;
    }

    public void setAcft(String acft) {
        this.acft = acft;
    }

    public int getFl() {
        return fl;
    }

    public void setFl(int fl) {
        this.fl = fl;
    }

    public int getTour() {
        return tour;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public long getTouchDownRate() {
        return touchDownRate;
    }

    public void setTouchDownRate(long touchDownRate) {
        this.touchDownRate = touchDownRate;
    }

    public long getTouchDownIAS() {
        return touchDownIAS;
    }

    public void setTouchDownIAS(long touchDownIAS) {
        this.touchDownIAS = touchDownIAS;
    }

    public Date getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Date dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    /*public void addDataHoraFim(int segundos) {
        Calendar c=Calendar.getInstance();
        c.setTime(dataHoraFim);
        c.add(Calendar.SECOND,segundos);
        this.dataHoraFim.setTime(c.getTimeInMillis());
    }*/

    public double getLatDe() {
        return latDe;
    }

    public void setLatDe(double latDe) {
        this.latDe = latDe;
    }

    public double getLonDe() {
        return lonDe;
    }

    public void setLonDe(double lonDe) {
        this.lonDe = lonDe;
    }

    public double getLatAte() {
        return latAte;
    }

    public void setLatAte(double latAte) {
        this.latAte = latAte;
    }

    public double getLonAte() {
        return lonAte;
    }

    public void setLonAte(double lonAte) {
        this.lonAte = lonAte;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public List<Evento> getLog() {
        return log;
    }

    public void setLog(List<Evento> log) {
        this.log = log;
    }
    public String getLogText(){
        StringBuffer vlog=new StringBuffer();
        for (Evento e:getLog()){
            //if (!e.getEvento().contains("SIM RATE")){
            if (e.getHora().equals("")){
                vlog.append(e.getEvento()+"*");
            }else{
                vlog.append(e.getHora()+" "+e.getEvento()+"*"); //+"\n\r";
            }
            //}
        }
        String lg=vlog.toString();
        lg=lg.replace("\n", " ");
        return lg; //vlog.toString();
    }

    public String getDepNome() {
        return depNome;
    }

    public void setDepNome(String depNome) {
        this.depNome = depNome;
    }

    public String getArrNome() {
        return arrNome;
    }

    public void setArrNome(String arrNome) {
        this.arrNome = arrNome;
    }

}
