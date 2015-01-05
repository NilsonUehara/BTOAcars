package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Props;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class SelAcftView extends javax.swing.JFrame{
    PesquisadorAcft quem;
    public SelAcftView(PesquisadorAcft quem) {
        initComponents();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-getWidth())/2;
        int y = (screen.height-getHeight())/2;
        setBounds(x,y, getWidth(),getHeight());
        setTitle("Seleção de Aeronave");
        setIconImage(new ImageIcon("bto.png").getImage());

        this.quem=quem;
        
        montaGrid();
        ((AcftTM)jtblAcft.getModel()).ordem(0);
        final JTableHeader h= jtblAcft.getTableHeader();
        h.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    final int colIndex = h.columnAtPoint(new Point(e.getX(), e.getY()));
                    //System.out.println("click:"+colIndex);
                    ((AcftTM)jtblAcft.getModel()).ordem(colIndex);
                }
            }
        );
    }

    private void montaGrid(){
        jtblAcft.setModel(new AcftTM());
        jtblAcft.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jtblAcft.getColumnModel().getColumn(0).setPreferredWidth(70);
        jtblAcft.getColumnModel().getColumn(1).setPreferredWidth(150);
        jtblAcft.getColumnModel().getColumn(2).setPreferredWidth(300);
    }

    public void refresh(){
        this.setEnabled(true);
        //((AcftTM)jtblAcft.getModel()).refresh();
        //jtblAcft.setModel(new AcftTM());
        //((AcftTM)jtblAcft.getModel()).ordem(0);
        //jtblAcft.repaint();
        montaGrid();
        ((AcftTM)jtblAcft.getModel()).ordem(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtblAcft = new javax.swing.JTable();
        jbOK = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmnuExcluir = new javax.swing.JMenuItem();
        jmnuIncluir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleção de Aronave");
        setResizable(false);

        jtblAcft.setModel(new AcftTM());
        jtblAcft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblAcftMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblAcft);

        jbOK.setText("OK");
        jbOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKActionPerformed(evt);
            }
        });

        jbCancelar.setText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jMenu1.setText("Editar");

        jmnuExcluir.setText("Excluir Aeronave Selecionada");
        jmnuExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuExcluirActionPerformed(evt);
            }
        });
        jMenu1.add(jmnuExcluir);

        jmnuIncluir.setText("Incluir Nova Aeronave");
        jmnuIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuIncluirActionPerformed(evt);
            }
        });
        jMenu1.add(jmnuIncluir);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbOK, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbOK)
                    .addComponent(jbCancelar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtblAcftMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblAcftMouseClicked
        if (evt.getClickCount()==2){
            selecionado();
        }
    }//GEN-LAST:event_jtblAcftMouseClicked

    private void selecionado(){
        quem.setAcft((String)jtblAcft.getModel().getValueAt(jtblAcft.getSelectedRow(), 0));
        dispose();
    }
    private void jbOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOKActionPerformed
        selecionado();
}//GEN-LAST:event_jbOKActionPerformed

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        dispose();
}//GEN-LAST:event_jbCancelarActionPerformed

    private void jmnuIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuIncluirActionPerformed
        IncAcftView i=new IncAcftView(this);
        i.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_jmnuIncluirActionPerformed

    private void jmnuExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuExcluirActionPerformed
        if (JOptionPane.showConfirmDialog(null,"Confirma a exclusão da aeronave "+
                (String)jtblAcft.getModel().getValueAt(jtblAcft.getSelectedRow(), 0),
                "Exclusão de Aeronave",
                JOptionPane.YES_NO_OPTION )==JOptionPane.NO_OPTION){
            return;
        }else{
            Props.removeAcft((String)jtblAcft.getModel().getValueAt(jtblAcft.getSelectedRow(), 0));
            //System.out.println(">>>>excluiu");
            refresh();
        }
    }//GEN-LAST:event_jmnuExcluirActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SelAcftView(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbOK;
    private javax.swing.JMenuItem jmnuExcluir;
    private javax.swing.JMenuItem jmnuIncluir;
    private javax.swing.JTable jtblAcft;
    // End of variables declaration//GEN-END:variables

}
