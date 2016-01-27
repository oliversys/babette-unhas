package br.com.oliversys.babettenosalao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.PerfilUsuarioVO;
import br.com.oliversys.mobilecommons.AppController;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class CadastrarPerfilUsuarioActivity extends Activity {

    private RestHttpClient restClient;

    private OnFragmentInteractionListener mListener;
    private PerfilUsuarioVO perfil;

    private ImageView foto;
    private EditText nome;
    private EditText dtNascimento;
    private EditText rua;
    private EditText num;
    private EditText complemento;
    private Spinner cidade;
    private Spinner estado;
    private EditText bairro;
    private EditText cep;
    private EditText email;
    private Button cadastrar;

    private static final String URL_INCLUIR_USUARIO_JSON = Constants.DNS_CASA + "/babetteunhas-backend/rest/usuario/incluir";

    public CadastrarPerfilUsuarioActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        restClient = AppController.getInstance().getRestClient();

        if(getIntent().getBundleExtra("PERFIL") != null){
            this.perfil = savedInstanceState.getParcelable("PERFIL");
            exibirSomenteLeitura();
        }
        else {
            foto = (ImageView) findViewById(R.id.foto);
            nome = (EditText) findViewById(R.id.name);
            dtNascimento = (EditText) findViewById(R.id.data_nascimento);
            rua = (EditText) findViewById(R.id.rua);
            num = (EditText) findViewById(R.id.num);
            complemento = (EditText) findViewById(R.id.compl);
            cidade = (Spinner) findViewById(R.id.cidade);
            estado = (Spinner) findViewById(R.id.estado);
            bairro = (EditText) findViewById(R.id.bairro);
            cep = (EditText) findViewById(R.id.cep);
            email = (EditText) findViewById(R.id.email);
            cadastrar = (Button) findViewById(R.id.cadastrar);
        }

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValoresPreenchidosPeloUsuario();
                cadastrarUsuario();
            }
        });

        // evento de enter no teclado apos o ultimo campo do form
        email.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getValoresPreenchidosPeloUsuario();
                    cadastrarUsuario();
                    return true;
                }
                return false;
            }
        });
    }

    /*
        Exibe as informacoes do perfil ja recuperadas pelo sistema automaticamente (pelo Facebook)
        como somente leitura. As info faltantes devem ser solicitadas em campos habilitados para edicao.
     */
    private void exibirSomenteLeitura(){
        if (this.perfil.getFoto() != null){
            foto.setImageBitmap(this.perfil.getFoto());
            foto.setEnabled(false);
        }

        if (this.perfil.getName() != null) {
            nome.setText(this.perfil.getName());
            nome.setEnabled(false);
        }

        if (this.perfil.getBirthday() != null) {
            dtNascimento.setText(this.perfil.getBirthday());
            dtNascimento.setEnabled(false);
        }

        if (this.perfil.getNomeRua() != null) {
            rua.setText(this.perfil.getNomeRua());
            rua.setEnabled(false);
        }

        if (this.perfil.getNumRua() != null) {
            num.setText(this.perfil.getNumRua());
            num.setEnabled(false);
        }

        if (this.perfil.getComplemento() != null) {
            complemento.setText(this.perfil.getComplemento());
            complemento.setEnabled(false);
        }

        if (this.perfil.getCidade() != null) {
            for (int i = 0; i >= cidade.getAdapter().getCount(); i++) {
                if (cidade.getAdapter().getItem(i).toString().contains(this.perfil.getCidade())) {
                    cidade.setSelection(i);
                    break;
                }
            }
            cidade.setEnabled(false);
        }

        if (this.perfil.getEstado() != null) {
            for (int j = 0; j >= estado.getAdapter().getCount(); j++) {
                if (estado.getAdapter().getItem(j).toString().contains(this.perfil.getEstado())) {
                    estado.setSelection(j);
                    break;
                }
            }
            estado.setEnabled(false);
        }

        if (this.perfil.getBairro() != null) {
            bairro.setText(this.perfil.getBairro());
            bairro.setEnabled(false);
        }

        if (this.perfil.getCep() != null) {
            cep.setText(this.perfil.getCep());
            cep.setEnabled(false);
        }

        if (this.perfil.getEmail() != null) {
            email.setText(this.perfil.getEmail());
            email.setEnabled(false);
        }
    }

    private void getValoresPreenchidosPeloUsuario()
    {
        this.perfil = new PerfilUsuarioVO();

        try {
            this.perfil.setFoto(((BitmapDrawable) foto.getDrawable()).getBitmap());
        }catch(ClassCastException e){
            Log.e(getClass().getSimpleName(), "erro no casting da imagem do perfil para Bitmap");
        }

        this.perfil.setName(nome.getText().toString());
        this.perfil.setBirthday(dtNascimento.getText().toString());
        this.perfil.setNomeRua(rua.getText().toString());
        this.perfil.setNumRua(num.getText().toString());
        this.perfil.setComplemento(complemento.getText().toString());
        this.perfil.setCidade(cidade.getSelectedItem().toString());
        this.perfil.setEstado(estado.getSelectedItem().toString());
        this.perfil.setBairro(bairro.getText().toString());
        this.perfil.setCep(cep.getText().toString());
        this.perfil.setEmail(email.getText().toString());

        Bundle b = new Bundle();
        b.putParcelable("PERFIL",this.perfil);
        getIntent().putExtras(b);
    }

    private void cadastrarUsuario(){
        JSONObject usuarioJsonObj = Utils.toJsonFile(this.perfil);
        restClient.post(URL_INCLUIR_USUARIO_JSON,usuarioJsonObj,this,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirma cadastramento ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chamarPromocoesActivity();
            }
        });
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        chamarPromocoesActivity();
    }

    private void chamarPromocoesActivity(){
        Intent i = new Intent(this, PromocoesActivity.class);
        startActivity(i);
    }
}
