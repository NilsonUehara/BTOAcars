package br.com.aeroboteco.view;

import javax.swing.table.AbstractTableModel;
import br.com.aeroboteco.model.Acft;
import br.com.aeroboteco.model.Props;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcftTM extends AbstractTableModel{
    private String[] colunas = {"ICAO","Fabricante","Modelo"};
    private Object[][] dados;
    private List<Acft> acfts;

    public AcftTM(){
        refresh();
    }
    public void refresh(){
        this.acfts=Props.getAcft();
        carrega();
    }
    private void carrega(){
        dados=new Object[acfts.size()][colunas.length];
        int i=0;
        for (Acft a:acfts){
            int c=0;
            dados[i][c++]=a.getIcao();
            dados[i][c++]=a.getFabricante();
            dados[i][c++]=a.getModelo();
            i++;
        }
    }
    public int getRowCount() {
        return dados.length;
    }
    public int getColumnCount() {
        return colunas.length;
    }
    @Override
    public String getColumnName(int col) {
        return colunas[col];
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dados[rowIndex][columnIndex];
    }
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    public void ordem(final int coluna){
        Collections.sort(acfts, new Comparator() {
            public int compare(Object arg0, Object arg1) {
                Acft a1=(Acft)arg0;
                Acft a2=(Acft)arg1;
                int ret=0;
                if (coluna==0){ret=a1.getIcao().compareTo(a2.getIcao());}
                if (coluna==1){ret=a1.getFabricante().compareTo(a2.getFabricante());}
                if (coluna==2){ret=a1.getModelo().compareTo(a2.getModelo());}
                return ret;
            }
        });
        carrega();
    }
}
