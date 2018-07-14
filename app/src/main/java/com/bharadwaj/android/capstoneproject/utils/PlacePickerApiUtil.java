package com.bharadwaj.android.capstoneproject.utils;

import android.content.Context;

import com.google.android.gms.location.places.ui.PlacePicker;

import timber.log.Timber;

import static android.app.PendingIntent.getActivity;

public final class PlacePickerApiUtil {

    public static void pickPlace(Context context){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Timber.v("Timber : Starting Place Picker Intent...");
        /*try {
            startActivityForResult(builder.build((Activity) context), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
*/
    }

}
