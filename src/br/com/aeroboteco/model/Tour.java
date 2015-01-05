package br.com.aeroboteco.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private int codigo;
    private String nome;
    private List<PernaTour> pernas = new ArrayList<PernaTour>();

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<PernaTour> getPernas() {
        return pernas;
    }

    public void setPernas(List<PernaTour> pernas) {
        this.pernas = pernas;
    }

    @Override
    public String toString(){
        return getNome()+"("+getCodigo()+")";
    }
}
