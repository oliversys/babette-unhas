package br.com.oliversys.babettenosalao;

import android.support.v4.app.Fragment;

public class QuizFragment extends Fragment {

//    private Fragment ultimoFragmentInstanciado;
//    private PerguntaVO ultimaPerguntaExibida;
//    private static FrameLayout framePrincipal;
//    private String TAG;
//    private OnFragmentInteractionListener listener;
//
//    public static QuizFragment newInstance(){
//        QuizFragment frag = new QuizFragment();
//        return frag;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        restaurarUltimaTelaQuiz(savedInstanceState);
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            listener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        restaurarUltimaTelaQuiz(outState);
//    }
//
//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        restaurarUltimaTelaQuiz(savedInstanceState);
//    }
//
//    /**
//     * Restaura a ultima prgunta respondida do BUNDLE (salvo qdo a atividade eh sustensa)
//     * @param savedInstanceState
//     */
//    private void restaurarUltimaTelaQuiz(Bundle savedInstanceState) {
//        if (savedInstanceState != null){
//            this.ultimaPerguntaExibida = savedInstanceState.getParcelable("ULTIMA_PERGUNTA");
//            if (this.ultimaPerguntaExibida == null) {
//                this.buildPergunta1();
//                savedInstanceState.putParcelable("ULTIMA_PERGUNTA", this.ultimaPerguntaExibida);
//            }else{
//                this.instanceFragmentFromPergunta(this.ultimaPerguntaExibida);
//            }
//        }else{
//            this.buildPergunta1();
//        }
//        savedInstanceState.putParcelable("ULTIMA_PERGUNTA", this.ultimaPerguntaExibida);
//        // carrega o fragment no framePrincipal
//        listener.onQuizCreated(this.ultimoFragmentInstanciado);
//    }
//
//    public void buildPergunta1() {
//        TAG = "PERGUNTA 1";
//        List<String> respostas = new ArrayList<>();
//        respostas.add(getString(R.string.casa));
//        respostas.add(getString(R.string.trabalho));
//        respostas.add(getString(R.string.salao));
//
//        PerguntaVO p = new PerguntaVO(getString(R.string.pergunta1), (ArrayList) respostas, TAG);
//        instanceFragmentFromPergunta(p);
//    }
//
//    public void buildPergunta2() {
//        TAG = "PERGUNTA 2";
//        List<String> respostas = new ArrayList<>();
//        respostas.add(getString(R.string.sofisticada));
//        respostas.add(getString(R.string.criativa));
//        respostas.add(getString(R.string.divertida));
//        respostas.add(getString(R.string.baladeira));
//        respostas.add(getString(R.string.romantica));
//        respostas.add(getString(R.string.classica));
//        respostas.add(getString(R.string.aceito));
//
//        PerguntaVO p = new PerguntaVO(getString(R.string.pergunta2), (ArrayList) respostas, TAG);
//        instanceFragmentFromPergunta(p);
//    }
//
//    private void buildPergunta3() {
//        TAG = "PERGUNTA 3";
//        List<String> respostas = new ArrayList<>();
//        respostas.add(getString(R.string.pe));
//        respostas.add(getString(R.string.mao));
//        respostas.add(getString(R.string.pe_e_mao));
//
//        PerguntaVO p = new PerguntaVO(getString(R.string.pergunta3), (ArrayList) respostas, TAG);
//        instanceFragmentFromPergunta(p);
//    }
//
//    private void buildPergunta4() {
//        TAG = "PERGUNTA 4";
//        List<String> respostas = new ArrayList<>();
//        respostas.add(getString(R.string.mesma));
//        respostas.add(getString(R.string.diferentes));
//
//        PerguntaVO p = new PerguntaVO(getString(R.string.pergunta4), (ArrayList) respostas, TAG);
//        instanceFragmentFromPergunta(p);
//    }
//
//    private void instanceFragmentFromPergunta(PerguntaVO p) {
//        Bundle b = new Bundle();
//        b.putParcelable("PERGUNTA", p);
//
//        PerguntaFragment f = PerguntaFragment.newInstance(b);
//        this.ultimoFragmentInstanciado = f;
//        this.ultimaPerguntaExibida = f.getPergunta();
//    }
//
//    private void exibirMensagem(String msg) {
//        new AlertDialog.Builder(getActivity())
//                .setPositiveButton("OK", null)
//                .setMessage(msg)
//                .show();
//    }
//
//    // chamado qdo o usuario invoca outro app
//    @Override
//    public void onPause() {
//        super.onPause();
//        getArguments().putParcelable("ULTIMA_PERGUNTA", this.ultimaPerguntaExibida);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getArguments() != null)
//            this.ultimaPerguntaExibida = getArguments().getParcelable("ULTIMA_PERGUNTA");
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onQuizCreated(Fragment f);
//        void onQuizFinished();
//    }
}
