package br.com.aeroboteco.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class BtoUIPC implements SimInterface{
    private static int clientPort = 49005;
    private static int buffer_size = 1024;
    private static DatagramSocket ds;
    private static byte buffer[] = new byte[buffer_size];

    private boolean simPresente;
    private boolean emVoo;
    private long vs;
    private long alt;
    private float lat;
    private float lon;
    private long tas;
    private long ias;
    private long gs;
    private long hdg;
    private float flap;
    private boolean gearDown;
    private boolean brakeOn;
    private long fuelWeight;
    private long zfw;
    private long payload;
    private DatagramPacket p;

    public BtoUIPC(){
        try{
            ds = new DatagramSocket(clientPort);
            p = new DatagramPacket(buffer, buffer.length);
            ds.setSoTimeout(10000);
            Logador.getLogador().log(Level.INFO,"Aguardando resposta do X-Plane por 10seg.");
            recebe();
            if (simPresente()){
                Logador.getLogador().log(Level.INFO,"X-Plane - respondeu! Iniciando timer");
                Timer t=new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        recebe();
                    }
                },50,50);
            }
        }
        catch(Exception e){Logador.getLogador().log(Level.SEVERE,"BtoUIPC",e);}
    }
    private void recebe(){
                    try{
                        ds.receive(p);
                        simPresente=true;

                        int start=5;
                        long offset=0;
                        while(p.getData()[start]!=0 || p.getData()[start+1]!=0 || p.getData()[start+2]!=0 || p.getData()[start+3]!=0){
                            //System.out.println("::"+start);
                            int start2=start;
                            for(int x=0; x<=8; x++){
                                if (x==0) {
                                    offset=arr2long(p.getData(), start2);
                                    //System.out.println("OffSet:"+offset);
                                }else{
                                    //System.out.println("float:"+arr2float(p.getData(), start2));
                                    float f=arr2float(p.getData(), start2);
                                    switch((int)offset){
                                        case 3:
                                            //speeds
                                            if (x==1){ias=(long)f;}
                                            if (x==3){tas=(long)f;}
                                            if (x==4){gs=(long)f;}
                                        case 4:
                                            //mach,vvi,g-load
                                            if (x==3){vs=(long)f;}
                                        case 13:
                                            //flaps
                                            if (x==4){flap=f;}
                                        case 14:
                                            //gear/brake
                                            if (x==1){gearDown=(f>0.0);}
                                            if (x==2){brakeOn=(f>0.0);}
                                        case 18:
                                            //pitch/roll/heading
                                            if (x==3){hdg=(long)f;}
                                        case 20:
                                            //lat
                                            if (x==1){lat=f;}
                                            if (x==2){lon=f;}
                                            if (x==3){alt=(long)f;}
                                            if (x==4){emVoo=(f>3.0);}
                                        case 63:
                                            //payload weight
                                            if (x==1){zfw=(long)f;}
                                            if (x==2){payload=(long)f;}
                                            if (x==3){fuelWeight=(long)f;}
                                    }
                                }
                                start2+=4;
                            }
                            start+=36;
                        }
                    }
                    catch(SocketTimeoutException e){
                        Logador.getLogador().log(Level.INFO,"X-Plane não responde");
                        simPresente=false;
                    }
                    catch(Exception e){Logador.getLogador().log(Level.SEVERE,"BtoUIPC",e);}

    }
    public boolean simPresente(){
        return simPresente;
    }
    public boolean isEmVoo() {
        return emVoo;
    }

    public boolean isParkingBrakes() {
        return brakeOn;
    }

    public boolean isGearDown() {
        return gearDown;
    }

    public long getTAS() {
        return gs; //tas;
    }

    public long getIAS() {
        return ias;
    }

    public double getGS() {
        return gs;
    }

    public long getALT() {
        return alt;
    }

    public long getHDG() {
        return hdg;
    }

    public long getVS() {
        return vs;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getZFW() {
        return zfw;
    }

    public int getVentoDirecao() {
        return 1;
    }

    public short getVentoVelocidade() {
        return 1;
    }

    public boolean isEmPausa() {
        return false;
    }

    public int getSimRate() {
        return 1;
    }

    public double arredondar(double valor) {
        DecimalFormat aproximador = new DecimalFormat("0.00");
        return Double.parseDouble(aproximador.format(valor).replaceAll(",", "."));
    }
    public float arr2float (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}
	public long arr2long (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
    public double arr2double (byte[] arr, int start) {
		int i = 0;
		int len = 8;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}
    public static void main(String args[]){
        BtoUIPC b=new BtoUIPC();
        System.out.println("HDG:"+b.getHDG());
        System.out.println("Alt:"+b.getALT());
        System.out.println("ZFW:"+b.getZFW());
    }

}
