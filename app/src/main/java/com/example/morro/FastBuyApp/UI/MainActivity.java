package com.example.morro.FastBuyApp.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.morro.FastBuyApp.Core.Store;
import com.example.morro.FastBuyApp.R;

/**
 * The activity which will contain the fragment which will list elements through a RecyclerView
 */
public abstract class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 0;    //have we got the permissions?
    private static boolean mainLoaded = false;      //remember if the first fragment has been loaded already
    protected DrawerLayout drawer;
    private Toolbar toolbar;
    protected Store store;


    /**
     * Gets called when the activity is created
     * @param savedInstanceState bundle of objects which can be passed upon creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeSetup();

        /* Ask for Android permissions */
        askForPermission();

        /* TOOLBAR Instantiation */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadMainFragment();
    }


    /**
     * Instantiate the store object with initial configuration
     */
    protected void storeSetup(){
        store = new Store();
        String root = Environment.getExternalStorageDirectory().toString();
        store.loadDatabase(root +Store.DEFAULT_STORE_PATH);
    }

    /** Instantiate the main fragment in the view (left to the childs) */
    protected abstract void loadMainFragment();

    /**
     *  Close the Navigation Drawer if hardware back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Load another fragment in place in a container
     * @param containerViewId : the container on the xml view
     * @param fragment : the fragment which is to be placed
     */
    protected void replaceFragment(@IdRes int containerViewId, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId,fragment);
        if(mainLoaded)
            transaction.addToBackStack(null);
        else
            mainLoaded = true;
            transaction.commit();
    }

    /**
     * Ask the user to grant the permissions needed for the app to work.
     */
    private void askForPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
        }
    }

}
