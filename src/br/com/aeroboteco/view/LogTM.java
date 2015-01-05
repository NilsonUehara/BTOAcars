package br.com.aeroboteco.view;

import br.com.aeroboteco.model.Evento;
import br.com.aeroboteco.model.PV;
import javax.swing.table.AbstractTableModel;

public class LogTM extends AbstractTableModel{
    private String[] colunas = {"Hora","Descrição"};
    private Object[][] dados;
    private PV pv;
    //private List<Evento>log=new ArrayList<Evento>();

    public LogTM(PV pv){
        this.pv=pv;
        carrega();
    }
    private void carrega(){
        dados=new Object[pv.getLog().size()][colunas.length];
        int i=0;
        for (Evento l:pv.getLog()){
            int c=0;
            dados[i][c++]=l.getHora();
            dados[i][c++]=l.getEvento();
            i++;
        }
    }
    public int getRowCount() {
        return dados.length;
    }
    public int getColumnCount() {
        return colunas.length;
    }
    public String getColumnName(int col) {
        return colunas[col];
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dados[rowIndex][columnIndex];
    }
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    public boolean isCellEditable(int row, int col) {
        return true;
    }
    public void addEvento(Evento evento){
        pv.getLog().add(evento);
        carrega();
        fireTableDataChanged();
    }
    public void addEvento(String evento){
        pv.getLog().add(new Evento(evento));
        carrega();
        fireTableDataChanged();
    }
    /*public void setValueAt(Object value, int row, int col) {
		Object[] coluna = (Object[]) dados[row];
        coluna[col] = (Object) value;
        fireTableCellUpdated(row, col);
    }
    public Lancamento getLancamentoAt(int row){
        return movto.get(row);
    }*/
}
