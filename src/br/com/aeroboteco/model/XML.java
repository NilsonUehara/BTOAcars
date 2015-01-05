package br.com.aeroboteco.model;

import java.io.File;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class XML {
    public static void gravaPirep(List<PV> pvs, String arquivo){
        XStream xs=new XStream(new DomDriver());
        File f=new File(arquivo);
        try {
            CryptographyTripleDES crypto=CryptographyTripleDES.newInstance();
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write(crypto.encrypt(xs.toXML(pvs)));
            out.close();
        }
        catch(Exception e){Logador.getLogador().log(Level.SEVERE,"gravaPirep",e);}
    }
    public static void gravaPirep(PV pv, String arquivo){
        List<PV>pvs=(List<PV>)XML.le(arquivo);
        pvs.add(pv);
        XML.gravaPirep(pvs, arquivo);
    }
    public static void gravaPirep(PV pv){
        XML.gravaPirep(pv, "acars");
    }
    public static List<PV> le(String arquivo){
        List<PV>pvs=new ArrayList<PV>();
        File f=new File(arquivo);
        try{
        if (f.exists()){
            XStream xs=new XStream(new DomDriver());
            BufferedReader ri=new BufferedReader(new FileReader(f));
            try{
                CryptographyTripleDES crypto=CryptographyTripleDES.newInstance();
                StringBuffer conteudo=new StringBuffer();
                while (ri.ready()){
                    conteudo.append(ri.readLine());
                }
                pvs=(List<PV>)xs.fromXML(arquivo=crypto.decrypt(conteudo.toString()));
            }catch(Exception e){Logador.getLogador().severe(e.getMessage());}
            ri.close();
        }
        }catch(Exception e){
            Logador.getLogador().log(Level.SEVERE,"le",e);
            pvs=new ArrayList<PV>();
        }
        return pvs;
    }
    public static List<PV> le(){
        return XML.le("acars");
    }
    public static List<Tour> leTours(){
        List<Tour> lista=new ArrayList<Tour>();
        File f=new File("tour.xml");
        try{
            XStream xs=new XStream(new DomDriver());
            Reader ri=new BufferedReader(new FileReader(f));
            lista=(List)xs.fromXML(ri);
            ri.close();
        }
        catch(FileNotFoundException e){/*arquivo não encontrado*/}
        catch(IOException e){Logador.getLogador().log(Level.SEVERE,"leTours",e);}
        catch(Exception e){JOptionPane.showMessageDialog(null, "Não foi possível carregar a lista de tours. Clique em Option/Carregar Tours");}
        return lista;
    }
    public static List<Ad> leIcaos(String xml){
        List<Ad> lista=new ArrayList<Ad>();
        try{
            XStream xs=new XStream(new DomDriver());
            xs.alias("List", List.class);
            xs.alias("Ad", Ad.class);
            lista=(List)xs.fromXML(xml);
        }
        catch(Exception e){
            Logador.getLogador().log(Level.WARNING,"versao",e);
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao carregar a lista de Icaos");
        }
        return lista;
    }
    public static void main(String[] args) {
        /*PV pv=new PV();
        pv.setBto("BTO5121");
        pv.setDep("SBMT");
        pv.setArr("SBSP");
        pv.setDataHoraInicio(new Date());
        pv.setLatDe(-23.878878454);
        pv.getLog().add(new Evento("13:00","Teste de fvento"));
        pv.getLog().add(new Evento("13:01","Teste de fvento1"));
        pv.getLog().add(new Evento("13:02","Teste de fvento2"));
        pv.getLog().add(new Evento("13:03","Teste de fvento3"));
        pv.getLog().add(new Evento("13:04","Teste de fvento4"));
        pv.getLog().add(new Evento("13:05","Teste de fvento5"));
        pv.getLog().add(new Evento("13:06","Teste de fvento6"));
        XML.gravaPirep(pv);
        pv=new PV();
        pv.setBto("BTO5102");
        pv.setDep("SBSP");
        pv.setArr("SBGR");
        pv.setDataHoraInicio(new Date());
        pv.setLatDe(-42.78454);
        pv.getLog().add(new Evento("12:00","Teste de fvento"));
        pv.getLog().add(new Evento("12:01","Teste de fvento1"));
        pv.getLog().add(new Evento("12:02","Teste de fvento2"));
        pv.getLog().add(new Evento("12:03","Teste de fvento3"));
        pv.getLog().add(new Evento("12:04","Teste de fvento4"));
        pv.getLog().add(new Evento("12:05","Teste de fvento5"));
        pv.getLog().add(new Evento("12:06","Teste de fvento6"));
        XML.gravaPirep(pv);*/

        /*List<PV>pvs=XML.le();*/
        
        List<PV>pvs=XML.le("acars_err.xml");
        for(PV p:pvs){
            System.out.println("pv:"+p.getDep()+"/"+p.getArr());
            for (Evento e:p.getLog()){
                System.out.println("    evento:"+e.getHora()+":"+ e.getEvento());
            }
        }
        /*            for(Tour t:lista){
                System.out.println("Tour"+t.getCodigo()+"-"+t.getNome());
                for(PernaTour pt:t.getPernas()){
                    System.out.println("     Perna"+pt.getSequencia()+"-"+pt.getOrigem()+"/"+pt.getDestino());
                }
            }
            */
    }
}
