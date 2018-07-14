package com.bharadwaj.android.capstoneproject.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bharadwaj.android.capstoneproject.api_utils.GooglePlacesAPIResponse;

import java.util.HashMap;
import java.util.Map;

import com.bharadwaj.android.capstoneproject.constants.Constants;
import retrofit2.Call;
import com.bharadwaj.android.capstoneproject.service.RequestBuilder;
import com.bharadwaj.android.capstoneproject.service.ServiceCreator;
import timber.log.Timber;

/**
 * Created by Bharadwaj on 6/1/18.
 */

public class NetworkUtils {

    public static Call<GooglePlacesAPIResponse> getPlaces(Map<String, String> preferenceMap){

        /*preferenceMap.put("location", "39.683723,-75.749657");
        preferenceMap.put("radius", "1500");
        preferenceMap.put("type", "gym");
        preferenceMap.put("key", "AIzaSyDShKzie-o9lFY0a6zdBuLTAf95DDV_W9w");
*/

        Timber.d("Fetching Places on Network");
        return ServiceCreator.createService(RequestBuilder.class, Constants.BASE_URL).getPlaces(preferenceMap);
    }

    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnectedToInternet = (networkInfo != null) && networkInfo.isConnectedOrConnecting();
        if (isConnectedToInternet) {
            Timber.v("Not connected to Internet");
            return true;
        } else {
            return false;
        }
    }

}
