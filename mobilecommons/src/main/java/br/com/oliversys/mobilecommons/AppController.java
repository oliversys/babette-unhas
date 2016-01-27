package br.com.oliversys.mobilecommons;

import android.support.multidex.MultiDexApplication;

/**
 *  Controlador geral do aplicativo. Contem os componentes
 *  de escopo de aplicacao como o cliente REST.
 */
public class AppController extends MultiDexApplication {
	 	 
    public static final String TAG = AppController.class.getSimpleName();
    private static RestHttpClient restClient;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public synchronized RestHttpClient getRestClient(){
        return RestHttpClient.getInstance();
    }
}
