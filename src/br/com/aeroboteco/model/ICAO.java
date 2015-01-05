package br.com.aeroboteco.model;

public class ICAO {
    public static boolean isIcaoCorreto(String icao, double latitude, double longitude){
        boolean correto=false;
        double margem=0.01;
        String[] cg=Props.getCoordenadasICAO(icao);
        double latCG=Double.parseDouble(cg[1]);
        double lonCG=Double.parseDouble(cg[2]);
        correto=(latitude>=(latCG-margem) && latitude<=(latCG+margem));
        if (correto) correto=(longitude>=(lonCG-margem) && longitude<=(lonCG+margem));
        return correto;
    }

    public static void main(String[] args) {
        String icao="SBGR";
        String[] cg=Props.getCoordenadasICAO(icao);
        System.out.println(icao+": "+cg[0]+" - "+cg[1]+" - "+cg[2]);
        System.out.println(ICAO.isIcaoCorreto("SBGR", -23.43439531427888,-46.450404330088975));
        
        //-23.43439531427888/-46.470404330088975
    }
}
