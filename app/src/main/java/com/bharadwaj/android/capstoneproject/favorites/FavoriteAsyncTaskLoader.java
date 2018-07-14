package com.bharadwaj.android.capstoneproject.favorites;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.bharadwaj.android.capstoneproject.adapters.PlacesAdapter;

import timber.log.Timber;

public class FavoriteAsyncTaskLoader extends AsyncTaskLoader<Cursor> {

    PlacesAdapter mPlacesFavoritesAdapter;

    public FavoriteAsyncTaskLoader(@NonNull Context context, PlacesAdapter placesFavoritesAdapter) {
        super(context);
        mPlacesFavoritesAdapter = placesFavoritesAdapter;
    }

    @Override
    protected void onStartLoading() {
        Timber.v("Entering onStartLoading");
        super.onStartLoading();

        Cursor cursor = mPlacesFavoritesAdapter.getCursor();
        if(null != cursor){
            deliverResult(cursor);
        }else {
            Timber.v("Cursor is Null. Generating");
            //mProgressBar.setVisibility(View.VISIBLE);
            forceLoad();
        }

        Timber.v("Leaving onStartLoading");
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        Timber.v("Entering loadInBackground");

        Cursor cursor;
        cursor = getContext().getContentResolver().query(FavoriteContract.Favorites.PLACES_CONTENT_URI,
                null,
                null,
                null,
                null);
        Timber.v("Cursor length : %s", cursor.getCount());
        Timber.v("Leaving loadInBackground");
        return cursor;
    }

    @Override
    protected void onStopLoading() {
        Timber.v("Entering onStopLoading");
        super.onStopLoading();
        Timber.v("Leaving onStopLoading");
    }

    @Override
    public void deliverResult(@Nullable Cursor data) {
        Timber.v("Delivering result");
        super.deliverResult(data);
    }
}
