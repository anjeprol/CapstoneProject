package com.bharadwaj.android.capstoneproject.favorites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bharadwaj.android.capstoneproject.PlaceFragment;
import com.bharadwaj.android.capstoneproject.R;

import timber.log.Timber;

public class FavoritesActivity extends AppCompatActivity{

    PlaceFragment mPlaceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Timber.plant(new Timber.DebugTree());
        Timber.d("Entering onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.favorites_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPlaceFragment = new PlaceFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.favorites_places_frame, mPlaceFragment).commit();

        Timber.d("Leaving onCreate");
    }
}
