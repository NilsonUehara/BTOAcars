package br.com.aeroboteco.model;

public class Geo {
    public static double getConverteLat(long lat){
        double a=90.0/(10001750.0*65536.0*65536.0);
        double r=lat*a;
        return r;
    }
    public static double getConverteLon(long lon){
        double a=360.0/(65536.0*65536.0*65536.0*65536.0);
        double r=lon*a;
        return r;
    }
    public static double getDistancia(double latitudeDe, double longitudeDe, double latitudeAte, double longitudeAte){
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
    public static double getKmEmNm(double km){
        return km*0.539956803;
    }
    public static boolean estouNoIcaoCerto(String icao, double latitude, double longitude){
        boolean correto=false;
        double margem=0.1;
        String[] cg=Props.getCoordenadasICAO(icao);
        double latCG=Double.parseDouble(cg[1]);
        double lonCG=Double.parseDouble(cg[2]);
        correto=(latitude>=(latCG-margem) && latitude<=(latCG+margem));
        if (correto) correto=(longitude>=(lonCG-margem) && longitude<=(lonCG+margem));
        return correto;
    }

}
