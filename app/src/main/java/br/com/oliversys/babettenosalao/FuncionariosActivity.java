package br.com.oliversys.babettenosalao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.viewadapter.FuncionarioListAdapter;
import br.com.oliversys.babettenosalao.valueobject.FuncionarioRow;
import br.com.oliversys.mobilecommons.AppController;
import br.com.oliversys.mobilecommons.ISwappableView;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class FuncionariosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private AbsListView mListView;
    private ISwappableView mAdapter;
    private OnFragmentInteractionListener mListener;
    private List<FuncionarioRow> funcionarios = new ArrayList<>();
    private String estabelecimentoSelecionado;

    private Dialog progress;
    private RestHttpClient restClient;
    private static final String URL_FUNCIONARIOS_POR_ESTAB = Constants.DNS_CASA + "/babetteunhas-backend/rest/profissionais/estab/";
    private static final String URL_GET_TODOS_FUNCIONARIOS = Constants.DNS_CASA + "/babetteunhas-backend/rest/profissionais/todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionarios);

        mAdapter = new FuncionarioListAdapter(this,funcionarios);
        this.estabelecimentoSelecionado = getIntent().getStringExtra("ESTAB_ID");
        restClient = AppController.getInstance().getRestClient();

        mListView = (AbsListView) findViewById(android.R.id.list);
        mListView.setAdapter((ArrayAdapter) mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_funcionarios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FuncionarioRow f = (FuncionarioRow) parent.getItemAtPosition(position);
        Intent i = new Intent(this,AgendamentoActivity.class);
        i.putExtra("FUNCIONARIO_ID",f.getId());
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null)
            progress.dismiss();
    }

//    private void doFakeRequest(){
//        FuncionarioRow r1 = new FuncionarioRow("","LAURINDA","5");
//        FuncionarioRow r2 = new FuncionarioRow("","CATITA","4.5");
//        FuncionarioRow r3 = new FuncionarioRow("","BAM-BAM","2.5");
//
//        List<FuncionarioRow> lista = new ArrayList<>();
//        lista.add(r1);
//        lista.add(r2);
//        lista.add(r3);
//
//        mAdapter.swapRecords(lista);
//        if(progress != null) {
//            progress.dismiss();
//        }
//    }

    private void buscarFuncionariosDoSalao(){
        progress = Utils.showProgressDialog(this);
        Map mapa = new HashMap();
        mapa.put("ESTAB_SELECIONADO", this.estabelecimentoSelecionado);
        restClient.getJsonArray(URL_FUNCIONARIOS_POR_ESTAB, mapa, (ISwappableView) mAdapter,
                new FuncionarioRow(), this, progress);
    }

    private void buscarTodosFuncionarios(){
        progress = Utils.showProgressDialog(this);
        restClient.getJsonArray(URL_GET_TODOS_FUNCIONARIOS, null, (ISwappableView) mAdapter,
                new FuncionarioRow(), this, progress);
    }

    @Override
    public void onStart() {
        super.onStart();
//        buscarTodosFuncionarios();
        buscarFuncionariosDoSalao();
    }

}
