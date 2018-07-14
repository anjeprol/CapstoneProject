package com.bharadwaj.android.capstoneproject.service;

import com.bharadwaj.android.capstoneproject.api_utils.GooglePlacesAPIResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Bharadwaj on 6/1/18.
 */

public interface RequestBuilder {

    @GET("/maps/api/place/nearbysearch/json")
    Call<GooglePlacesAPIResponse> getPlaces(@QueryMap Map<String, String> dataMap);

}
