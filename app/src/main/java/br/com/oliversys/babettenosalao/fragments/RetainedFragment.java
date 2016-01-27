package br.com.oliversys.babettenosalao.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
    Fragment criado para manter o estado de uma Activity.
    Ele pode ser usado para armezenar qq tipo de objeto,
    inclusive Layouts e outros q nao implementam Parceable, ou seja,
    nao esta sujeito a limitacao do onSaveInstanceState(Bundle b).

    @link http://developer.android.com/guide/topics/resources/runtime-changes.html
 */
public class RetainedFragment extends Fragment {

    private FrameLayout framePrincipalDaMainActivity;

    // salvar as fotos baixadas nos listViews (para n tr q baixar de novo)... talvez n precise pq fica no cache

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public FrameLayout getFramePrincipalDaMainActivity() {
        return framePrincipalDaMainActivity;
    }

    public void setFramePrincipalDaMainActivity(FrameLayout framePrincipalDaMainActivity) {
        this.framePrincipalDaMainActivity = framePrincipalDaMainActivity;
    }

}
