package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Evento;
import br.com.aeroboteco.model.Geo;
import br.com.aeroboteco.model.Logador;
import javax.swing.JTable;
import br.com.aeroboteco.model.PV;
import br.com.aeroboteco.model.Props;
import br.com.aeroboteco.model.SimInterface;
import br.com.aeroboteco.model.Site;
import br.com.aeroboteco.model.UIPCFactory;
import br.com.aeroboteco.model.XML;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class LogView extends javax.swing.JFrame {

    private PV pv;
    private SimInterface uipc;
    private LogTM logTM;
    private Timer timer;
    private Timer timerPosition;
    private boolean gearDown = true;
    private boolean reinicio=false;
    private int segundosEmPausa;
    private double[] coordDep={0.0,0.0};
    private double[] coordArr={0.0,0.0};
    private long ete;
    private double distDep;
    private double distArr;
    private double[] ultimaPosicao={0.0,0.0};
    private double distVoada;

    public LogView(PV pv) {
        initComponents();
        setTitle("BTOAcars v"+java.util.ResourceBundle.getBundle("br.com.aeroboteco.model.BtoProp").getString("versao").toString());
        setIconImage(new ImageIcon("bto.png").getImage());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-getWidth())/2;
        int y = (screen.height-getHeight())/2;
        setBounds(x,y, getWidth(),getHeight());

        this.pv = pv;
        logTM = new LogTM(pv);
        jtblLog.setModel(logTM);
        jtblLog.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jtblLog.getColumnModel().getColumn(0).setPreferredWidth(70);
        jtblLog.getColumnModel().getColumn(1).setPreferredWidth(300);
        //Chamar programa de recepção de dados do FSUIPC/XPUIPC
        //uipc = new UIPC();
        uipc=UIPCFactory.getUIPC();

        startAcars();
    }

    public void startAcars() {
        //Dados atuais
        try{
            String[] cDep=Props.getCoordenadasICAO(pv.getDep());
            if (!cDep[0].equals("")){
                coordDep[0]=Double.parseDouble(cDep[1]);
                coordDep[1]=Double.parseDouble(cDep[2]);
            }
            String[] cArr=Props.getCoordenadasICAO(pv.getArr());
            if (!cArr[0].equals("")){
                coordArr[0]=Double.parseDouble(cArr[1]);
                coordArr[1]=Double.parseDouble(cArr[2]);
            }
        }catch(Exception e){Logador.getLogador().severe("Status atual:"+e.getMessage());}

        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm");
        jtxtDepArr.setText(pv.getDep() + "~" + pv.getArr());
        jtxtDistancia.setText(df.format(uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(this.coordDep[0], this.coordDep[1], this.coordArr[0], this.coordArr[1])))).replaceAll(",", ".")+"nm");

        logTM.addEvento(new Evento("","PV enviado: " + pv.getBto() + " " + pv.getDep() + "/" + pv.getArr()));
        logTM.addEvento(new Evento("Data: " + sdf.format(new Date())));
        logTM.addEvento(new Evento("","Aircraft Type: " + pv.getAcft()));
        
        //if (pv.getTour() != 0) {
        //    logTM.addEvento(new Evento("","Route: " + pv.getRoute() + " TOUR/"+pv.getTour()));
        //}else{
        //    logTM.addEvento(new Evento("","Route: " + pv.getRoute()));
        //}
        logTM.addEvento(new Evento("","Tour: "+pv.getTour()));
        logTM.addEvento(new Evento("","Route: " + pv.getRoute()));
        logTM.addEvento(new Evento("","BtoAcars v"+java.util.ResourceBundle.getBundle("br.com.aeroboteco.model.BtoProp").getString("versao").toString()));


        jbParar.setEnabled(true);
        jbEnviar.setEnabled(false);
        jbSalvar.setEnabled(false);
        jbRejeitar.setEnabled(false);

        timer = new Timer();

        timer.schedule(new TimerTask() {

            boolean iniciouVoo = false;
            int simRate=1;

            public void run() {
                DecimalFormat df = new DecimalFormat("0.00");
                if (uipc.isEmVoo()) {
                    if (!iniciouVoo) {
                        pv.setLatDe(uipc.getLat());
                        pv.setLonDe(uipc.getLon());
                        //Variaveis usadas no acumulador de dist.voada
                        ultimaPosicao[0]=pv.getLatDe();
                        ultimaPosicao[1]=pv.getLonDe();
                        logTM.addEvento("Takeoff: " + uipc.getIAS() + "kt");
                        if (!reinicio){
                            pv.setDataHoraInicio(new Date());
                            pv.setDataHoraFim(pv.getDataHoraInicio());
                            logTM.addEvento("Lat/Lon: " + pv.getLatDe() + "/" + pv.getLonDe());
                            logTM.addEvento("ZFW: " + uipc.getZFW()+"lbs");
                            logTM.addEvento("HDG: " + uipc.getHDG());
                            logTM.addEvento("Wind: " + uipc.getVentoDirecao()+"/"+uipc.getVentoVelocidade()+"kt");
                        }
                        jbParar.setEnabled(true);

                        //enviar posição atual
                        sendPosition(Site.FASE.DECOLANDO);

                        //Agendar envio de posição atual (5min.)
                        timerPosition = new Timer();
                        timerPosition.schedule(new TimerTask() {
                            int t=0;
                            public void run() {
                                //pv.addDataHoraFim(60);
                                sendPosition(getFase(uipc.getVS()));
                                //A cada 15min.
                                if (t++>=14){
                                    try{
                                    //Acumular dist. voada
                                    distVoada+=uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(ultimaPosicao[0], ultimaPosicao[1], pv.getLatAte(), pv.getLonAte())));
                                    ultimaPosicao[0]=pv.getLatAte();
                                    ultimaPosicao[1]=pv.getLonAte();

                                    //Log
                                    logTM.addEvento("Lat/Lon: " + pv.getLatAte() + "/" + pv.getLonAte());
                                    logTM.addEvento("Wind: " + uipc.getVentoDirecao()+"/"+uipc.getVentoVelocidade()+"kt");
                                    logTM.addEvento("GS: " + Math.round(uipc.getGS()) + "kt");
                                    logTM.addEvento("HDG: " + uipc.getHDG());
                                    t=0;
                                    }catch(Exception e){
                                        Logador.getLogador().severe("SendPosition: "+e.getMessage());
                                    }
                                }
                            }
                        }, 60000, 60000);
                    } else {
                        //Atualizar dados atuais
                        DecimalFormat df2=new DecimalFormat("0.0000");
                        try{
                            jtxtPosicaoAtual.setText(
                                    String.valueOf(df2.format(uipc.getLat()))
                                    +","+
                                    String.valueOf(df2.format(uipc.getLon()))
                                    );
                            jtxtGSAtual.setText(String.valueOf(Math.round(uipc.getGS())));
                            jtxtAltAtual.setText(String.valueOf(uipc.getALT()));
                            jtxtStatus.setText(getFase(uipc.getVS()).name());
                            
                            distDep=uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(uipc.getLat(), uipc.getLon(), coordDep[0], coordDep[1])));
                            distArr=uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(uipc.getLat(), uipc.getLon(), coordArr[0], coordArr[1])));
                            ete=Math.round(60.0*(uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(uipc.getLat(), uipc.getLon(), coordArr[0], coordArr[1])))/uipc.getGS()));

                            jtxtDistanciaOrigem.setText(df.format(distDep).replaceAll(",", ".")+"nm");
                            jtxtDistDestino.setText(df.format(distArr).replaceAll(",", ".")+"nm");
                            jtxtETE.setText(String.valueOf(ete)+"min.");
                        }catch(Exception e){Logador.getLogador().severe("Status atual:"+e.getMessage());}

                        if (gearDown != uipc.isGearDown()) {
                            if (uipc.isGearDown()) {
                                logTM.addEvento("Gear Down: " + uipc.getIAS() + " kt, " + uipc.getALT() + " ft");
                            } else {
                                logTM.addEvento("Gear UP: " + uipc.getIAS() + " kt, " + uipc.getALT() + " ft");
                            }
                            gearDown = uipc.isGearDown();
                        }
                    }
                    iniciouVoo = true;
                    pv.setTouchDownRate(uipc.getVS());
                    pv.setTouchDownIAS(uipc.getIAS());
                    pv.setLatAte(uipc.getLat());
                    pv.setLonAte(uipc.getLon());
                } else {
                    if (iniciouVoo) {
                        if (uipc.getTAS() <= 0) {
                            //Cancelar timer de posição

                            logTM.addEvento("TouchDown:Rate " + pv.getTouchDownRate() + " ft/min Speed: " + pv.getTouchDownIAS() + " Knots");
                            sendPosition(Site.FASE.POUSADO);

                            //Cancelar timers
                            timerPosition.cancel();
                            timer.cancel();

                            String[] icao=Props.getCoordenadasICAO(pv.getArr());
                            boolean localCorreto=true;
                            if (icao!=null && icao[0]!=null && !icao[0].equals("")){
                                if (Geo.estouNoIcaoCerto(pv.getArr(), uipc.getLat(), uipc.getLon())){
                                    localCorreto=true;
                                }else{
                                    localCorreto=false;
                                }
                            }
                            if (localCorreto){
                                pv.setDataHoraFim(new Date());

                                /*Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR, 0);
                                c.set(Calendar.MINUTE, 0);
                                c.set(Calendar.SECOND, 0);
                                c.add(Calendar.MINUTE, (int) ((pv.getDataHoraFim().getTime() - pv.getDataHoraInicio().getTime()) / 1000 / 60));
                                //Descontar o tempo em pausa
                                c.add(Calendar.SECOND, segundosEmPausa*-1);
                                pv.setDuracao(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));*/
                                pv.setDuracao(getDuracao(pv.getDataHoraInicio(), pv.getDataHoraFim(), segundosEmPausa));
                                logTM.addEvento("Tempo de voo: " + pv.getDuracao() + " horas");
                                logTM.addEvento("Tempo em pausa: " + segundosEmPausa + "s");
                                logTM.addEvento("Lat/Lon: " + pv.getLatAte() + "/" + pv.getLonAte());
                                pv.setDistancia(Double.parseDouble(df.format(uipc.arredondar(Geo.getKmEmNm(Geo.getDistancia(pv.getLatDe(), pv.getLonDe(), pv.getLatAte(), pv.getLonAte())))).replaceAll(",", ".")));
                                logTM.addEvento("Distância percorrida: " + distVoada + "nm");
                                logTM.addEvento("Distância DEP/ARR: " + pv.getDistancia() + "nm");
                                logTM.addEvento("Pouso no destino correto! Bem-vindo a " + pv.getArr());
                                if (icao==null || icao[0]==null || icao[0].equals("")){
                                    logTM.addEvento("   (destino não encontrado no BD)");
                                }
                                jbParar.setEnabled(false);
                                jbEnviar.setEnabled(true);
                                jbSalvar.setEnabled(true);
                                jbRejeitar.setEnabled(true);
                            }else{
                                logTM.addEvento("Pouso FORA do destino informado! siga para " + pv.getArr());
                                reinicio=true;
                                startAcars();
                            }
                        }
                    }
                }
                //Acumular o tempo em que o simulador estiver em pausa
                if (uipc.isEmPausa()) segundosEmPausa++;
                //ignorar simrate maior que 32X (PMDG envia coisas absurdamente grandes)
                if (uipc.getSimRate()>0 && uipc.getSimRate()<32){
                    if (simRate!=uipc.getSimRate()){
                        simRate=uipc.getSimRate();
                        logTM.addEvento("SIM RATE: " + simRate+"X");
                    }
                }
            }
        }, 1000, 1000);
    }

    private Site.FASE getFase(long vs){
        if (vs>200){
            return Site.FASE.SUBINDO;
        }else if (vs<-200){
            return Site.FASE.DESCENDO;
        }else{
            return Site.FASE.CRUZEIRO;
        }
    }
    public void sendAcars() {
        jbEnviar.setEnabled(false);
        jbSalvar.setEnabled(false);
        if (Site.sendAcars(pv)) {
            JOptionPane.showMessageDialog(null, "Pirep enviado com sucesso!");
            jbEnviar.setEnabled(true);
            jbSalvar.setEnabled(true);
            voltar();
        } else {
            JOptionPane.showMessageDialog(null, "ERRO ao enviar o Pirep. Tente novamente ou clique em ENVIAR DEPOIS");
            jbEnviar.setEnabled(true);
            jbSalvar.setEnabled(true);
        }
    }

    public void sendPosition(Site.FASE fase) {
        Calendar c=Calendar.getInstance();
        //c.set(Calendar.DAY_OF_MONTH,1);
        //c.set(Calendar.MONTH,0);
        //c.set(Calendar.YEAR,2009);
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.add(Calendar.MINUTE, (int)ete);
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String tempo=String.format("%02d", c.get(Calendar.HOUR))+":"+String.format("%02d", c.get(Calendar.MINUTE))+":"+String.format("%02d", c.get(Calendar.SECOND));
        Site.sendPosition(pv, uipc.getLat(), uipc.getLon(), Math.round(uipc.getGS()), uipc.getALT(), fase,distDep, distArr, tempo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jbEnviar = new javax.swing.JButton();
        jbParar = new javax.swing.JButton();
        jbSalvar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jtxtPosicaoAtual = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtStatus = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtGSAtual = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtAltAtual = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtxtDepArr = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtxtDistancia = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtDistDestino = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtETE = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtDistanciaOrigem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblLog = new javax.swing.JTable();
        jbRejeitar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Flight Log");

        jbEnviar.setText("ENVIAR>>");
        jbEnviar.setEnabled(false);
        jbEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviarActionPerformed(evt);
            }
        });

        jbParar.setText("Parar voo");
        jbParar.setEnabled(false);
        jbParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPararActionPerformed(evt);
            }
        });

        jbSalvar.setText("Enviar Depois");
        jbSalvar.setEnabled(false);
        jbSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarActionPerformed(evt);
            }
        });

        jLabel2.setText("Posição Atual (Lat,Lon):");

        jLabel3.setText("Status:");

        jLabel4.setText("GS:");

        jLabel5.setText("Alt:");

        jLabel6.setText("Dep~Arr:");

        jtxtDepArr.setText("SBSP~SBRJ");

        jLabel7.setText("Distância:");

        jtxtDistancia.setText("250nm");

        jLabel8.setText("Dist Arr:");

        jLabel9.setText("Tempo estimado:");

        jLabel10.setText("Dist Dep:");

        jtblLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblLog);

        jbRejeitar.setText("Rejeitar voo");
        jbRejeitar.setEnabled(false);
        jbRejeitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRejeitarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jtxtDepArr, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(46, 46, 46))
                                    .addComponent(jtxtDistancia, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jtxtGSAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jtxtAltAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addComponent(jLabel3)))
                            .addComponent(jtxtPosicaoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtDistanciaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jtxtDistDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(1, 1, 1))
                                    .addComponent(jtxtETE, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jbSalvar)
                                    .addGap(18, 18, 18)
                                    .addComponent(jbRejeitar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jbEnviar))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jbParar))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtDepArr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDistancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtGSAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtAltAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtPosicaoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtDistanciaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtETE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDistDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbParar)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbEnviar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbSalvar)
                        .addComponent(jbRejeitar)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPararActionPerformed
        timer.cancel();
        logTM.addEvento("Voo cancelado pelo piloto");
        pv.setDataHoraFim(new Date());
        pv.setDuracao(getDuracao(pv.getDataHoraInicio(), pv.getDataHoraFim(), segundosEmPausa));
        logTM.addEvento("Tempo de voo: " + pv.getDuracao() + " horas");
        logTM.addEvento("Tempo em pausa: " + segundosEmPausa + "s");
        
        jbEnviar.setEnabled(true);
        jbSalvar.setEnabled(true);
        jbRejeitar.setEnabled(true);
    }//GEN-LAST:event_jbPararActionPerformed

    private void jbEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviarActionPerformed
        sendAcars();
    }//GEN-LAST:event_jbEnviarActionPerformed

    private void jbSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarActionPerformed
        XML.gravaPirep(pv);
        voltar();
    }//GEN-LAST:event_jbSalvarActionPerformed

    private void jbRejeitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRejeitarActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Confirma a exclusão deste voo?", "", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            return;
        }else{
            voltar();
        }
}//GEN-LAST:event_jbRejeitarActionPerformed

    private void voltar(){
        new PirepView(true).setVisible(true);
        this.dispose();
    }

    private String getDuracao(Date de, Date ate, int segDesconto){
        String duracao="00:00";
        int minutos=(int) ((ate.getTime() - de.getTime()) / 1000 / 60);
        //if (minutos>0){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.YEAR, 2009);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);

            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(c.getTimeInMillis());

            c2.add(Calendar.MINUTE, minutos);
            c2.add(Calendar.SECOND, segDesconto*-1);

            //Se o tempo descontado ultrapassar a qtde de horas voadas, zera o contador
            if (c2.getTimeInMillis()<c.getTimeInMillis()){
                c2.setTimeInMillis(c.getTimeInMillis());
            }

            duracao=String.valueOf((c2.get(Calendar.DAY_OF_MONTH)-1)*24 + c2.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d",c2.get(Calendar.MINUTE)));
        //}
        return duracao;
    }

    public static void main(String args[]) {
        Date d1;
        Date d2;
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 10);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.YEAR, 2010);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 12);
        c.set(Calendar.SECOND, 28);
        d1=new Date(c.getTimeInMillis());
        c.set(Calendar.DAY_OF_MONTH, 10);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.YEAR, 2010);
        c.set(Calendar.HOUR_OF_DAY, 2);
        c.set(Calendar.MINUTE, 12);
        c.set(Calendar.SECOND, 28);
        d2=new Date(c.getTimeInMillis());

        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d1));
        System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d2));

        System.out.println("Duracao: "+new LogView(new PV()).getDuracao(d1, d2, 3801));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbEnviar;
    private javax.swing.JButton jbParar;
    private javax.swing.JButton jbRejeitar;
    private javax.swing.JButton jbSalvar;
    private javax.swing.JTable jtblLog;
    private javax.swing.JTextField jtxtAltAtual;
    private javax.swing.JTextField jtxtDepArr;
    private javax.swing.JTextField jtxtDistDestino;
    private javax.swing.JTextField jtxtDistancia;
    private javax.swing.JTextField jtxtDistanciaOrigem;
    private javax.swing.JTextField jtxtETE;
    private javax.swing.JTextField jtxtGSAtual;
    private javax.swing.JTextField jtxtPosicaoAtual;
    private javax.swing.JTextField jtxtStatus;
    // End of variables declaration//GEN-END:variables
}
