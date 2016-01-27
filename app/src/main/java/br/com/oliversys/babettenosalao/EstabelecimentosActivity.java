package br.com.oliversys.babettenosalao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.EstabelecimentoRow;
import br.com.oliversys.babettenosalao.viewadapter.EstabelecimentoListAdapter;
import br.com.oliversys.mobilecommons.AppController;
import br.com.oliversys.mobilecommons.ISwappableView;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class EstabelecimentosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private AbsListView mListView;
    private ISwappableView mAdapter;
    private List<EstabelecimentoRow> estabelecimentos = new ArrayList<EstabelecimentoRow>();
    private String cepDoUsuario; // Location(coordenadas) em formato de string
    private Dialog progress;
    private RestHttpClient restClient;

    private static final String URL_GET_TODOS_ESTAB = Constants.DNS_PRODUCAO + "/babetteunhas-backend/rest/estabelecimentos/todos";
    private static final String URL_GET_ESTAB_POR_CEP = Constants.DNS_CASA + "/babetteunhas-backend/rest/estabelecimentos/cep/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ((ViewStub)findViewById(R.id.stub_estab)).inflate();

//        buildLayoutDinamically();

        mAdapter = new EstabelecimentoListAdapter(this,estabelecimentos);
        restClient = AppController.getInstance().getRestClient();

        mListView = (AbsListView) findViewById(android.R.id.list);
        mListView.setAdapter((ListAdapter) mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estabelecimentos, menu);
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
    protected void onStart() {
        super.onStart();
        String cep = Utils.getFromSharedPrefs(this.getApplicationContext(), "CEP");
        if (cep != null) {
            this.cepDoUsuario = cep;
            buscarEstabelecimentosMaisProximos();
        }else
            buscarTodosEstabelecimentos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progress != null)
            progress.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,FuncionariosActivity.class);
        EstabelecimentoRow estabSelecionado = (EstabelecimentoRow)parent.getItemAtPosition(position);
        i.putExtra("ESTAB_ID",estabSelecionado.getId());
        startActivity(i);
    }

    private void buscarTodosEstabelecimentos(){
        progress = Utils.showProgressDialog(this);
        restClient.getJsonArray(URL_GET_TODOS_ESTAB,null,mAdapter,new EstabelecimentoRow(),this,progress);
    }

//    private void doFakeRequest(){
//        EstabelecimentoRow r1 = new EstabelecimentoRow("","FIQUE LINDA","5","25","JD. ANGELA","15:00","3.5");
//        EstabelecimentoRow r2 = new EstabelecimentoRow("","SUPER BONITA","7","28","GUAIANAZES","16:00","1.5");
//        EstabelecimentoRow r3 = new EstabelecimentoRow("","RECAUCHUTADA","10","32","SOCORRO","15:00","4.5");
//
//        List<EstabelecimentoRow> lista = new ArrayList<>();
//        lista.add(r1);
//        lista.add(r2);
//        lista.add(r3);
//
//        mAdapter.swapRecords(lista);
//        if(progress != null) {
//            progress.dismiss();
//        }
//    }

    private void buscarEstabelecimentosMaisProximos(){
        Map<String,Object> mapa = new HashMap<>();
        mapa.put("cep",this.cepDoUsuario);
        // buscar saloes proximos ao bairro do usuario
        restClient.getJsonArray(URL_GET_ESTAB_POR_CEP, mapa, (ISwappableView)mAdapter,
                new EstabelecimentoRow(), this, progress);
    }

    protected View buildLayoutDinamically(){
        View layout = null;

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams
                (DrawerLayout.LayoutParams.MATCH_PARENT,DrawerLayout.LayoutParams.MATCH_PARENT);

        DrawerLayout drawerLayout = new DrawerLayout(this);
        drawerLayout.setLayoutParams(params);


//        android:layout_alignParentTop="true"
//
//        <LinearLayout
//        android:layout_width="match_parent"
//        android:layout_height="match_parent">
//        <include
//        android:id="@+id/toolbar"
//        layout="@layout/toolbar" />
//        </LinearLayout>
//
//        <FrameLayout
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:id="@+id/main_content"/>
//
//        <fragment
//        android:id="@+id/fragment_nav_drawer"
//        android:name="br.com.oliversys.babettenosalao.fragments.NavigationDrawerFragment"
//        tools:layout="@layout/fragment_nav_drawer"
//        android:layout_width="match_parent" android:layout_height="match_parent" />
//
//        </android.support.v4.widget.DrawerLayout>

        return drawerLayout;
    }
}
