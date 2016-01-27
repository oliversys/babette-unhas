package br.com.oliversys.mobilecommons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RestHttpClient
{
    private static AsyncHttpClient client;
    private static RestHttpClient instance;

    public static synchronized RestHttpClient getInstance()
    {
        if (instance == null){
            instance = new RestHttpClient();
            client = new AsyncHttpClient();
        }

        return instance;
    }

    private RestHttpClient(){}

    /**
     * Baixa a imagem da url informada e preenche o imageView especificado.
     *
     * @param url
     * @param ctx
     * @param mImageView
     */
    public void downloadImage(String url,Context ctx,final ImageView mImageView){
        Picasso.with(ctx)
                .load(url)
                .error(R.drawable.image_load_error)
                .into(mImageView);
    }

    /**
     * Invoca o servico REST na url informada utilizando o responseHandler padrao, e obtem um object json como resposta.
     * Este metodo convertera o jsonObject em um mapa.
     * Utilize este metodo para servicos do tipo HTTP GET (consulta de dados).
     *
     * @param url endereco do servico REST
     * @param parametros parametros do servico REST
     * @param voClass VO com os dados que serao incluidos
     * @param activity Android Activity chamadora deste metodo
     */
    public void getJsonObject(String url, Map parametros, final Activity activity,
                              final IValueObject voClass)
    {
        RequestParams params = new RequestParams();
        params.put("params",parametros);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                IValueObject obj = Utils.fromJsonObjToVO(response.toString(), voClass.getClass());
                if (obj == null) {
                    Log.e(RestHttpClient.class.getSimpleName(),
                            "Lista de " + voClass.getClass().getSimpleName() + " retornou vazia");
                    activity.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(RestHttpClient.class.getSimpleName(), "Falha na recuperacao de " + voClass.getClass().getSimpleName());
            }
        });
    }

    /**
     * Invoca o servico REST na url informada utilizando o responseHandler padrao, e obtem um array string como resposta.
     * Utilize este metodo para servicos do tipo HTTP GET (consulta de dados).
     *
     * @param url endereco do servico REST
     * @param parametros parametros do servico REST
     * @param activity Android Activity chamadora deste metodo
     * @param adapter array adapter que recebera os dados retornados da consulta
     */
    public void getJsonStringList(String url, Map parametros,final Activity activity,final ArrayAdapter adapter){

        RequestParams params = new RequestParams();
        params.put("params",parametros);
        client.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<String> lista = (ArrayList<String>) new Gson().fromJson(response.toString(),
                        new TypeToken<ArrayList<String>>() {
                        }.getType());
                if (lista != null) {
                    // popula o adapter com os valores do VO gravados no BANCO DE DADOS
                    adapter.addAll(lista);
                    adapter.notifyDataSetChanged();
                }else {
                    Log.e(RestHttpClient.class.getSimpleName(),"Lista de retornou vazia");
                    activity.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                handleFailure(activity, throwable);
            }
        });
    }

    /**
     * Invoca o servico REST na url informada utilizando o responseHandler padrao, e obtem um array json como vresposta.
     * Utilize este metodo para servicos do tipo HTTP GET (consulta de dados).
     *
     * @param url endereco do servico REST
     * @param parametros parametros do servico REST
     * @param voPreenchido VO com os dados que serao incluidos
     * @param activity Android Activity chamadora deste metodo
     * @param progress (nullable) dialogo de progresso, se desejar
     * @param viewAdapter view adapter que recebera os dados retornados da consulta
     */
    public void getJsonArray(String url, Map parametros,final ISwappableView<IValueObject> viewAdapter,
             final IValueObject voPreenchido,final Activity activity, final Dialog progress) {

        RequestParams params = new RequestParams();
        params.put("params",parametros);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<IValueObject> lista =
                        (List<IValueObject>) Utils.fromJsonArrayToListVO(response.toString(), voPreenchido.getClass());
                if (lista != null) {
                    if (viewAdapter != null)
                        viewAdapter.swapRecords(lista);
                    if (progress != null)
                        progress.dismiss();
                } else {
                    Log.e(RestHttpClient.class.getSimpleName(),
                            "Lista de " + voPreenchido.getClass().getSimpleName() + " retornou vazia");

                    // mata a atividade corrente e volta para a MainActivity
                    activity.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handleFailure(activity, throwable);
            }
        });
    }

    /**
     * Invoca o servico REST na url informada utilizando o responseHandler padrao, e obtem um array json como vresposta.
     * Utilize este metodo para servicos do tipo HTTP GET (consulta de dados).
     *
     * @param url endereco do servico REST
     * @param parametros parametros do servico REST
     * @param VOclass class do VO (PerguntaVO.class). Foi parametrizado para evitar dep. ciclica
     * @param activity Android Activity chamadora deste metodo
     * @param progress (nullable) dialogo de progresso, se desejar
     */
    public void getJsonArrayWithNoListView(String url, Map parametros, final List<IValueObject> listaVO,
                                           final Activity activity, final Dialog progress, final Class VOclass) {

        RequestParams params = new RequestParams();
        params.put("params",parametros);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<IValueObject> lista =
                        (List<IValueObject>) Utils.fromJsonArrayToListVO(response.toString(), VOclass);
                if (lista != null && !lista.isEmpty()) {
                    listaVO.addAll(lista);
                    if (progress != null)
                        progress.dismiss();
                } else {
                    Log.e(RestHttpClient.class.getSimpleName(),
                            "Lista de " + VOclass.getSimpleName() + " retornou vazia");

                    // mata a atividade corrente e volta para a MainActivity
                    activity.finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handleFailure(activity, throwable);
            }
});
    }

    /**
     * Invoca o servico REST na url informada utilizando o responseHandler padrao.
     * Utilize este metodo para servicos do tipo HTTP POST (inclusao de dados).
     *
     * @param url endereco do servico REST
     * @param jsonFile arquivo JSON de entrada do servico REST
     * @param activity Android Activity chamadora deste metodo
     */
    public void post(String url, JSONObject jsonFile, final Activity activity, Dialog progress)
    {
        if (jsonFile == null){
            Log.e(RestHttpClient.class.getSimpleName(), "metodo post chamado sem parametros. Preencha o objeto json");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("JSON",jsonFile);
        client.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                String nomeEntidadeNegocio = activity.getClass().getSimpleName().replace("Activity", "");
                Log.i(RestHttpClient.class.getSimpleName(), nomeEntidadeNegocio+" incluido(a) com sucesso");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handleFailure(activity,throwable);
            }
        });
        progress.dismiss();
    }

    private void handleFailure(Activity activity,Throwable throwable){
        // formata e exibe a msg de erro
        String nomeEntidadeNegocio = activity.getClass().getSimpleName().replace("Activity", "");
        Log.e(RestHttpClient.class.getSimpleName(), "Erro na recuperacao de " + nomeEntidadeNegocio
                + ": " + throwable.getMessage());
        Toast.makeText(activity, "Falha na recuperacao de " + nomeEntidadeNegocio
                , Toast.LENGTH_LONG).show();
        // mata a atividade corrente e volta para a MainActivity
        activity.finish();
    }
}
