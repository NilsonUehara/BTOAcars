package br.com.aeroboteco.model;

import com.flightsim.fsuipc.FSUIPC;
import com.flightsim.fsuipc.fsuipc_wrapper;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class UIPC extends FSUIPC implements SimInterface{
    public UIPC(){
        fsuipc_wrapper.Open(fsuipc_wrapper.SIM_ANY);
    }
	
	public void testaStatus(){
		System.out.println("Parking brakes:" + (getShort(0x0bc8)==32767));
		System.out.println("Gear down:" + (getShort(0x0be8)==16383));
		System.out.println("Arnv on ground:" + (getShort(0x0366)==1));
		System.out.println("Radio altimeter:" + getShort(0x3324));
		System.out.println("Gnd altitude:" + Math.round(getShort(0x0020)/256*3.2808399));
		//System.out.println("V/S (mt/s):" + (getShort(0x02c8)));
		System.out.println("V/S (ft/m):" + Math.round(((getShort(0x02c8)/256.0)*60.0)*3.2808399));
		System.out.println("IAS:" + getShort(0x02bc)/128);
		//System.out.println("fuel:" + getShort(0x1264)+"/"+getShort(0x1240));
		//System.out.println("ZFW:" + getDouble(0x3bfc));
	}
	//Aircraft on ground
        public boolean isEmVoo(){return (getShort(0x0366)==0);}
	public boolean isParkingBrakes(){return (getShort(0x0bc8)==32767);}
	public boolean isGearDown(){return (getShort(0x0be8)==16383);}
	public long getTAS(){return Math.round(getShort(0x02b8)/128)-getVentoVelocidade();}
	public long getIAS(){return Math.round(getShort(0x02bc)/128);}
	public double getGS(){
        //System.out.println(getInt(0x02b4)/65536*1.9438);
        return (getInt(0x02b4)/65536*1.9438);
        //return Math.round(getShort(0x02b4));
    }
	public long getALT(){
        int inteiro=getInt(0x0574);
        if (inteiro<0)inteiro=0;
        double altInMeters=inteiro;
        //double altInMeters=Double.parseDouble(getInt(0x0574)+"."+getInt(0x0570));
        long altInFeets=Math.round(altInMeters*3.28084);
        return altInFeets;
    }
	public long getHDG(){
        long a=getLong(0x0580);
        double x=(65536.0*65536.0);
        double d=360.0/(65536.0*65536.0);
        double r=a*d;
        //System.out.println(r);
        //System.out.println(Math.round(r));
        return Math.round(r-getVarMag());
    }
    public long getVS(){return Math.round(((getShort(0x02c8)/256.0)*60.0)*3.2808399);}
    public double getLat(){
        //double a=90.0/(10001750.0*65536.0*65536.0);
        //double r=getLong(0x0560)*a;
        return Geo.getConverteLat(getLong(0x0560));
    }
    public double getLon(){
        //double a=360.0/(65536.0*65536.0*65536.0*65536.0);
        //double r=getLong(0x0568)*a;
        return Geo.getConverteLon(getLong(0x0568));
    }
    public long getZFW(){
        return getLong(0x3bfc)/256;
    }
    public boolean isEmPausa(){
        return getInt(0x0264)==1;
    }
    public int getVarMag(){
        return getShort(0x02a0)*360/65536;
    }
    public int getVentoDirecao(){
        int wnd=getShort(0x0E92);
        wnd=wnd*360/65536-(getVarMag());
        return wnd;
    }
    public short getVentoVelocidade(){
        return getShort(0x0E90);
    }
    public int getSimRate(){
        return getInt(0x0c1a)/256;
    }

    public void freeze(){
        //fsuipc_wrapper wr=new fsuipc_wrapper();
        /*Timer t=new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                byte[] b="255".getBytes();
                fsuipc_wrapper.WriteData(0x3541, 1, b);
                System.out.println("Freeze");
            }
        }, 1, 5000);
        //System.out.println("freeze");
        //getShort(0x3541);*/
        while (true){
                byte[] b="255".getBytes();
                fsuipc_wrapper.WriteData(0x3541, 1, b);
                System.out.println("Freeze");
        }
    }
    public void position(){
        //Lat/Lon/Alt/Pitch/Bank/Hdg
        //byte[] b="-23.508539766781222,-46.64202780444758".getBytes();
        //fsuipc_wrapper.WriteData(0x3541, 1, b);
        //System.out.println("Freeze");
        /*System.out.println(getLong(0x0560));
        double a=90.0/(10001750.0*65536.0*65536.0);
        double r=getLong(0x0560)*a;
        System.out.println(r);

        double a2=360.0/(65536.0*65536.0*65536.0*65536.0);
        double r2=getLong(0x0568)*a;*/

        System.out.println(getLong(0x0568));

        byte[] b="-11220706531660706".getBytes();
        fsuipc_wrapper.WriteData(0x0560, 8, b);
        b="-2389392803724124672".getBytes();
        fsuipc_wrapper.WriteData(0x0568, 8, b);
        fsuipc_wrapper.WriteData(0x055C, 4, b);
        System.out.println("lat/lon");

    }

    /*public double getDistancia(double latitudeDe, double longitudeDe, double latitudeAte, double longitudeAte){
        double c=90.0-latitudeDe;
        double b=90.0-latitudeAte;
        double a=longitudeDe-longitudeAte;

        double cosA=Math.cos(Math.toRadians(b))*Math.cos(Math.toRadians(c))+
                Math.sin(Math.toRadians(b))*Math.sin(Math.toRadians(c))*Math.cos(Math.toRadians(a));
        double distEmRadianos=Math.acos(cosA);
        double distEmGraus=Math.toDegrees(distEmRadianos);
        double distancia=(40030.0 * distEmGraus)/360;
        return distancia;
    }
    public double getKmEmNm(double km){
        return km*0.539956803;
    }*/
    public double arredondar(double valor){
          DecimalFormat aproximador = new DecimalFormat("0.00");
          return Double.parseDouble(aproximador.format(valor).replaceAll(",", "."));
    }
    public static void main(String[] args) {
        UIPC u=new UIPC();
        //u.freeze();
        u.position();

        //System.out.println("Freeze");

        //System.out.println("TAS: "+u.getTAS());
        //System.out.println("IAS: "+u.getIAS());
        //System.out.println("wnd-d: "+u.getVentoDirecao());
        //System.out.println("wnd-v: "+u.getVentoVelocidade());
        //System.out.println("hdg: "+u.getHDG());
        //System.out.println("hdg: "+u.getGS());
        //System.out.println("SR: "+u.getSimRate());
        //System.out.println("SR: "+u.getVarMag());
        //System.out.println("Wd: "+u.getVentoDirecao());
        //System.out.println("Wv: "+u.getVentoVelocidade());
        //System.out.println("alt1: "+u.getInt(0x0574)+"."+u.getInt(0x0570));
        //System.out.println("alt: "+u.getFloat(0x6da0));
        //double d=Geo.getDistancia(-22.902778,-43.206667,-23.548333,-46.636111);
        //System.out.println("Distancia em km="+u.arredondar(d));
        //System.out.println("Distancia em nm="+u.arredondar(Geo.getKmEmNm(d)));
        /*System.out.println(u.getLat()+","+u.getLon());
        System.out.println("fuel weight: "+u.getShort(0x3308));
        System.out.println("engines: "+u.getLong(0x0AEC));
        System.out.println("fuel weight: "+u.getShort(0x0AF4));
        System.out.println("simulation rate * 256: "+u.getLong(0x0c1a)/256);
        System.out.println("zero fuel height lbs * 256: "+u.getLong(0x3bfc)/256);
        System.out.println("fuel : "+u.getLong(0x0b74));
        System.out.println("fuel : "+u.getLong(0x0b78));
         *
         */
        //u.testaStatus();

    }
}
