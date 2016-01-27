package br.com.oliversys.babettenosalao.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.NavDrawerItem;
import br.com.oliversys.babettenosalao.NavDrawerListAdapter;
import br.com.oliversys.babettenosalao.valueobject.PerfilUsuarioVO;
import br.com.oliversys.mobilecommons.RestHttpClient;
import br.com.oliversys.mobilecommons.Utils;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private ListView mDrawerListView;
    private List<NavDrawerItem> drawerItems;
    private ArrayAdapter navigationDrawerAdapter;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private DrawerLayout mDrawerLayout;

    private RelativeLayout relativeLayout;
    private RestHttpClient restClient;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (container != null){
            View v = container.findViewById(R.id.container);
            if (v != null)
                return v;
        }

        this.relativeLayout = (RelativeLayout)inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        mDrawerListView = (ListView) this.relativeLayout.findViewById(R.id.left_drawer);
                mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                });

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return this.relativeLayout;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Quiz");
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            getToolbar().setDisplayHomeAsUpEnabled(false);
            getToolbar().setHomeButtonEnabled(false);
        }

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        setUpListViewDrawer();

        addItemsToDrawer();
        navigationDrawerAdapter = new NavDrawerListAdapter(getActivity(), drawerItems);
        mDrawerListView.setAdapter(navigationDrawerAdapter);

        setUpDrawerListener(drawerLayout);

        populateDrawerHeader();

        if (mDrawerLayout != null) {
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    drawerToggle.syncState();
                }
            });
        }
    }

    private void setUpDrawerListener(final DrawerLayout drawerLayout) {
        this.drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().supportInvalidateOptionsMenu();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
                drawerToggle.syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()

                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                drawerToggle.setDrawerIndicatorEnabled(true);
                drawerToggle.syncState();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();
    }

    private void setUpListViewDrawer() {
        mDrawerListView = (ListView) this.relativeLayout.findViewById(R.id.left_drawer);
        View header = getActivity().getLayoutInflater().inflate(R.layout.drawer_header, mDrawerListView, false);
        mDrawerListView.addHeaderView(header);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.drawer_footer, mDrawerListView, false);
        mDrawerListView.addFooterView(footer);
    }

    private void populateDrawerHeader()
    {
        String perfil = Utils.getFromSharedPrefs(getActivity().getApplicationContext(), "PERFIL");
        PerfilUsuarioVO user = (PerfilUsuarioVO)Utils.fromJsonObjToVO(perfil, PerfilUsuarioVO.class);

        String email = user.getEmail();
        TextView textoEmail = (TextView) getActivity().findViewById(R.id.email);
        if (textoEmail != null)
            textoEmail.setText(email);
        if (user.getName() != null) {
            TextView textoName = (TextView) getActivity().findViewById(R.id.name);
            if (textoName != null)
                textoName.setText(user.getName()); // drawer header
        }

        if (user.getFotoURL() != null) {
            final ImageView pictureView = (ImageView) getActivity().findViewById(R.id.circleView);
            if (pictureView != null)
                this.restClient.downloadImage(user.getFotoURL(), getActivity(), pictureView);
        }
    }

    private void addItemsToDrawer() {
        this.drawerItems = new ArrayList<>();
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section0), R.drawable.ic_perfil));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section1), R.drawable.ic_favoritos));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section2), R.drawable.ic_busca));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section3), R.drawable.ic_dicas));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section4), R.drawable.ic_promo));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section5), R.drawable.ic_blog));
        drawerItems.add(new NavDrawerItem(getString(R.string.title_section6), R.drawable.ic_mail));
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
        drawerToggle.syncState();
    }

    private ActionBar getToolbar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
