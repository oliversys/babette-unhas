package br.com.oliversys.babettenosalao.viewadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.EstabelecimentoRow;
import br.com.oliversys.mobilecommons.AppController;
import br.com.oliversys.mobilecommons.ISwappableView;
import br.com.oliversys.mobilecommons.RestHttpClient;

public class EstabelecimentoListAdapter extends ArrayAdapter implements ISwappableView<EstabelecimentoRow> {

    private Context ctx;
    private List<EstabelecimentoRow> listItems;
    private RestHttpClient restClient;

    public EstabelecimentoListAdapter(Context context, List<EstabelecimentoRow> items) {
        super(context, R.layout.estabelecimento_row, items);
        this.listItems = items;
        restClient = AppController.getInstance().getRestClient();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder v = null;
        if (row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(R.layout.estabelecimento_row, parent, false);
            v = new ViewHolder(row);
            row.setTag(v);
        }else{
            v = (ViewHolder) row.getTag();
        }

        if(listItems.isEmpty()) {
            Toast.makeText((Activity) this.ctx, "Nenhum estabelecimento encontrado", Toast.LENGTH_LONG).show();
        }
        else
        {
            EstabelecimentoRow tempValues = (EstabelecimentoRow)listItems.get(position);

            restClient.downloadImage(tempValues.getUrl(),ctx,v.logoEstab);
            v.nome.setText(tempValues.getNome().toUpperCase());
            v.distancia.setText(tempValues.getDistancia()+" KM");
            v.precoMao.setText("R$ " + tempValues.getPrecoMao());
            v.bairro.setText(tempValues.getBairro());
            v.proximoHorario.setText(tempValues.getProximoHorarioDisponivel()); // data/hora
            v.barraAvaliacao.setRating(Float.valueOf(tempValues.getAvaliacao()));
        }
        return row;
    }

    @Override
    public void swapRecords(List<EstabelecimentoRow> objects) {
        this.listItems.clear();
        for (EstabelecimentoRow e:objects){
            this.listItems.add(e);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView logoEstab;
        private TextView nome;
        private TextView distancia;
        private TextView precoMao;
        private TextView bairro;
        private TextView proximoHorario;
        private RatingBar barraAvaliacao;

        ViewHolder(View listViewItem) {
            logoEstab = (ImageView) listViewItem.findViewById(R.id.logoEstab);
            nome = (TextView) listViewItem.findViewById(R.id.nomeEstab);
            distancia = (TextView) listViewItem.findViewById(R.id.distancia);
            precoMao = (TextView) listViewItem.findViewById(R.id.preco_mao);
            bairro = (TextView) listViewItem.findViewById(R.id.bairro);
            proximoHorario = (TextView) listViewItem.findViewById(R.id.proximo_horario);
            barraAvaliacao = (RatingBar) listViewItem.findViewById(R.id.barra_avaliacao);
        }
    }
}
