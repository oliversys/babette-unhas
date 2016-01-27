package br.com.oliversys.babettenosalao.fragments.viewadapter;

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
import br.com.oliversys.babettenosalao.valueobject.FuncionarioRow;
import br.com.oliversys.mobilecommons.AppController;
import br.com.oliversys.mobilecommons.ISwappableView;
import br.com.oliversys.mobilecommons.RestHttpClient;

public class FuncionarioListAdapter extends ArrayAdapter implements ISwappableView<FuncionarioRow> {

    private Context ctx;
    private List<FuncionarioRow> listItems;
    private RestHttpClient restClient;

    public FuncionarioListAdapter(Context context, List<FuncionarioRow> items) {
        super(context, R.layout.funcionario_row, items);
        this.listItems = items;
        restClient = AppController.getInstance().getRestClient();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder v = null;
        if (row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(R.layout.funcionario_row, parent, false);
            v = new ViewHolder(row);
            row.setTag(v);
        }else{
            v = (ViewHolder) row.getTag();
        }

        if(listItems.isEmpty()) {
            Toast.makeText((Activity) this.ctx, "Nenhum funcionario encontrado", Toast.LENGTH_LONG).show();
        }
        else
        {
            FuncionarioRow tempValues = (FuncionarioRow)listItems.get(position);

            restClient.downloadImage(tempValues.getFotoUrl(),ctx,v.foto);
            v.nome.setText(tempValues.getNome().toUpperCase());
            v.avaliacao.setRating(Float.valueOf(tempValues.getAvaliacao()));
        }
        return row;
    }

    @Override
    public void swapRecords(List<FuncionarioRow> list) {
        this.listItems.clear();
        for (FuncionarioRow e:list){
            this.listItems.add(e);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView foto;
        private TextView nome;
        private RatingBar avaliacao;

        ViewHolder(View listViewItem) {
            foto = (ImageView) listViewItem.findViewById(R.id.foto);
            nome = (TextView) listViewItem.findViewById(R.id.nomeFunc);
            avaliacao = (RatingBar) listViewItem.findViewById(R.id.barra_avaliacao);
        }
    }
}
