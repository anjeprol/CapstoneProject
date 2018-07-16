package com.bharadwaj.android.capstoneproject.utils;

import android.content.Context;
import android.database.Cursor;

import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import timber.log.Timber;

public class ExtractionUtils {

    public static ArrayList<String> getPlacesNamesListFromCursor(Context context) {

        ArrayList<String> placesNamesList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(FavoriteContract.Favorites.PLACES_CONTENT_URI,
                null,
                null,
                null,
                null);
        Timber.v("Cursor length : " + cursor.getCount());
        for (int i = 0 ;i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String placeName = cursor.getString(cursor.getColumnIndex(FavoriteContract.Favorites.COLUMN_PLACE_NAME));
            placesNamesList.add(placeName);
        }

        return placesNamesList;
    }

    public static String getFormattedLocationString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }
}
