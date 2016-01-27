package br.com.oliversys.babettenosalao.valueobject;

import br.com.oliversys.mobilecommons.IValueObject;

public class FuncionarioRow implements IValueObject {

    private String id; // recebe do backend. Nao tem na tela

    private String fotoUrl;
    private String nome;
    private String avaliacao;

    public FuncionarioRow(){}

    public FuncionarioRow(String fotoUrl, String nome, String avaliacao) {
        this.fotoUrl = fotoUrl;
        this.nome = nome;
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "FuncionarioRow{" +
                "fotoUrl='" + fotoUrl + '\'' +
                ", nome='" + nome + '\'' +
                ", avaliacao='" + avaliacao + '\'' +
                '}';
    }
}
