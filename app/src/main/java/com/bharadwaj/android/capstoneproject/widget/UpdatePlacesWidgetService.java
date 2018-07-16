package com.bharadwaj.android.capstoneproject.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.bharadwaj.android.capstoneproject.constants.Constants;

import org.parceler.Parcels;

import java.util.ArrayList;

import timber.log.Timber;

public class UpdatePlacesWidgetService extends IntentService {

    public UpdatePlacesWidgetService() {
        super("UpdatePlacesWidgetService");
    }

    public static void startFavoritePlacesIntentService(Context context, ArrayList<String> placesNamesList) {
        Timber.d("Entering startFavoritePlacesIntentService");
        Intent intent = new Intent(context, UpdatePlacesWidgetService.class);

        Timber.v("CustomPlace count in startFavoritePlacesIntentService : %s", placesNamesList.size());

        intent.putExtra(Constants.PLACES_NAMES_LIST, Parcels.wrap(placesNamesList));
        context.startService(intent);
        Timber.d("Leaving startFavoritePlacesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("Entering onHandleIntent");
        if (intent != null) {
            ArrayList<String> placesNamesList = Parcels.unwrap(intent.getExtras().getParcelable(Constants.PLACES_NAMES_LIST));
            Intent newIntent = new Intent(Constants.UPDATE_ACTION);
            Timber.v("CustomPlace in onHandleIntent : %s", placesNamesList.size());
            newIntent.setAction(Constants.UPDATE_ACTION);
            newIntent.putExtra(Constants.PLACES_NAMES_LIST, Parcels.wrap(placesNamesList));

            //Broadcasting the given intent to interested BroadcastReceivers (AppWidgetProvider in this project).
            sendBroadcast(newIntent);
        }
        Timber.d("Leaving onHandleIntent");
    }
}
