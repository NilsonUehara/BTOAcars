package br.com.aeroboteco.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Properties;
import java.util.logging.Level;
public class Props {
    public static String getProperty(String key){
        File cfg=new File("cfg.txt");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cfg);
            props.load(fis);
            fis.close();
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"getProperty",ex);}
        String valor=props.getProperty(key);
        if (valor==null)valor="";
        return valor;
    }
    public static void setProperty(String key, String value){
        File cfg=new File("cfg.txt");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cfg);
            props.load(fis);
            fis.close();
            props.setProperty(key,value);
            props.store(new FileOutputStream(new File("cfg.txt")), "Configurações");
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"setProperty",ex);}
    }
    public static String[] getCoordenadasICAO(String key){
        File cfg=new File("apt");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cfg);
            props.load(fis);
            fis.close();
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"getCoordenadasICAO",ex);}
        String ret[]={"",""};
        if (props.getProperty(key)!=null){
            ret=props.getProperty(key).split(";");
        }
        return ret;
    }
    public static void setCoordenadasICAO(String key, String value){
        File cfg=new File("apt");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cfg);
            props.load(fis);
            fis.close();
            props.setProperty(key,value);
            props.store(new FileOutputStream(new File("apt")), "Apt");
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"setCoordenadasICAO",ex);}
    }
    public static String[] getPirepEnviado(String key){
        File tv=new File("tourvoado");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(tv);
            props.load(fis);
            fis.close();
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"getPirepEnviado",ex);}
        String ret[]={"",""};
        if (props.getProperty(key)!=null){
            ret=props.getProperty(key).split(";");
        }
        return ret;
    }
    public static void setPirepEnviado(String key, String value){
        Logador.getLogador().log(Level.INFO,"key:"+key+" - value:"+value);
        File tv=new File("tourvoado");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            if (!tv.exists())tv.createNewFile();
            fis = new FileInputStream(tv);
            props.load(fis);
            fis.close();
            props.setProperty(key,value);
            props.store(new FileOutputStream(new File("tourvoado")), "tourvoado");
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"setPirepEnviado",ex);}
    }
    public static ArrayList<Acft> getAcft(){
        ArrayList<Acft> acfts=new ArrayList<Acft>();
        File tv=new File("acft");
        Properties props=new Properties();
        FileInputStream fis = null;
        Set s=null;
        try {
            fis = new FileInputStream(tv);
            props.load(fis);
            s= props.keySet();
            Iterator i=s.iterator();
            while (i.hasNext()){
                String key=(String) i.next();
                String valor[]=props.getProperty(key).split(";");
                Acft acft=new Acft(key,valor[0],valor[1]);
                //System.out.println("acft:"+props.getProperty(key));
                acfts.add(acft);
            }
            fis.close();
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"getAcft",ex);}
        return acfts;
    }
    public static Acft getAcft(String icao){
        Acft acft=new Acft(icao, "x", "x");
        File tv=new File("acft");
        Properties props=new Properties();
        FileInputStream fis = null;
        Set s=null;
        try {
            fis = new FileInputStream(tv);
            props.load(fis);
            String dados=props.getProperty(icao);
            if (dados!=null){
                String valor[]=dados.split(";");
                acft=new Acft(icao,valor[0],valor[1]);
            }
            fis.close();
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"getAcft",ex);}
        return acft;
    }
    public static void setAcft(Acft acft){
        if (acft==null)return;
        File tv=new File("acft");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            if (!tv.exists())tv.createNewFile();
            fis = new FileInputStream(tv);
            props.load(fis);
            fis.close();
            props.setProperty(acft.getIcao(),acft.getFabricante()+";"+acft.getModelo());
            props.store(new FileOutputStream(new File("acft")), "acft");
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"setAcft",ex);}
    }
    public static void removeAcft(String key){
        if (key==null)return;
        File tv=new File("acft");
        Properties props=new Properties();
        FileInputStream fis = null;
        try {
            if (!tv.exists())tv.createNewFile();
            fis = new FileInputStream(tv);
            props.load(fis);
            fis.close();
            props.remove(key);
            props.store(new FileOutputStream(new File("acft")), "acft");
        }
        catch (IOException ex) {Logador.getLogador().log(Level.SEVERE,"removeAcft",ex);}
    }
    public static void main(String[] args) {
        /*for (Acft a:Props.getAcft()){
            System.out.println(a.getIcao()+"-"+a.getFabricante()+" : "+a.getModelo());
        }*/
        Acft a = Props.getAcft("172");
        System.out.println(a.getIcao()+"-"+a.getFabricante()+" : "+a.getModelo());
    }
}
