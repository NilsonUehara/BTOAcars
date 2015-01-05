package br.com.aeroboteco.model;

public class PernaTour {
    private int tour;
    private int sequencia;
    private String origem;
    private String destino;

    public int getTour() {
        return tour;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
    @Override
    public String toString(){
        String texto=getSequencia()+" - "+getOrigem()+"/"+getDestino();
        String enviado=Props.getPirepEnviado(getTour()+":"+getOrigem()+":"+getDestino())[1].toLowerCase();
        if (enviado!=null && !enviado.equals("")){
            if (enviado.equals("aprovado")){
                texto+=" (Aprovado)";
            }else{
                texto+=" (Enviado)";
            }
        }
        return texto;
    }

}
