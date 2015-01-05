package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Ad;
import br.com.aeroboteco.model.Geo;
import br.com.aeroboteco.model.Logador;
import br.com.aeroboteco.model.PV;
import br.com.aeroboteco.model.PernaTour;
import br.com.aeroboteco.model.Props;
import br.com.aeroboteco.model.SimInterface;
import br.com.aeroboteco.model.Site;
import br.com.aeroboteco.model.Tour;
import br.com.aeroboteco.model.UIPCFactory;
import br.com.aeroboteco.model.XML;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class PirepView extends javax.swing.JFrame implements PesquisadorAcft{
    private Tour tourSelecionado;

    public PirepView(boolean reabertura) {
        config(reabertura);
    }
    public PirepView() {
        config(false);
    }
    private void config(boolean reabertura){
        if (!reabertura){
            Logador.getLogador().log(Level.INFO,"pirepview setLookAndFeel");
            try {
                // Configura o Look and feel Nimbus
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            }
            catch (Exception e) {
                Logador.getLogador().log(Level.WARNING,"pirepview LAF",e);
            }
        }
        Logador.getLogador().log(Level.INFO,"pirepview initComponents");
        initComponents();

        if (!reabertura){
            Logador.getLogador().log(Level.INFO,"pirepview UIPC OK?");
            if (!UIPCFactory.isOK()){
                Logador.getLogador().log(Level.INFO,"pirepview Desabilitar enviar");
                jbEnviar.setEnabled(false);
            }
        }

        Logador.getLogador().log(Level.INFO,"pirepview dimension");
        /**/
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-getWidth())/2;
        int y = (screen.height-getHeight())/2;
        setBounds(x,y, getWidth(),getHeight());
        setTitle("BTOAcars v"+java.util.ResourceBundle.getBundle("br.com.aeroboteco.model.BtoProp").getString("versao").toString());

        String versaoSite=Site.versao();
        String v[]=versaoSite.split("\\.");
        String v2[]=java.util.ResourceBundle.getBundle("br.com.aeroboteco.model.BtoProp").getString("versao").toString().split("\\.");
        int vSite=0;
        try{
            vSite=Integer.parseInt(v[0])*1000;
            vSite+=Integer.parseInt(v[1]);
        }catch(NumberFormatException e){
            Logador.getLogador().log(Level.INFO,"Erro de comunicacao. Versao nao checada");
        }
        if (vSite!=0){
            int vLocal=Integer.parseInt(v2[0])*1000;
            vLocal+=Integer.parseInt(v2[1]);
            if (vLocal<vSite){
                Logador.getLogador().log(Level.INFO,"versao diferente");
                JOptionPane.showMessageDialog(null, "Existe uma nova versão do BTOAcars ("+versaoSite+"). Acesse o fórum Aeroboteco para mais detalhes e download.");
                JOptionPane.showMessageDialog(null, "O BTOAcars será finalizado.");
                System.exit(0);
                return;
            }
        }
        /*if (v.equals("")){
            Logador.getLogador().log(Level.INFO,"Erro de comunicacao. Versao nao checada");
        }else{
            if (!v.equals(java.util.ResourceBundle.getBundle("br.com.aeroboteco.model.BtoProp").getString("versao").toString())){
                Logador.getLogador().log(Level.INFO,"versao diferente");
                JOptionPane.showMessageDialog(null, "Existe uma nova versão do BTOAcars ("+v+"). Acesse o fórum Aeroboteco para mais detalhes e download.");
                JOptionPane.showMessageDialog(null, "O BTOAcars será finalizado.");
                System.exit(0);
                return;
            }
        }*/

        setIconImage(new ImageIcon("bto.png").getImage());

        Logador.getLogador().log(Level.INFO,"pirepview Props");
        jtxtBto.setText(Props.getProperty("matricula"));
        //Carregar proxy
        if (Props.getProperty("proxy").equalsIgnoreCase("S")){
            System.setProperty("http.proxyHost", Props.getProperty("proxy.host"));
            System.setProperty("http.proxyPort", Props.getProperty("proxy.port"));
        }

        jbEnviarPirepsGravados.setText("Não há pireps gravados");
        Logador.getLogador().log(Level.INFO,"pirepview acars exists?");
        if (new File("acars").exists()){
            Logador.getLogador().log(Level.INFO,"pirepview leitura acars");
            List<PV>pvs=XML.le("acars");
            jbEnviarPirepsGravados.setText("Enviar Pireps gravados( "+pvs.size()+")");
            jbEnviarPirepsGravados.setEnabled(true);
        }
        Logador.getLogador().log(Level.INFO,"pirepview carregatours");
        carregaTours();
    }

    private void downloadTour(){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("tour.xml"));
            out.write(Site.carregaTour());
            out.flush();
            out.close();
        }catch(IOException e){Logador.getLogador().log(Level.SEVERE,"downloadTour()",e);}
        carregaTours();
    }
    private void carregaTours(){
        Logador.getLogador().log(Level.INFO,"pirepview tour.xml exists?");
        if (new File("tour.xml").exists()){
            Logador.getLogador().log(Level.INFO,"pirepview lerTour");
            jcboTour.removeAllItems();
            jcboTour.addItem("<Nenhum>");
            for (Tour t:XML.leTours()){
                jcboTour.addItem(t);
            }
        }else{
            Logador.getLogador().log(Level.INFO,"pirepview downloadTour");
            downloadTour();
        }
        Logador.getLogador().log(Level.INFO,"pirepview pirepsEnviados");
        Site.pirepsEnviados(jtxtBto.getText());
        Logador.getLogador().log(Level.INFO,"pirepview ok");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtxtBto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtxtDep = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtArr = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtAlt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtFl = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtxtAcft = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jbEnviar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtRoute = new javax.swing.JTextArea();
        jbEnviarPirepsGravados = new javax.swing.JButton();
        jcboTour = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jcboPerna = new javax.swing.JComboBox();
        jbAcft = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmnuProxy = new javax.swing.JMenuItem();
        jmnuCarregaTours = new javax.swing.JMenuItem();
        jmnuAD = new javax.swing.JMenuItem();
        jmnuSobre = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BTOAcars");

        jLabel1.setText("Matrícula do piloto:");

        jtxtBto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtxtBto.setText("BTO5102");
        jtxtBto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtBtoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtBtoFocusLost(evt);
            }
        });

        jLabel2.setText("AD Partida:");

        jtxtDep.setToolTipText("Teste");
        jtxtDep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtDepFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtDepFocusLost(evt);
            }
        });

        jLabel3.setText("AD Destino:");

        jtxtArr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtArrFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtArrFocusLost(evt);
            }
        });

        jLabel4.setText("AD Altern:");

        jtxtAlt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAltFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtAltFocusLost(evt);
            }
        });

        jLabel5.setText("FL:");

        jtxtFl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtFlFocusGained(evt);
            }
        });

        jLabel6.setText("Aeronave:");

        jtxtAcft.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAcftFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtAcftFocusLost(evt);
            }
        });

        jLabel7.setText("Tour:");

        jLabel8.setText("Rota:");

        jbEnviar.setText("Iniciar voo");
        jbEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviarActionPerformed(evt);
            }
        });

        jtxtRoute.setColumns(20);
        jtxtRoute.setLineWrap(true);
        jtxtRoute.setRows(5);
        jtxtRoute.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtRouteFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(jtxtRoute);

        jbEnviarPirepsGravados.setText("Enviar Pireps gravados");
        jbEnviarPirepsGravados.setEnabled(false);
        jbEnviarPirepsGravados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviarPirepsGravadosActionPerformed(evt);
            }
        });

        jcboTour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcboTour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboTourActionPerformed(evt);
            }
        });

        jLabel9.setText("Perna:");

        jcboPerna.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcboPerna.setEnabled(false);
        jcboPerna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPernaActionPerformed(evt);
            }
        });

        jbAcft.setText("?");
        jbAcft.setToolTipText("Clique aqui para selecionar a aeronave ou digite o ICAO no campo ao lado");
        jbAcft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAcftActionPerformed(evt);
            }
        });

        jMenu1.setMnemonic('o');
        jMenu1.setText("Opções");

        jmnuProxy.setMnemonic('c');
        jmnuProxy.setText("Configurações");
        jmnuProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuProxyActionPerformed(evt);
            }
        });
        jMenu1.add(jmnuProxy);

        jmnuCarregaTours.setMnemonic('t');
        jmnuCarregaTours.setText("Carregar Tours");
        jmnuCarregaTours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuCarregaToursActionPerformed(evt);
            }
        });
        jMenu1.add(jmnuCarregaTours);

        jmnuAD.setText("Atualizar Aeroportos");
        jmnuAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuADActionPerformed(evt);
            }
        });
        jMenu1.add(jmnuAD);

        jMenuBar1.add(jMenu1);

        jmnuSobre.setMnemonic('s');
        jmnuSobre.setText("Sobre");
        jmnuSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmnuSobreMouseClicked(evt);
            }
        });
        jMenuBar1.add(jmnuSobre);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtBto, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbEnviarPirepsGravados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbEnviar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcboPerna, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcboTour, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jtxtDep, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtArr)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtxtAlt, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtFl, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtxtAcft, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jbAcft))
                            .addComponent(jLabel6)))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtBto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcboTour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jcboPerna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtDep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtAcft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAcft)
                    .addComponent(jtxtAlt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtFl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbEnviar)
                    .addComponent(jbEnviarPirepsGravados))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviarActionPerformed
        if (jtxtBto.getText().trim().equals("") || jtxtBto.getText().trim().equals("BTO")){
            JOptionPane.showMessageDialog(null, "Preencha o campo Matrícula do piloto com seu BTO (Ex.: BTO5102)");
            return;
        }
        if (jtxtDep.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Preencha o campo AD Partida com o código ICAO do aeródromo de partida (Ex.: SBMT)");
            return;
        }
        if (jtxtArr.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Preencha o campo AD Destino com o código ICAO do aeródromo de destino (Ex.: SBST)");
            return;
        }
        if (jtxtAlt.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Preencha o campo AD Altern com o código ICAO do aeródromo alternativo (Ex.: SBMT)");
            return;
        }
        try{
            if (jtxtFl.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "Preencha o campo FL com o nível de voo (Ex.: 140)");
                return;
            }
            if (Integer.parseInt(jtxtFl.getText().trim())<=0){
                JOptionPane.showMessageDialog(null, "Nível de voo inválido! (Informe apenas números inteiros)");
                return;
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Nível de voo inválido! (Informe apenas números inteiros)");
            return;
        }
        if (jtxtAcft.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Preencha o campo Aeronave com o ICAO da aeronave usada (Ex.: C172)");
            return;
        }

        boolean adDesconhecido=false;
        String icaoDep="";
        String icaoArr="";

        String icao[]=Props.getCoordenadasICAO(jtxtDep.getText());
        if (icao==null || icao[0]==null || icao[0].equals("")){
            //if (JOptionPane.showConfirmDialog(null, "O AD de partida ["+jtxtDep.getText()+"] não consta do banco de dados. Confirma mesmo assim?","ICAO Desconhecido",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            if (JOptionPane.showConfirmDialog(null, "O AD de partida ["+jtxtDep.getText()+"] não consta do banco de dados local. Deseja que o BTOAcars se conecte ao banco de dados Aeroboteco?","ICAO Desconhecido",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                String[] apt=Site.carregaApt(jtxtDep.getText());
                if (!apt[0].equals("")){
                    //Gravar o novo ICAO no DB local
                    Props.setCoordenadasICAO(jtxtDep.getText(), apt[1]+";"+apt[2]+";"+apt[3]);
                    //Reprocessar
                    jbEnviarActionPerformed(evt);
                }else{
                    JOptionPane.showMessageDialog(null, "O AD de partida ["+jtxtDep.getText()+"] tmabém não consta do banco de dados Aeroboteco. Informe o Staff.");
                }
                return;
            }else{adDesconhecido=true;}
        }else{
            icaoDep=icao[0];
        }
        icao=Props.getCoordenadasICAO(jtxtArr.getText());
        if (icao==null || icao[0]==null || icao[0].equals("")){
            //if (JOptionPane.showConfirmDialog(null, "O AD de destino ["+jtxtArr.getText()+"] não consta do banco de dados. Confirma mesmo assim?","ICAO Desconhecido",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            if (JOptionPane.showConfirmDialog(null, "O AD de destino ["+jtxtArr.getText()+"] não consta do banco de dados local. Deseja que o BTOAcars se conecte ao banco de dados Aeroboteco?","ICAO Desconhecido",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                String[] apt=Site.carregaApt(jtxtArr.getText());
                if (!apt[0].equals("")){
                    //Gravar o novo ICAO no DB local
                    Props.setCoordenadasICAO(jtxtArr.getText(), apt[1]+";"+apt[2]+";"+apt[3]);
                    //Reprocessar
                    jbEnviarActionPerformed(evt);
                }else{
                    JOptionPane.showMessageDialog(null, "O AD de destino ["+jtxtArr.getText()+"] tmabém não consta do banco de dados Aeroboteco. Informe o Staff.");
                }
                return;
            }else{adDesconhecido=true;}
        }else{
            icaoArr=icao[0];
        }

        if (!adDesconhecido){
            SimInterface uipc=UIPCFactory.getUIPC();
            if (!Geo.estouNoIcaoCerto(jtxtDep.getText(), uipc.getLat(), uipc.getLon())){
                if (JOptionPane.showConfirmDialog(null, "Você não está no AD de partida informado em seu plano de voo. ["+jtxtDep.getText()+"]. Quer iniciar o voo mesmo assim?","ICAO Desconhecido",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
                    //JOptionPane.showMessageDialog(null, "Você não está no AD de partida informado em seu plano de voo. ["+jtxtDep.getText()+"]");
                    return;
                }
            }
        }

        PV pv=new PV();
        pv.setBto(jtxtBto.getText());
        pv.setDep(jtxtDep.getText());
        pv.setDepNome(icaoDep);
        pv.setArr(jtxtArr.getText());
        pv.setArrNome(icaoArr);
        pv.setAlt(jtxtAlt.getText());
        pv.setFl(Integer.parseInt(jtxtFl.getText()));
        pv.setAcft(jtxtAcft.getText());
        pv.setRoute(jtxtRoute.getText());
        pv.setTour(0);
        if (tourSelecionado!=null){
            PernaTour pt=(PernaTour)jcboPerna.getItemAt(jcboPerna.getSelectedIndex());
            if (!pt.toString().contains("Aprovado") && !pt.toString().contains("Enviado")){
                pv.setTour(tourSelecionado.getCodigo());
            }
        }

        Logador.getLogador().info("BTO: "+pv.getBto()+
                "Dep: "+pv.getDep()+
                "DepNome: "+pv.getDepNome()+
                "Arr: "+pv.getArr()+
                "ArrNome: "+pv.getArrNome()+
                "Alt: "+pv.getAlt()+
                "Fl: "+pv.getFl()+
                "Acft: "+pv.getAcft()+
                "Route: "+pv.getRoute()+
                "Tour: "+pv.getTour()
                );

        //Gravar ultimo pirep
        //XML.gravaPirep(pv, "upv.xml");

        LogView lv=new LogView(pv);
        lv.setVisible(true);
        //setVisible(false);
        dispose();

}//GEN-LAST:event_jbEnviarActionPerformed

    private void jbEnviarPirepsGravadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviarPirepsGravadosActionPerformed
        List<PV>pvs=XML.le();
        boolean erro=false;
        for (PV pv:pvs){
            Object[] options = { "Sim", "Não"};
            int opcao = JOptionPane.showOptionDialog(null, "Enviar pirep: "+pv.getDep()+"-"+pv.getArr()+" Duração:"+pv.getDuracao(), "Enviar?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (opcao==0){
                if (!Site.sendAcars(pv)){
                    XML.gravaPirep(pv, "acars_err.xml");
                    erro=true;
                }
            }
        }
        if (erro){
            JOptionPane.showMessageDialog(null, "Um ou mais pireps gravados não puderam ser enviados!");
        }
        new File("acars").delete();
        jbEnviarPirepsGravados.setEnabled(false);
        JOptionPane.showMessageDialog(null, "Pireps enviados!");
    }//GEN-LAST:event_jbEnviarPirepsGravadosActionPerformed

    private void jmnuSobreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jmnuSobreMouseClicked
        SplashView sv=new SplashView(5000);
        sv.showSplash(false);
    }//GEN-LAST:event_jmnuSobreMouseClicked

    private void jtxtBtoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBtoFocusLost
        jtxtBto.setText(jtxtBto.getText().toUpperCase());
    }//GEN-LAST:event_jtxtBtoFocusLost

    private void jtxtDepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtDepFocusLost
        jtxtDep.setText(jtxtDep.getText().toUpperCase());
    }//GEN-LAST:event_jtxtDepFocusLost

    private void jtxtArrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtArrFocusLost
        jtxtArr.setText(jtxtArr.getText().toUpperCase());
    }//GEN-LAST:event_jtxtArrFocusLost

    private void jtxtAltFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAltFocusLost
        jtxtAlt.setText(jtxtAlt.getText().toUpperCase());
    }//GEN-LAST:event_jtxtAltFocusLost

    private void jtxtAcftFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAcftFocusLost
        jtxtAcft.setText(jtxtAcft.getText().toUpperCase());
    }//GEN-LAST:event_jtxtAcftFocusLost

    private void jtxtBtoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBtoFocusGained
        jtxtBto.selectAll();
    }//GEN-LAST:event_jtxtBtoFocusGained

    private void jtxtDepFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtDepFocusGained
        jtxtDep.selectAll();
    }//GEN-LAST:event_jtxtDepFocusGained

    private void jtxtArrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtArrFocusGained
        jtxtArr.selectAll();
    }//GEN-LAST:event_jtxtArrFocusGained

    private void jtxtAltFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAltFocusGained
        jtxtAlt.selectAll();
    }//GEN-LAST:event_jtxtAltFocusGained

    private void jtxtFlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtFlFocusGained
        jtxtFl.selectAll();
    }//GEN-LAST:event_jtxtFlFocusGained

    private void jtxtAcftFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAcftFocusGained
        jtxtAcft.selectAll();
    }//GEN-LAST:event_jtxtAcftFocusGained

    private void jmnuCarregaToursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuCarregaToursActionPerformed
        downloadTour();
}//GEN-LAST:event_jmnuCarregaToursActionPerformed

    private void jcboTourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboTourActionPerformed
        tourSelecionado=null;
        if (jcboTour.getItemAt(jcboTour.getSelectedIndex())==null){return;}
        if (jcboTour.getItemAt(jcboTour.getSelectedIndex())!="<Nenhum>"){
            tourSelecionado=(Tour)jcboTour.getItemAt(jcboTour.getSelectedIndex());
            jcboPerna.setEnabled(true);
            jcboPerna.removeAllItems();
            Tour t=(Tour)jcboTour.getItemAt(jcboTour.getSelectedIndex());
            for (PernaTour pt:t.getPernas()){
                jcboPerna.addItem(pt);
            }
        }else{
            jcboPerna.setEnabled(false);
        }
    }//GEN-LAST:event_jcboTourActionPerformed

    private void jcboPernaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPernaActionPerformed
        if (jcboPerna.getItemAt(jcboPerna.getSelectedIndex())==null)return;
        PernaTour pt=(PernaTour)jcboPerna.getItemAt(jcboPerna.getSelectedIndex());
        jtxtDep.setText(pt.getOrigem());
        jtxtArr.setText(pt.getDestino());
        jtxtAlt.setText(pt.getOrigem());
}//GEN-LAST:event_jcboPernaActionPerformed

    private void jmnuProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuProxyActionPerformed
        new ConfigView().setVisible(true);
    }//GEN-LAST:event_jmnuProxyActionPerformed

    private void jtxtRouteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtRouteFocusLost
        jtxtRoute.setText(jtxtRoute.getText().toUpperCase());
    }//GEN-LAST:event_jtxtRouteFocusLost

    private void jbAcftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAcftActionPerformed
        new SelAcftView(this).setVisible(true);
    }//GEN-LAST:event_jbAcftActionPerformed

    private void jmnuADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuADActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Confirma a atualização da lista de aeroportos?","Atualização de ADs",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            for (Ad ad:XML.leIcaos(Site.carregaApt())){
                Props.setCoordenadasICAO(ad.getIcao(), ad.getNome()+";"+ad.getLat()+";"+ad.getLon());
            }
        }
        JOptionPane.showMessageDialog(null, "Lista de aeroportos atualizada!");
    }//GEN-LAST:event_jmnuADActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PirepView().setVisible(true);
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAcft;
    private javax.swing.JButton jbEnviar;
    private javax.swing.JButton jbEnviarPirepsGravados;
    private javax.swing.JComboBox jcboPerna;
    private javax.swing.JComboBox jcboTour;
    private javax.swing.JMenuItem jmnuAD;
    private javax.swing.JMenuItem jmnuCarregaTours;
    private javax.swing.JMenuItem jmnuProxy;
    private javax.swing.JMenu jmnuSobre;
    private javax.swing.JTextField jtxtAcft;
    private javax.swing.JTextField jtxtAlt;
    private javax.swing.JTextField jtxtArr;
    private javax.swing.JTextField jtxtBto;
    private javax.swing.JTextField jtxtDep;
    private javax.swing.JTextField jtxtFl;
    private javax.swing.JTextArea jtxtRoute;
    // End of variables declaration//GEN-END:variables

    public void setAcft(String acft) {
        jtxtAcft.setText(acft);
    }

}
