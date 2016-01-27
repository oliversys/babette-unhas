package br.com.oliversys.babettenosalao;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.AgendamentoRecord;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class AgendamentoActivity extends AppCompatActivity {

    private CalendarView calendario;
    private static final String URL_GET_HORARIOS_POR_DATA = Constants.DNS_CASA + "/babetteunhas-backend/rest/agendamentos/horarios/";
    private static final String URL_GET_DATAS_POR_FUNC = Constants.DNS_CASA + "/babetteunhas-backend/rest/agendamentos/datas/funcionario/";
    private static final String URL_INCLUIR_AGENDAMENTO_JSON = Constants.DNS_CASA + "/babetteunhas-backend/rest/agendamentos/incluir";

    private String funcionarioId;
    private String servicoAgendado;
    private List<Date> datasDisponiveis = new ArrayList<Date>();
    private String usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        if(getIntent().getStringExtra("FUNCIONARIO_ID") != null){
            this.funcionarioId = getIntent().getStringExtra("FUNCIONARIO_ID");
        }

        this.servicoAgendado = Utils.getFromSharedPrefs(getApplicationContext(), "PERGUNTA 3");
        this.usuarioLogado = Utils.getFromSharedPrefs(getApplicationContext(), "USUARIO"); // nao exibido na tela

        this.calendario = (CalendarView)findViewById(R.id.calendario);
        this.calendario.setFirstDayOfWeek(2);

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
//        AppController.getInstance().doJsonRequestStringList(adapter, this, URL_GET_DATAS_POR_FUNC + this.funcionarioId);

        doFakeRequestDatesByProfissional(adapter);
        addDatasDisponiveis(adapter);
        setMinMaxCalendarDate();

        this.calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                buscarHorariosPorDia(dayOfMonth, month, year);
            }
        });
    }

    private void addDatasDisponiveis(ArrayAdapter a){
        Calendar c = Calendar.getInstance();
        for(int i=0; i<= a.getCount() - 1;i++){
            int dia = Integer.valueOf(a.getItem(i).toString().substring(0, 2));
            int mes = Integer.valueOf(a.getItem(i).toString().substring(3, 5));
            int ano = Integer.valueOf(a.getItem(i).toString().substring(6, 10));
            c.set(ano,mes,dia);
            Date d = c.getTime();
            this.datasDisponiveis.add(d);
        }
    }

    private void doFakeRequestDatesByProfissional(ArrayAdapter<String> a){
        Calendar c = Calendar.getInstance();
        String fData = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
        a.insert(fData,0);

        c.add(Calendar.DAY_OF_MONTH, 4);
        fData = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
        a.insert(fData,1);

        c.add(Calendar.DAY_OF_MONTH, 6);
        fData = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
        a.insert(fData,2);

        c.add(Calendar.DAY_OF_MONTH,12);
        fData = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
        a.insert(fData, 3);
    }

    private void setMinMaxCalendarDate() {
        long fMin = datasDisponiveis.get(0).getTime(); // no minimo hoje
        long fMax = datasDisponiveis.get(datasDisponiveis.size() - 1).getTime(); // no maximo a ultima data da lista
        this.calendario.setMaxDate(fMax);
        this.calendario.setMinDate(fMin);
        // this.calendario.setMinDate(fMin - 1000); // subtrai 1 seg pq nao pode ser exatamente agora senao IllegalArgumentException
    }

    private void buscarHorariosPorDia(int dia, int mes, int ano){
        final Calendar dataSelecionada = Calendar.getInstance();
        dataSelecionada.set(ano, mes, dia);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

//        AppController.getInstance().doJsonRequestStringList(adapter, this, URL_GET_HORARIOS_POR_DATA + dataSelecionada);
        doFakeRequestHorariosByDate(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_horarios);
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dlgHorarios, int which) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    ft.remove(prev);
                ft.addToBackStack(null);

                if (AgendamentoActivity.this.servicoAgendado == null)
                    Log.d(AgendamentoActivity.this.getLocalClassName(),"SVC NULO");

                if (AgendamentoActivity.this.usuarioLogado == null)
                    Log.d(AgendamentoActivity.this.getLocalClassName(),"USUARIO NULO");

                DialogFragment dlgConfirmacao = ConfirmacaoAgendamentoFragment
                        .newInstance(adapter.getItem(which).toString(),dataSelecionada.getTime().toString(),
                                AgendamentoActivity.this.servicoAgendado,AgendamentoActivity.this.usuarioLogado);

                dlgConfirmacao.show(ft,"dialog");
                dlgHorarios.dismiss();
            }
        });
        builder.show();
    }

    private void doFakeRequestHorariosByDate(ArrayAdapter a){
        a.insert("12:00", 0);
        a.insert("12:35", 1);
        a.insert("13:10", 2);
        a.insert("13:40", 3);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agendamento, menu);
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

    public static class ConfirmacaoAgendamentoFragment extends DialogFragment
    {
        private RestHttpClient restClient;

        public static ConfirmacaoAgendamentoFragment newInstance(String hora,String data,String svc,String userName){
            Bundle b = new Bundle();
            b.putString("DATA", data);
            b.putString("HORARIO", hora);
            b.putString("SVC", svc);
            b.putString("USER", userName);
            ConfirmacaoAgendamentoFragment f = new ConfirmacaoAgendamentoFragment();
            f.setArguments(b);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_confirmacao_agendamento, container, false);

            final TextView data = (TextView) v.findViewById(R.id.data);
            final TextView horario = (TextView) v.findViewById(R.id.horario);
            final TextView servico = (TextView) v.findViewById(R.id.servico);
            Button confirmar = (Button) v.findViewById(R.id.confirmar);

            restClient = RestHttpClient.getInstance();

            data.setText(getArguments().getString("DATA"));
            horario.setText(getArguments().getString("HORARIO"));
            servico.setText(getArguments().getString("SVC"));
            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AgendamentoRecord agendamento = new AgendamentoRecord(data.getText().toString()
                            , horario.getText().toString(), servico.getText().toString(), getArguments().getString("USER"));

                    incluirAgendamento(agendamento);
                }
            });
            return v;
        }

        private void incluirAgendamento(AgendamentoRecord agendamento){
            JSONObject agendamentoJsonObj = Utils.toJsonFile(agendamento);
            restClient.post(URL_INCLUIR_AGENDAMENTO_JSON, agendamentoJsonObj, getActivity(), null);
            Log.i("AGENDAMENTO", agendamentoJsonObj.toString());

            chamarCadastrarPerfilActivity();
        }

        private void chamarCadastrarPerfilActivity(){
            Intent i = new Intent(getActivity(),CadastrarPerfilUsuarioActivity.class);
            startActivity(i);
        }
    }
}
