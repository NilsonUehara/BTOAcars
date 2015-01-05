package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Logador;
import br.com.aeroboteco.model.Props;
import java.awt.Dimension;
import java.awt.Toolkit;

public class ConfigView extends javax.swing.JFrame {

    public ConfigView() {
       
        initComponents();
        setTitle("Configurações");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-getWidth())/2;
        int y = (screen.height-getHeight())/2;
        setBounds(x,y, getWidth(),getHeight());

        jtxtBto.setText(Props.getProperty("matricula"));
        jtxtUrl.setText(Props.getProperty("url.acars"));
        jtxtPirep.setText(Props.getProperty("pirep"));
        jtxtPosicao.setText(Props.getProperty("posicao"));
        jchkProxy.setSelected(Props.getProperty("proxy").equalsIgnoreCase("S"));
        jtxtHost.setText(Props.getProperty("proxy.host"));
        jtxtPort.setText(Props.getProperty("proxy.port"));
        jcboLog.removeAllItems();
        jcboLog.addItem(Logador.NIVEL.ERRO);
        jcboLog.addItem(Logador.NIVEL.INFO);
        jcboSim.removeAllItems();
        jcboSim.addItem("Flight Simulator (Via FSUIPC)");
        jcboSim.addItem("X-Plane (Via BtoUIPC)");
        try{
            jcboLog.setSelectedIndex(Integer.parseInt(Props.getProperty("log")));
            jcboSim.setSelectedIndex(Integer.parseInt(Props.getProperty("simulador")));
        }catch(Exception e){}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtxtBto = new javax.swing.JTextField();
        jtxtUrl = new javax.swing.JTextField();
        jtxtPirep = new javax.swing.JTextField();
        jtxtPosicao = new javax.swing.JTextField();
        jchkProxy = new javax.swing.JCheckBox();
        jtxtHost = new javax.swing.JTextField();
        jbGravar = new javax.swing.JButton();
        jtxtPort = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jcboLog = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jcboSim = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Matrícula:");

        jLabel4.setText("Url do BTOAcars:");

        jLabel5.setText("Pirep:");

        jLabel6.setText("Posição:");

        jLabel1.setText("Host:");

        jLabel2.setText("Porta:");

        jtxtBto.setText("BTO");

        jtxtUrl.setText("http://www.aeroboteco.com.br/va/fsacars/");

        jtxtPirep.setText("receive_pirep.php");

        jtxtPosicao.setText("position_update.php");

        jchkProxy.setText("Utilizar conexão via proxy");

        jtxtHost.setToolTipText("teste");

        jbGravar.setText("Gravar");
        jbGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGravarActionPerformed(evt);
            }
        });

        jtxtPort.setText("80");

        jLabel7.setText("Log:");

        jcboLog.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Simulador:");

        jcboSim.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbGravar)
                    .addComponent(jtxtBto, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jtxtPirep, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jtxtPosicao, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jchkProxy)
                    .addComponent(jtxtHost, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jtxtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jcboSim, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jcboLog, javax.swing.GroupLayout.Alignment.LEADING, 0, 161, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtBto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtPirep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtPosicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchkProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcboLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jcboSim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jbGravar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGravarActionPerformed
        Props.setProperty("matricula",jtxtBto.getText());
        Props.setProperty("url.acars",jtxtUrl.getText());
        Props.setProperty("pirep",jtxtPirep.getText());
        Props.setProperty("posicao",jtxtPosicao.getText());
        if (jchkProxy.isSelected()){
            Props.setProperty("proxy", "S");
            System.setProperty("http.proxyHost", jtxtHost.getText());
            System.setProperty("http.proxyPort", jtxtPort.getText());
        }else{
            Props.setProperty("proxy", "N");
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
        }
        Props.setProperty("proxy.host", jtxtHost.getText());
        Props.setProperty("proxy.port", jtxtPort.getText());
        Props.setProperty("log", String.valueOf(jcboLog.getSelectedIndex()));
        Props.setProperty("simulador", String.valueOf(jcboSim.getSelectedIndex()));
        dispose();
}//GEN-LAST:event_jbGravarActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConfigView cv=new ConfigView();
                cv.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton jbGravar;
    private javax.swing.JComboBox jcboLog;
    private javax.swing.JComboBox jcboSim;
    private javax.swing.JCheckBox jchkProxy;
    private javax.swing.JTextField jtxtBto;
    private javax.swing.JTextField jtxtHost;
    private javax.swing.JTextField jtxtPirep;
    private javax.swing.JTextField jtxtPort;
    private javax.swing.JTextField jtxtPosicao;
    private javax.swing.JTextField jtxtUrl;
    // End of variables declaration//GEN-END:variables

}
