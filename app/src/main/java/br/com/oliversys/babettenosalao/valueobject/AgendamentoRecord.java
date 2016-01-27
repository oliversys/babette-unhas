package br.com.oliversys.babettenosalao.valueobject;

import br.com.oliversys.mobilecommons.IValueObject;

/**
 * Created by William on 6/18/2015.
 */
public class AgendamentoRecord implements IValueObject
{
    private String id; // recebe do backend. Nao tem na tela

    private String data;
    private String horario;
    private String servico;
    private String user;

    public AgendamentoRecord(){}

    public AgendamentoRecord(String d,String h,String svc,String user){
        this.data = d;
        this.horario = h;
        this.servico = svc;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "AgendamentoRecord{" +
                "data='" + data + '\'' +
                ", horario='" + horario + '\'' +
                ", servico='" + servico + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
