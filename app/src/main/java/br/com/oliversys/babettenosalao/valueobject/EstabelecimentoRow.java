package br.com.oliversys.babettenosalao.valueobject;

import br.com.oliversys.mobilecommons.IValueObject;

public class EstabelecimentoRow implements IValueObject
{
    private String id; // recebe do backend. Nao tem na tela

    private String url;
    private String nome;
    private String distancia; // calculado
    private String precoMao;
    private String bairro;
    private String proximoHorarioDisponivel;
    private String avaliacao;

    public EstabelecimentoRow(){}

    public EstabelecimentoRow(String url, String nome, String distancia, String precoMao,
                              String bairro, String proximoHorarioDisponivel, String avaliacao) {
        this.url = url;
        this.nome = nome;
        this.distancia = distancia;
        this.precoMao = precoMao;
        this.bairro = bairro;
        this.proximoHorarioDisponivel = proximoHorarioDisponivel;
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getPrecoMao() {
        return precoMao;
    }

    public void setPrecoMao(String precoMao) {
        this.precoMao = precoMao;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getProximoHorarioDisponivel() {
        return proximoHorarioDisponivel;
    }

    public void setProximoHorarioDisponivel(String proximoHorarioDisponivel) {
        this.proximoHorarioDisponivel = proximoHorarioDisponivel;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "EstabelecimentoRow{" +
                ", url='" + url + '\'' +
                ", nome='" + nome + '\'' +
                ", distancia='" + distancia + '\'' +
                ", precoMao='" + precoMao + '\'' +
                ", bairro='" + bairro + '\'' +
                ", proximoHorarioDisponivel='" + proximoHorarioDisponivel + '\'' +
                ", avaliacao='" + avaliacao + '\'' +
                '}';
    }
}
