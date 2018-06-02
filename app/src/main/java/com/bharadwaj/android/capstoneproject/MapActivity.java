package com.bharadwaj.android.capstoneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;
import timber.log.Timber.DebugTree;


public class MapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Timber.plant(new DebugTree());
        Timber.d("Entering onCreate");

        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment_placeholder, mapFragment).commit();

        Timber.d("Leaving onCreate");
    }




}
