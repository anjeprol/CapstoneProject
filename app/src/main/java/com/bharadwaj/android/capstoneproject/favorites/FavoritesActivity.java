package com.bharadwaj.android.capstoneproject.favorites;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.bharadwaj.android.capstoneproject.PlaceFragment;
import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import timber.log.Timber;

import static com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites;

public class FavoritesActivity extends AppCompatActivity
        implements PlaceFragment.OnCallButtonInteractionListener,
        PlaceFragment.OnMapsButtonInteractionListener,
        PlaceFragment.OnWebsiteButtonInteractionListener,
        PlaceFragment.OnShareButtonInteractionListener,
        PlaceFragment.OnFavoriteButtonInteractionListener{

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

    private String getFormattedLocationString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    public boolean isIntentAvailable(Intent intent) {
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


}
