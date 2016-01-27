package br.com.oliversys.babettenosalao;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import babette.oliversys.com.br.babettenosalao.R;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver mReceiver;

    public FetchAddressIntentService(){
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Address> enderecos = null;
        String errorMessage = null;

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf("RECUPERACAO DO RESULT RECEIVER", "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        String cep = null;
        try {
            enderecos = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
        } catch (IOException e) {
            e.printStackTrace();
            deliverResultToReceiver(Constants.FAILURE_RESULT, null, null);
            return;
        }

        // nenhum endereco encontrado
        if (enderecos == null || enderecos.size()  == 0) {
            if (errorMessage != null && errorMessage.isEmpty()) {
                Log.e("FETCH_ADDRESS_SVC", getString(R.string.nenhum_local));
                deliverResultToReceiver(Constants.SUCCESS_RESULT, null, null);
            }
            ArrayList<String> erro = new ArrayList<>();
            erro.add(errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, erro, null);
        } else {
            ArrayList<String> enderecosStr = new ArrayList<String>();
            ArrayList<Address> listaAuxiliar = new ArrayList<Address>();

            for(Address a:enderecos){
                // nao exibe enderecos repetidos no dialog
                if (listaAuxiliar.contains(a))
                    continue;
                else
                    listaAuxiliar.add(a);

                Log.i("FETCH_ADDRESS_SVC", "Endereco encontrado: " + enderecosStr);
                enderecosStr.add(a.getAddressLine(0));
                cep = a.getPostalCode();
                if (cep != null)
                    break;
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT, enderecosStr, cep);
        }
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<String> enderecos, String cep) {
        Bundle bundle = new Bundle();

        if (resultCode == Constants.FAILURE_RESULT)
            mReceiver.send(resultCode, bundle);

        if (enderecos != null && (enderecos.isEmpty() || cep == null))
            resultCode = Constants.NO_ADDRESS_FOUND;

        bundle.putStringArrayList(Constants.RESULT_DATA_KEY, enderecos);
        bundle.putString(Constants.CEP, cep);
        try{
            mReceiver.send(resultCode, bundle);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
        }

    }
}
