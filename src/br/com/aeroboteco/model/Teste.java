package br.com.aeroboteco.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Nilson
 */
public class Teste {
    public static void main(String[] args){
        JFileChooser jFileChooser1 = new JFileChooser(new File("."));  
        jFileChooser1.setApproveButtonText("Selecionar");  
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
        
        int returnVal = jFileChooser1.showDialog(null, null);  
        if (returnVal == JFileChooser.APPROVE_OPTION) {  
            File raiz = new File(jFileChooser1.getSelectedFile().getAbsolutePath());  
            //StringBuilder sb=new StringBuilder();
            for(File f: raiz.listFiles()) {  
                if(f.isFile()) {  
                    /*System.out.println(f.getName());  
                    sb.append(f.getName());  
                    sb.append("\n"); */
                    try{
                        FileReader reader = new FileReader(f);
                        //leitor do arquivo
                        BufferedReader leitor = new BufferedReader(reader);
                        String data=leitor.readLine();
                        if (data!=null && !"".equals(data.trim())){
                            System.out.println(f.getName()+"-> "+data);
                            envia(data);
                        }
                        leitor.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }  
            }  
        }
    }
    public static void envia(String data){
        try{
            URL url = new URL("http://www.aeroboteco.com.br/btoacars/receive_btoacars.php");
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
            System.out.println("Resposta: "+sb.toString());

            wr.close();
            in.close();
            in.close();
        }
        catch (Exception ex) {ex.printStackTrace();}
    }
}
