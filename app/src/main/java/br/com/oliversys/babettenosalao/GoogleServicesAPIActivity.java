package br.com.oliversys.babettenosalao;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.valueobject.PerfilUsuarioVO;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class GoogleServicesAPIActivity extends FragmentActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	private boolean isResolvendoErro = false;
	private boolean erroConexao = false;
	// Request code to use when launching the resolution activity
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	private static final String STATE_RESOLVING_ERROR = "ESTADO_RESOLUCAO_ERRO";
	// Unique tag for the error dialog fragment
	private static final String DIALOG_ERROR = "dialog_error";

	private Location mLastLocation;
	private static final String LOCATION_KEY = "LAST_KNOWN_LOCATION";
	private AddressResultReceiver mResultReceiver;
	private boolean isRecuperarEnderecoPendente = true;

	private static LoginButton botaoLogin;
	private CallbackManager callbackManager;

	private GoogleApiClient mGoogleApiClient;
	private RestHttpClient restClient;
	private String username;
	private String senha;

	private PerfilUsuarioVO perfil;
	private String cep;

	private String URL_LOGIN = Constants.DNS_CASA + "/babetteunhas-backend/rest/login/";
	private String URL_GET_USUARIO_POR_NOME = Constants.DNS_CASA + "/babetteunhas-backend/rest/usuario/nome/";
	private String URL_PUT_USUARIO = Constants.DNS_CASA + "/babetteunhas-backend/rest/usuario/incluir";

	private boolean isLogado = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFacebookSignIn();

		restClient = RestHttpClient.getInstance();

		if (savedInstanceState != null) {
			isResolvendoErro = (savedInstanceState != null
					&& savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false));
			mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
		}

		mResultReceiver = new AddressResultReceiver(new Handler());

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addScope(new Scope(Scopes.PLUS_LOGIN))
				.addApi(LocationServices.API)
				.build();

		// Se ja estiver logado, loga de novo
		if(AccessToken.getCurrentAccessToken() != null) {
			isLogado = true;
			logInFacebook();
		}
	}

	private void initFacebookSignIn() {
		setContentView(R.layout.sign_in_activity);
		FacebookSdk.sdkInitialize(this.getApplicationContext());

		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callWSLogin();
			}
		});

		this.botaoLogin = (LoginButton)findViewById(R.id.facebookSignIn);
		//this.botaoLogin.setText(R.string.facebookSignIn);
		this.botaoLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				logInFacebook();
			}
		});
	}

	// Apos conexao bem-sucedida, chama o svc de "conversao de coordenadas"(geocode) em endereco
	@Override
	public void onConnected(Bundle bundle) {
		Toast.makeText(this, "Conectado ao Google Pay Services com sucesso", Toast.LENGTH_LONG).show();
		// usa o FusionLocationProvider para recuperar as coordenadas da ultima localizacao do dispositivo
		getUserLocation();
	}

	private boolean ehCadastrado(){
		if (this.username != null && this.senha != null){
			PerfilUsuarioVO perfilUsuarioRetornado = new PerfilUsuarioVO();
			Map mapa = new HashMap();
			mapa.put("nome",this.username);
			mapa.put("senha", senha);

			restClient.getJsonObject(URL_LOGIN, mapa, this, perfilUsuarioRetornado);
			if (perfilUsuarioRetornado != null)
				return true;
		}
		return false;
	}

	private void getPerfilUsuario(){
		PerfilUsuarioVO perfilUsuarioRetornado = new PerfilUsuarioVO();
		Map mapa = new HashMap();
		mapa.put("nome", this.username);

		restClient.getJsonObject(URL_GET_USUARIO_POR_NOME, mapa, this,
				perfilUsuarioRetornado);

		this.perfil = perfilUsuarioRetornado;
	}

	private void callWSLogin(){
		this.username = ((EditText)findViewById(R.id.usuario)).getText().toString();
		this.senha = ((EditText)findViewById(R.id.senha)).getText().toString();

		// se nao for cadastrado, exibe a tela de cadastro de perfil
		if (!ehCadastrado()) {
			Intent i = new Intent(this, CadastrarPerfilUsuarioActivity.class);
			startActivity(i);
			if (i.getBundleExtra("PERFIL") != null) {
				this.perfil = new PerfilUsuarioVO(i.getBundleExtra("PERFIL"));
				if (this.cep != null)
					this.perfil.setCep(this.cep);
			}
		}
		else{
			getPerfilUsuario();
			Bundle b = new Bundle();
			b.putParcelable("PERFIL", this.perfil);
			getIntent().putExtras(b);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void logInFacebook() {
		this.callbackManager = CallbackManager.Factory.create();
		if (this.botaoLogin != null) {
			List permissions = new ArrayList();
//            permissions.add("user_education_history");
			permissions.add("email");
			permissions.add("public_profile");
			permissions.add("user_friends");
			this.botaoLogin.setTextLocale(new Locale("pt","BR"));

			this.botaoLogin.setReadPermissions(permissions); //"user_education_history" passa por revisao
			// outras permissoes (precisam passar por revisao do Facebook): "user_place_visits"

			this.botaoLogin.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					GraphRequest req = GraphRequest.newMeRequest(loginResult.getAccessToken(),
							new GraphRequest.GraphJSONObjectCallback() {
								@Override
								public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
									onFacebookSignedIn(jsonObject);
								}
							});
					Bundle b = new Bundle();
					b.putString("fields","id,name,email,gender,birthday,picture");
					req.setParameters(b);
					req.executeAsync();
				}

				@Override
				public void onCancel() {
					// App code
				}

				@Override
				public void onError(FacebookException exception) {
					Toast.makeText(GoogleServicesAPIActivity.this, "Erro durante o login no Facebook", Toast.LENGTH_LONG).show();
					GoogleServicesAPIActivity.this.finish();
				}
			});
			LoginManager.getInstance().logInWithReadPermissions(this,permissions);
		}
		else
			Toast.makeText(this, "Erro durante o login no Facebook: Falha na interface grafica", Toast.LENGTH_LONG).show();
	}

	private void inserirPerfil(PerfilUsuarioVO vo){
		JSONObject json = Utils.toJsonFile(vo);
		// nao ha dialogo de progresso porque a inclusao eh implicita
		restClient.post(URL_PUT_USUARIO, json, this, null);
	}

	private void carregarPerfilPorContaFacebook(JSONObject o){
		PerfilUsuarioVO vo = (PerfilUsuarioVO)Utils.fromJsonObjToVO(o.toString(),PerfilUsuarioVO.class);
		if (this.cep != null)
			vo.setCep(this.cep); //Yp0ZBhF3ZSaSgupfUDdFmUyO17E=

//		inserirPerfil(vo);

// para trazer mais info do profile (obj User)
// https://developers.facebook.com/docs/graph-api/reference/user

//		new GraphRequest(
//				AccessToken.getCurrentAccessToken(),
//				"/{user-id}",
//				null,
//				HttpMethod.GET,
//				new GraphRequest.Callback() {
//					public void onCompleted(GraphResponse response) {
//        				response.
//					}
//				}
//		).executeAsync();

		Utils.putStringOnSharedPrefs(this, "PERFIL", Utils.toJson(vo));
		this.finish();
	}

	private void getUserLocation() {
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			// Determine whether a Geocoder is available.
			if (!Geocoder.isPresent()) {
				Toast.makeText(this, getString(R.string.erro_google_play), Toast.LENGTH_LONG).show();
				return;
			}
			if (isRecuperarEnderecoPendente) {
				startFetchAddressService();
			}
		}
		else{
			Toast.makeText(this, R.string.location_error, Toast.LENGTH_LONG).show();
		}
	}

	// Chama o svc de recuperacao de endereco baseado no obj Location recuperado da GoogleAPI Location
	protected void startFetchAddressService() {
		if (mGoogleApiClient.isConnected() && mLastLocation != null) {
			Intent intent = new Intent(this, FetchAddressIntentService.class);
			intent.putExtra(Constants.RECEIVER, mResultReceiver);
			intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
			this.startService(intent);
			isRecuperarEnderecoPendente = false;
		}
		else{
			isRecuperarEnderecoPendente = true;
		}
	}

	public void onFacebookSignedIn(JSONObject o) {
		Log.d("FACEBOOK_DATA", o.toString());
		// gravar no sharedPrefs (campo 'CEP' tb)
		carregarPerfilPorContaFacebook(o);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		tentarResolverErro(result);
	}

	// Chamado pelo GoogleApiClient apos algum erro de conexao ser resolvido
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(requestCode) {
			case REQUEST_RESOLVE_ERROR:
				isResolvendoErro = false;
				if (resultCode == MainActivity.RESULT_OK) {
					// Make sure the app is not already connected or attempting to connect
					if (!mGoogleApiClient.isConnecting() &&
							!mGoogleApiClient.isConnected()) {
						mGoogleApiClient.connect();
					}
				}else {
					Log.e(getLocalClassName(), "erro no carregamento de info da conta google");
					this.erroConexao = true;
				}
				break;
		}

		if (this.callbackManager != null)
			callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Se o erro tem solucao (ex: erro de autenticacao G+),
	 * o Android exibe dialogos para o usuario (ex: tela de sign in).
	 * Esse metodo evita multiplas chamadas ao metodo "connect" do GoogleAPIClient
	 * caso o app seja suspenso ou essa activity seja recriada.
	 *
	 * @param result resultado da conexao a GoogleAPI.
	 */
	private void tentarResolverErro(ConnectionResult result) {
		if (isResolvendoErro) {
			// Already attempting to resolve an error.
			return;
		} else if (result.hasResolution()) {
			try {
				isResolvendoErro = true;
				result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
			} catch (IntentSender.SendIntentException e) {
				// There was an error with the resolution intent. Try again.
				mGoogleApiClient.connect();
			}
		} else {
			// Show dialog using GooglePlayServicesUtil.getErrorDialog()
			showErrorDialog(result.getErrorCode());
			isResolvendoErro = true;
		}
	}

	/* Creates a dialog for an error message */
	private void showErrorDialog(int errorCode) {
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "errordialog");
	}

	/* A fragment to display an error dialog */
	public static class ErrorDialogFragment extends DialogFragment {
		public ErrorDialogFragment() { }

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR);
			return GooglePlayServicesUtil.getErrorDialog(errorCode,
					this.getActivity(), REQUEST_RESOLVE_ERROR);
		}
	}

	class AddressResultReceiver extends ResultReceiver {
		public AddressResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == Constants.FAILURE_RESULT)
				Toast.makeText(GoogleServicesAPIActivity.this, "Erro de conexao ao Google Play Services", Toast.LENGTH_LONG).show();

			cep = resultData.getString(Constants.CEP);
		}
	}

	// Tenta conectar ao Google Pay Services no inicio da atividade
	@Override
	public void onStart() {
		super.onStart();
		if (!isResolvendoErro && !this.erroConexao) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}
}