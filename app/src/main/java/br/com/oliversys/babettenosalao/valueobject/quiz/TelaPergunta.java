package br.com.oliversys.babettenosalao.valueobject.quiz;

public class TelaPergunta{
    private int ultimaTelaFragmentId; // nao pode ser Fragment pq n implementa Parceable ou Serializable
    private int rootContainerId; // nao pode ser View pq n implementa Parceable ou Serializable

    public TelaPergunta(int f){
        this.ultimaTelaFragmentId = f;
    }

    public int getUltimaTelaFragmentId() {
        return ultimaTelaFragmentId;
    }

}