package br.com.aeroboteco.model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logador{
   
    private static Logger log;
    public static enum NIVEL{
        ERRO("Somente Erros", Level.SEVERE ),INFO("Modo Debug",Level.INFO);
        private String descricao;
        private Level level;
        NIVEL(String descricao,Level level){
            this.descricao=descricao;
            this.level=level;
        }
        public String toString(){return this.descricao;}
        public Level getLevel(){return this.level;}
    }
    public static Logger getLogador(){
        Level level=Level.SEVERE;
        try{
            level=Logador.NIVEL.values()[Integer.parseInt(Props.getProperty("log"))].getLevel();
        }catch(Exception e){/*Ignorar*/}
        if (log==null){
            log=Logger.getLogger("");
            Handler handler;
            try {
                handler = new FileHandler("erro.txt",true);
                handler.setFormatter(new SimpleFormatter());
                handler.setLevel(level);
                log.addHandler(handler);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
            log.setLevel(level);
        }
        return log;
    }
}
