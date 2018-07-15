package com.bharadwaj.android.capstoneproject.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import timber.log.Timber;

public class FavoritePlacesWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Timber.v("Returning a new FavoritePlacesWidgetRemoteViewsFactory instance");
        return new FavoritePlacesWidgetRemoteViewsFactory(getApplicationContext());
    }
}
