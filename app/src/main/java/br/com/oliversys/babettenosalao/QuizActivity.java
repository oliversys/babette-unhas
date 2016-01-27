package br.com.oliversys.babettenosalao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.quiz.PerguntaVO;
import br.com.oliversys.mobilecommons.IValueObject;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class QuizActivity extends AppCompatActivity {

    private RestHttpClient restClient;
    private List<IValueObject> listaPerguntas = new ArrayList<>();
    private int numPerguntaAtual = 1;

    private static final String URL_GET_TODAS_PERGUNTAS_QUIZ =
            Constants.DNS_CASA + "/babetteunhas-backend/rest/perguntas/todas/";

    private FrameLayout layoutPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_quiz);
        setContentView(R.layout.activity_main);

//        buildLayoutDinamically();

        this.restClient = RestHttpClient.getInstance();
        // traz todas as perguntas e popula a lista de PerguntaVO
        this.restClient.getJsonArrayWithNoListView(URL_GET_TODAS_PERGUNTAS_QUIZ, null,
                this.listaPerguntas, this, null, PerguntaVO.class);

        preencherPerguntasFake();

        if (this.listaPerguntas.get(0) != null) {
            PerguntaVO p = (PerguntaVO)this.listaPerguntas.get(0);
            this.layoutPrincipal = (FrameLayout)findViewById(R.id.main_content);
            View root = getLayoutInflater().inflate(R.layout.activity_quiz, layoutPrincipal, true);
            p.gerarViewFromVO(0, root, this);
//            setContentView(viewGerada);
//        randomizarListaPerguntas();
        }
    }

    private void preencherPerguntasFake(){
        List<String> resps = new ArrayList<>();

        resps.add(getString(R.string.casa));
        resps.add(getString(R.string.salao));
        resps.add(getString(R.string.trabalho));
        PerguntaVO obj1 = new PerguntaVO(getString(R.string.pergunta1),resps);

        resps = new ArrayList<>();
        resps.add(getString(R.string.sofisticada));
        resps.add(getString(R.string.criativa));
        resps.add(getString(R.string.classica));
        resps.add(getString(R.string.divertida));
        resps.add(getString(R.string.baladeira));
        resps.add(getString(R.string.romantica));
        resps.add(getString(R.string.aceito));
        PerguntaVO obj2 = new PerguntaVO(getString(R.string.pergunta2),resps);

        resps = new ArrayList<>();
        resps.add(getString(R.string.pe));
        resps.add(getString(R.string.mao));
        resps.add(getString(R.string.pe_e_mao));
        PerguntaVO obj3 = new PerguntaVO(getString(R.string.pergunta3),resps);

        resps = new ArrayList<>();
        resps.add(getString(R.string.mesma));
        resps.add(getString(R.string.diferentes));
        PerguntaVO obj4 = new PerguntaVO(getString(R.string.pergunta4),resps);

        this.listaPerguntas.add(obj1);
        this.listaPerguntas.add(obj2);
        this.listaPerguntas.add(obj3);
        this.listaPerguntas.add(obj4);
    }

    private void mostrarPerguntaNum(int numPergunta){
        PerguntaVO p = (PerguntaVO)this.listaPerguntas.get(numPergunta);
        if (numPergunta == 3){
            chamarEstabelecimentoActivity(); // qdo a lista chegar ao fim, chama a tela de Estabelecimentos
        }
//        setContentView(R.layout.activity_quiz);
//        View root = findViewById(R.id.relativeLayout);
        View viewGerada = p.gerarViewFromVO(numPergunta,this.layoutPrincipal,this);
        setContentView(viewGerada);
    }

    /**
     * Renderiza a tela da proxima pergunta com base no valor do radioButton selecionado pelo usuario (resposta do usuario)
     *
     * @param textoResposta resposta do usuario
     */
    public void onQuestionAnswered(String textoResposta,String enunciado,int numPerguntaAtual) {
        Utils.putStringOnSharedPrefs(this.getApplicationContext(), enunciado, textoResposta);
        mostrarPerguntaNum(++numPerguntaAtual);
    }

    private void randomizarListaPerguntas(){
        Collections.shuffle(this.listaPerguntas,new Random(156729987L));
    }

    private void chamarEstabelecimentoActivity(){
        FrameLayout layoutPrincipal = (FrameLayout)findViewById(R.id.main_content);
        getLayoutInflater().inflate(R.layout.activity_estabelecimentos, layoutPrincipal, false);

        Intent i = new Intent(this, EstabelecimentosActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
