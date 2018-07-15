package com.bharadwaj.android.capstoneproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;
import com.bharadwaj.android.capstoneproject.settings.SettingsActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    PlaceFragment mPlaceFragment;
    String placeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        Timber.d("Entering onCreate");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        placeType = sharedPreferences.getString(Constants.TYPE_OF_PLACE, "");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPlaceFragment = new PlaceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE_OF_PLACE, placeType);
        mPlaceFragment.setArguments(bundle);
        Timber.v("Saved Place type : %s", placeType);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment_placeholder, mPlaceFragment).commit();

        Timber.d("Leaving onCreate");
    }


    @Override
    public void onBackPressed() {
        Timber.d("Entering onBackPressed");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Timber.d("Leaving onBackPressed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Timber.d("Entering onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Timber.d("Leaving onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("Entering onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_favorites:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
            case R.id.action_sort_by_distance:
                Timber.v("Sorting by distance");
                mPlaceFragment.loadPlaces(placeType, Constants.DISTANCE);

            case R.id.action_sort_by_default:
                Timber.v("Sorting by prominence (Default)");
                mPlaceFragment.loadPlaces(placeType, Constants.PROMINENCE);

        }
        Timber.d("Leaving onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Timber.d("Entering onNavigationItemSelected");

        int id = item.getItemId();

        switch (id) {
            case R.id.pharmacy:
                placeType = Constants.PHARMACY;
                break;

            case R.id.doctor:
                placeType = Constants.DOCTOR;
                break;

            case R.id.restaurant:
                placeType = Constants.RESTAURANT;
                break;

            case R.id.bakery:
                placeType = Constants.BAKERY;
                break;

            case R.id.cafe:
                placeType = Constants.CAFE;
                break;

            case R.id.amusement_park:
                placeType = Constants.AMUSEMENT_PARK;
                break;

            case R.id.casino:
                placeType = Constants.CASINO;
                break;

            case R.id.night_club:
                placeType = Constants.NIGHT_CLUB;
                break;

            case R.id.car_wash:
                placeType = Constants.CAR_WASH;
                break;

            case R.id.car_repair:
                placeType = Constants.CAR_REPAIR;
                break;

            case R.id.car_dealer:
                placeType = Constants.CAR_DEALER;
                break;

            case R.id.car_rental:
                placeType = Constants.CAR_RENTAL;
                break;

            default:
                Timber.v("ID not recognized");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Constants.TYPE_OF_PLACE, placeType).apply();

        mPlaceFragment.loadPlaces(placeType, "");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Timber.d("Leaving onNavigationItemSelected");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("Entering onDestroy");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Constants.TYPE_OF_PLACE, "").apply();
        Timber.d("Leaving onDestroy");
    }

}

