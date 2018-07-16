package com.bharadwaj.android.capstoneproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;
import com.bharadwaj.android.capstoneproject.utils.ExtractionUtils;

import org.parceler.Parcels;

import java.util.ArrayList;

import timber.log.Timber;

public class FavoritePlacesWidgetProvider extends AppWidgetProvider {

    static ArrayList<String> placesNamesList;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Timber.d("Entering updateAppWidget");

        placesNamesList = ExtractionUtils.getPlacesNamesListFromCursor(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fevorite_places_widget);

        Intent recipestepsIntent = new Intent(context, FavoritesActivity.class);
        recipestepsIntent.addCategory(Intent.ACTION_MAIN);
        recipestepsIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        recipestepsIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipestepsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.favorite_places_widget_list_view, pendingIntent);

        // Set the FavoritePlacesWidgetRemoteViewsService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, FavoritePlacesWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.favorite_places_widget_list_view, intent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Timber.d("Leaving updateAppWidget");

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("Entering onUpdate");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Timber.d("Leaving onUpdate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Entering onReceive");

        //Receiving the intent Broadcast Sent through IntentService
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoritePlacesWidgetProvider.class));
        final String action = intent.getAction();
        Timber.v("Action : %s", action);

        if (action != null && action.equals(Constants.UPDATE_ACTION)) {
            placesNamesList = Parcels.unwrap(intent.getExtras().getParcelable(Constants.PLACES_NAMES_LIST));
            //Timber.v("Places in onReceive has %s items", placesNamesList.size());

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.favorite_places_widget_list_view);

            //Now Updating all widgets
            onUpdate(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
        Timber.d("Leaving onReceive");

    }
}

