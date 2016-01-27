package br.com.oliversys.babettenosalao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.fragments.NavigationDrawerFragment;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;


public class MainActivity extends AppCompatActivity{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private RestHttpClient restClient;
    private FrameLayout layoutPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);

        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationDrawerFragment.setUp(R.id.fragment_nav_drawer,layout);

        chamarQuiz();
    }

    private void chamarQuiz(){

        Intent i = new Intent(this,QuizActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
// Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        Utils.showHashKey(getApplicationContext()); // debug
    }

    @Override
    protected void onPause() {
        super.onPause();
// Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.menu_blog || id == R.id.menu_buscar || id == R.id.menu_dicas
//                || id == R.id.menu_fale || id == R.id.menu_favoritos || id == R.id.menu_perfil
//                || id == R.id.menu_promo || id == R.id.menu_config) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
