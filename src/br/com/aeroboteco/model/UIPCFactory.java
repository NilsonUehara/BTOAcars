package br.com.aeroboteco.model;

import com.flightsim.fsuipc.fsuipc_wrapper;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class UIPCFactory {
    private static SimInterface uipc = null;

    public static SimInterface getUIPC() {
        Logador.getLogador().log(Level.INFO,"getUIPC()");
        if (Props.getProperty("simulador").equals("1")) {
            Logador.getLogador().log(Level.INFO,"Simulador = X-Plane");
            //X-Plane - BtoUIPC
            if (uipc==null) uipc = new BtoUIPC();
        } else {
            Logador.getLogador().log(Level.INFO,"Simulador = FS");
            //FS - FSUIPC
            if (uipc==null) uipc = new UIPC();
        }
        return uipc;
    }

    public static boolean isOK() {
        boolean ok = true;
        if (Props.getProperty("simulador").equals("1")) {
            //X-Plane - BtoUIPC
            if (!((BtoUIPC)UIPCFactory.getUIPC()).simPresente()){
                Logador.getLogador().warning("Simulador (X-Plane) não está presente.");
                if (JOptionPane.showConfirmDialog(null, "Simulador (X-Plane) não está presente. Deseja abrir em modo off?", "Offline", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "O botão ENVIAR foi desativado.");
                    ok = false;
                } else {
                    System.exit(0);
                }
            }
        }else{
            //FS - FSUIPC
            int ret = 0;
            try {
                ret = fsuipc_wrapper.Open(fsuipc_wrapper.SIM_ANY);
            } catch (UnsatisfiedLinkError e) {
                Logador.getLogador().warning("Não foi possível encontrar o arquivo fsuipc_java.dll.");
                JOptionPane.showMessageDialog(null, "Não foi possível encontrar o arquivo fsuipc_java.dll. Talvez o Java instalado seja 64bits (Instale a versão 32bits)");
                e.printStackTrace();
                System.exit(0);
            }
            if (ret == 0) {
                Logador.getLogador().warning("Simulador (Flight Simulator / X-Plane) não está presente.");
                if (JOptionPane.showConfirmDialog(null, "Simulador (Flight Simulator / X-Plane) não está presente. Deseja abrir em modo off?", "Offline", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "O botão ENVIAR foi desativado.");
                    ok = false;
                } else {
                    System.exit(0);
                }
            }
        }
        return ok;
    }

}
