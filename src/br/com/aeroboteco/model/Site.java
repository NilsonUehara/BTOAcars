package br.com.aeroboteco.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class Site {
    public enum FASE{DECOLANDO(3),SUBINDO(4),CRUZEIRO(5),DESCENDO(6),POUSADO(7);
        private int fase;
        FASE(int fase){this.fase=fase;}
        public int getFase(){return fase;}
    }
    public static String versao(){
        String resposta = "";
        try{
            String stringURL = "http://www.aeroboteco.com.br/nilson/versao.php";
            Logador.getLogador().info("versao: "+stringURL);
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            resposta = sb.toString();
            Logador.getLogador().info("Resposta versao: "+resposta);
            in.close();
        }
        catch (UnknownHostException  ex) {Logador.getLogador().log(Level.WARNING,"versao() - falha na internet",ex);}
        catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"versao",ex);}
        return resposta;
    }
    public static boolean sendAcars(PV pv){

        boolean ok = false;
        try{
            String resposta = "";
            SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat hr = new SimpleDateFormat("hh:mm:ss");
            //"http://www.aeroboteco.com.br/va/fsacars/receive_pirep.php?";
            String data=URLEncoder.encode("pilot", "iso-8859-1") + "=" + URLEncoder.encode(pv.getBto(), "iso-8859-1");
            data+="&"+URLEncoder.encode("date", "iso-8859-1") + "=" + URLEncoder.encode(dt.format(pv.getDataHoraInicio()), "iso-8859-1");
            data+="&"+URLEncoder.encode("time", "iso-8859-1") + "=" + URLEncoder.encode(hr.format(pv.getDataHoraInicio()), "iso-8859-1");
            data+="&"+URLEncoder.encode("callsign", "iso-8859-1") + "=" + URLEncoder.encode(pv.getBto(), "iso-8859-1");
            data+="&"+URLEncoder.encode("reg", "iso-8859-1") + "=" + URLEncoder.encode("", "iso-8859-1");
            data+="&"+URLEncoder.encode("origin", "iso-8859-1") + "=" + URLEncoder.encode(pv.getDep(), "iso-8859-1");
            data+="&"+URLEncoder.encode("dest", "iso-8859-1") + "=" + URLEncoder.encode(pv.getArr(), "iso-8859-1");
            data+="&"+URLEncoder.encode("alt", "iso-8859-1") + "=" + URLEncoder.encode(pv.getAlt(), "iso-8859-1");
            data+="&"+URLEncoder.encode("equipment", "iso-8859-1") + "=" + URLEncoder.encode(pv.getAcft(), "iso-8859-1");
            data+="&"+URLEncoder.encode("fuel", "iso-8859-1") + "=" + URLEncoder.encode("0", "iso-8859-1");
            data+="&"+URLEncoder.encode("duration", "iso-8859-1") + "=" + URLEncoder.encode(pv.getDuracao(), "iso-8859-1");
            data+="&"+URLEncoder.encode("distance", "iso-8859-1") + "=" + URLEncoder.encode(String.valueOf(pv.getDistancia()), "iso-8859-1");
            data+="&"+URLEncoder.encode("version", "iso-8859-1") + "=" + URLEncoder.encode("BTOACARS", "iso-8859-1");
            data+="&"+URLEncoder.encode("more", "iso-8859-1") + "=" + URLEncoder.encode("0", "iso-8859-1");
            data+="&"+URLEncoder.encode("tour", "iso-8859-1") + "=" + URLEncoder.encode(String.valueOf(pv.getTour()), "iso-8859-1");
            data+="&"+URLEncoder.encode("log", "iso-8859-1") + "=" + URLEncoder.encode(pv.getLogText(), "iso-8859-1");
            //String stringURL = Props.getProperty("url.acars")+Props.getProperty("pirep")+"?"+


            //System.out.println(stringURL);
            ok=true;
            //stringURL=stringURL.replaceAll(" ", "%20");
            Logador.getLogador().info("SendAcars: "+data);
            URL url = new URL(Props.getProperty("url.acars")+Props.getProperty("pirep"));
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            resposta = sb.toString();

            wr.close();
            in.close();

            Logador.getLogador().info("Resposta SendAcars: "+resposta);
            in.close();
            ok = (resposta.equalsIgnoreCase("OK"));
            if (ok){
                //Pirep foi enviado com sucesso!
                Site.pirepsEnviados(pv.getBto());
            }
        }
        //catch (UnknownHostException  ex) {ok=false;Logador.getLogador().log(Level.WARNING,"sendAcars() - falha na internet",ex);}
        catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"SendAcars",ex);}
        return ok;
    }
    /*public static boolean sendAcars(PV pv) {
        boolean ok = false;
        try{
            String resposta = "";
            SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat hr = new SimpleDateFormat("hh:mm:ss");
            //"http://www.aeroboteco.com.br/va/fsacars/receive_pirep.php?";
            String stringURL = Props.getProperty("url.acars")+Props.getProperty("pirep")+"?"+
                "pilot=" + pv.getBto()+
                "&date=" + dt.format(pv.getDataHoraInicio())+
                "&time=" + hr.format(pv.getDataHoraInicio())+
                "&callsign=" + pv.getBto()+
                "&reg="+
                "&origin=" + pv.getDep()+
                "&dest=" + pv.getArr()+
                "&alt=" + pv.getAlt()+
                "&equipment=" + pv.getAcft()+
                "&fuel=0"+
                "&duration=" + pv.getDuracao()+
                "&distance=" + pv.getDistancia()+
                "&version=BTOACARS"+
                "&more=0"+
                "&tour=" + pv.getTour()+
                "&log=" + pv.getLogText();
            //System.out.println(stringURL);
            ok=true;
            stringURL=stringURL.replaceAll(" ", "%20");
            Logador.getLogador().info("SendAcars: "+stringURL);
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            resposta = sb.toString();
            Logador.getLogador().info("Resposta SendAcars: "+resposta);
            in.close();
            ok = (resposta.equalsIgnoreCase("OK"));
            if (ok){
                //Pirep foi enviado com sucesso!
                Site.pirepsEnviados(pv.getBto());
            }
        }
        //catch (UnknownHostException  ex) {ok=false;Logador.getLogador().log(Level.WARNING,"sendAcars() - falha na internet",ex);}
        catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"SendAcars",ex);}
        return ok;
    }*/

    public static void sendPosition(PV pv, double lat, double lon, long ias, long alt, FASE fase, double distDep, double distArr, String ete) {
        SimpleDateFormat hr = new SimpleDateFormat("hh:mm");
        Calendar c = Calendar.getInstance();
        try{
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.MINUTE, (int) (new Date().getTime() - pv.getDataHoraInicio().getTime()));
            //"http://www.aeroboteco.com.br/va/fsacars/position_update.php?";
            String stringURL = Props.getProperty("url.acars")+Props.getProperty("posicao")+"?"+
                "lat=" + lat+
                "&long=" + lon+
                "&GS=" + ias+
                "&Alt=" + alt+
                "&IATA=BTO"+
                "&pnumber=" + pv.getBto().substring(3)+
                "&depaptICAO=" + pv.getDep()+
                "&depapt="+pv.getDepNome()+
                "&disdepapt="+distDep+
                "&timedepapt=" + hr.format(new Date(c.getTimeInMillis()))+
                "&destaptICAO=" + pv.getArr()+
                "&destapt="+pv.getArrNome()+
                "&disdestapt="+distArr+
                "&timedestapt="+ete+
                "&detailph="+fase.getFase()+
                "&Regist=";
            stringURL=stringURL.replaceAll(" ", "%20");
            Logador.getLogador().info("SendPosition: "+stringURL);
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            Logador.getLogador().info("Resposta SendPosition: "+sb.toString());
            in.close();
        }
        catch (UnknownHostException  ex) {Logador.getLogador().log(Level.WARNING,"sendPosition() - falha na internet",ex);}
        catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"SendPosition",ex);}
    }
    public static String carregaTour() {
        String stringURL = Props.getProperty("url.acars")+"dados.php";
        String xml = "";
        try {
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            xml = sb.toString();
            in.close();
        }
        catch (UnknownHostException  ex) {/*sem internet*/
            JOptionPane.showMessageDialog(null, "Não foi possível estabelecer uma conexão com o servidor Aeroboteco.");
        }
        catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"carregaTour",ex);}
        return xml;
    }
    public static String[] carregaApt(String icao) {
        String stringURL = Props.getProperty("url.acars")+"dados.php?tipo=apt&icao="+icao;
        String[] apt={"","","0.0","0.0"};
        try {
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            //Recebe icao,nome,lat,lon
            if (sb.toString()!=null && !sb.toString().equals("")){
                apt=sb.toString().split(";");
            }
            in.close();
        } catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"carregaApt",ex);}
        return apt;
    }
    public static String carregaApt() {
        String stringURL = Props.getProperty("url.acars")+"dados.php?tipo=allapt";
        String xml="";
        try {
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            //Recebe icao,nome,lat,lon
            if (sb.toString()!=null && !sb.toString().equals("")){
                xml=sb.toString();
            }
            in.close();
        } catch (Exception ex) {Logador.getLogador().log(Level.SEVERE,"carregaApt",ex);}
        return xml;
    }
    public static void pirepsEnviados(String bto) {
        String stringURL = Props.getProperty("url.acars")+"dados.php?tipo=pirepsenviados&bto="+bto;
        try {
            URL url = new URL(stringURL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            if (sb.toString()!=null && !sb.toString().equals("")){
                //BufferedWriter bw=new BufferedWriter(new FileWriter("tourvoado"));
                String[] pireps=sb.toString().split("#");
                for (int x=0; x<pireps.length;x++){
                    String[] pernas=pireps[x].split(";");
                    for (int y=0;y<pernas.length;y++){
                        //3141;10;FAJS;FAMM;Aprovado;
                        String perna=pernas[1]+":"+pernas[2]+":"+pernas[3];
                        String codigo=pernas[0];
                        String status=pernas[4];
                        Props.setPirepEnviado(perna,codigo+";"+status);
                    }
                }
            }
            in.close();
        }
        catch (UnknownHostException  e) {Logador.getLogador().log(Level.WARNING,"pirepsEnviados() - falha na internet",e);}
        catch (Exception e) {Logador.getLogador().log(Level.SEVERE,"pirepsEnviados",e);}
         
    }
    public static void main(String[] args) {
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try{
            PV pv=new PV();
            pv.setAcft("B205");
            pv.setAlt("4500");
            pv.setArr("SBMT");
            pv.setArrNome("Campo de Marte");
            pv.setBto("BTO5102");
            pv.setDataHoraInicio(sdf.parse("05/03/2014 10:00:00"));
            pv.setDataHoraFim(sdf.parse("05/03/2014 12:05:15"));
            pv.setDep("SBST");
            pv.setDepNome("Base de Santos");
            pv.setDistancia(31.45);
            pv.setDuracao("0:22");
            pv.setLatDe(-23.932885984113955);
            pv.setLonDe(-46.29595510074744);
            pv.setLatAte(-23.513130968231593);
            pv.setLonAte(-46.63828546422628);
            pv.setRoute("DCT");
            pv.setTouchDownIAS(80);
            pv.setTouchDownRate(-98);
            List<Evento>log=new ArrayList<Evento>();
            log.add(new Evento("10:13", "Takeoff: 80kt"));
            log.add(new Evento("10:13", "Lat/Lon: -23.932885984113955/-46.29595510074744"));
            log.add(new Evento("10:18", "Lat/Lon: -23.54757712902765/-46.51562665449147"));
            pv.setLog(log);
            Site.sendAcars(pv);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
