package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Logador;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SplashView  extends JFrame{
    private int duration;
    private boolean abrePirep;

    public SplashView(int d) {
        duration = d;
    }

    public void showSplash(boolean abrePirep) {
        this.abrePirep=abrePirep;
        JPanel content = (JPanel)getContentPane();
        //content.setBackground(Color.white);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        Logador.getLogador().log(Level.INFO,"setUndecorated");
        setUndecorated(true);

        // Configura a posição e o tamanho da janela
        Logador.getLogador().log(Level.INFO,"ScreenSize");
        int width = 400;
        int height = 400;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);

        // Constrói o splash screen
        Logador.getLogador().log(Level.INFO,"ImageIcon");
        JLabel label = new JLabel(new ImageIcon("splash.png"));
        content.add(label, BorderLayout.CENTER);
        // Torna visível
        Logador.getLogador().log(Level.INFO,"setVisible");
        setVisible(true);

        // Espera ate que os recursos estejam carregados
        //try { Thread.sleep(duration); } catch (Exception e) {}
        Logador.getLogador().log(Level.INFO,"TimerTask");
        Timer t=new Timer();
        t.schedule(new TimerTask(){
            @Override
            public void run() {
                Logador.getLogador().log(Level.INFO,"Fecha");
                fecha();
            }
        }, duration);
    }
    public void fecha(){
        Logador.getLogador().log(Level.INFO,"dentro do fecha()");
        if (abrePirep){
            Logador.getLogador().log(Level.INFO,"instanciando pirepview");
            PirepView p = new PirepView();
            Logador.getLogador().log(Level.INFO,"pirepview.setvisible");
            p.setVisible(true);
        }
        Logador.getLogador().log(Level.INFO,"dispose");
        dispose();
    }
    public static void main(String[] args) {
        try{
            Logador.getLogador().log(Level.INFO,"Construtor SplashScreen");
            SplashView s=new SplashView(5000);
            Logador.getLogador().log(Level.INFO,"Show SplashScreen");
            s.showSplash(true);
        }catch(Exception e){Logador.getLogador().log(Level.SEVERE,"Splash Main",e);}
    }
}
