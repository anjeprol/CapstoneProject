package com.bharadwaj.android.capstoneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import timber.log.Timber;
import timber.log.Timber.DebugTree;


public class MapActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Timber.plant(new DebugTree());
        Timber.d("Entering onCreate");

        setSupportActionBar(toolbar);

        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment_placeholder, mapFragment).commit();

        Timber.d("Leaving onCreate");
    }


}
