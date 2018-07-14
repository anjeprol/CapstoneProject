package com.bharadwaj.android.capstoneproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;
import com.bharadwaj.android.capstoneproject.settings.SettingsActivity;
import com.google.android.gms.location.places.Place;

import java.util.List;

import timber.log.Timber;

import static com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlaceFragment.OnCallButtonInteractionListener,
        PlaceFragment.OnMapsButtonInteractionListener,
        PlaceFragment.OnWebsiteButtonInteractionListener,
        PlaceFragment.OnShareButtonInteractionListener,
        PlaceFragment.OnFavoriteButtonInteractionListener{

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

    @Override
    public void onCallButtonInteraction(String phoneNumber) {

        Timber.v("Call button clicked. ");
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Phone number empty", Toast.LENGTH_SHORT).show();
        } else {
            Uri phoneCallUri = Uri.parse(Constants.CALL_INTENT_DATA + phoneNumber);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneCallUri);
            if (isIntentAvailable(callIntent)) {
                Timber.v("Opening call intent to call : %s", phoneNumber);
                startActivity(callIntent);
            }
        }
    }

    @Override
    public void onMapsButtonInteraction(String latLong) {

        Timber.v("Maps button clicked. ");
        Uri googleMapsEndPointUri = Uri.parse(Constants.MAPS_END_POINT + latLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsEndPointUri);
        //mapIntent.setPackage("com.google.android.apps.maps");
        if (isIntentAvailable(mapIntent)) {
            Timber.v("Opening Maps intent for coordinates : %s", googleMapsEndPointUri.getAuthority());
            startActivity(mapIntent);
        }

    }

    @Override
    public void onWebsiteButtonInteraction(Uri websiteUri) {

        Timber.v("Website button clicked. ");
        if (websiteUri == null) {
            Toast.makeText(this, "No Website linked", Toast.LENGTH_SHORT).show();
        }else {
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteUri);
            if (isIntentAvailable(websiteIntent)) {
                Timber.v("Opening Website intent to Uri : %s", websiteUri);
                startActivity(websiteIntent);
            }
        }
    }

    @Override
    public void onShareButtonInteraction(Uri websiteUrl) {

        Timber.v("Share button clicked. ");
        if (websiteUrl == null) {
            Toast.makeText(this, "No Website linked to share", Toast.LENGTH_SHORT).show();
        }else{
            Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SMS_INTENT_DATA));
            shareIntent.putExtra(Constants.SMS_BODY_HEADER, Constants.SMS_BODY + websiteUrl);
            if (isIntentAvailable(shareIntent)) {
                Timber.v("Opening message intent");
                startActivity(shareIntent);
            }
        }
    }

    @Override
    public void onFavoriteButtonInteraction(Place place) {
        Timber.v("Favorite button clicked. ");
        Timber.v("Inserting Place : %s", place.toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(Favorites.COLUMN_PLACE_ID, place.getId());
        contentValues.put(Favorites.COLUMN_PLACE_NAME, String.valueOf(place.getName()));
        contentValues.put(Favorites.COLUMN_PLACE_RATING, String.valueOf(place.getRating()));
        contentValues.put(Favorites.COLUMN_PLACE_PRICE_LEVEL, String.valueOf(place.getPriceLevel()));
        contentValues.put(Favorites.COLUMN_ADDRESS, String.valueOf(place.getAddress()));

        Uri insertedRowUri = getContentResolver().insert(FavoriteContract.Favorites.PLACES_CONTENT_URI, contentValues);
        Timber.v("Inserted Row Uri is : %s", insertedRowUri.getPath());
    }

    public boolean isIntentAvailable(Intent intent) {
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public interface SortByChangedListener{
        void onSortByChanged();
    }

}

