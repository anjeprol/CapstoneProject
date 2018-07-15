package com.bharadwaj.android.capstoneproject.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FavoritePlacesWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<String> mFavoritePlacesNames;

    public FavoritePlacesWidgetRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Timber.v("Replacing with new Data Set");
        mFavoritePlacesNames = FavoritePlacesWidgetProvider.placesNamesList;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mFavoritePlacesNames != null ? mFavoritePlacesNames.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Timber.d("Entering getViewAt");

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.favorite_places_widget_item);
        views.setTextViewText(R.id.widget_favorite_place_name_view, mFavoritePlacesNames.get(position));

        Intent placeNameIntent = new Intent(mContext,FavoritesActivity.class);
        placeNameIntent.putExtra(Constants.PLACES_NAMES_LIST, Parcels.wrap(FavoritePlacesWidgetProvider.placesNamesList));
        views.setOnClickFillInIntent(R.id.widget_favorite_place_name_view, placeNameIntent);

        Timber.d("Leaving getViewAt");
        Timber.v("Returning a new RemoteViews instance");
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
