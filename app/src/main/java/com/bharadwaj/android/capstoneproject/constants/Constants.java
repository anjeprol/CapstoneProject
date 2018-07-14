package com.bharadwaj.android.capstoneproject.constants;

import android.net.Uri;

public final class Constants {

    public static final int PLACE_PICKER_REQUEST = 1;
    public static final String PLACES_API_KEY_FROM_MANIFEST = "com.google.android.geo.API_KEY";
    public static final String PLACES_API_KEY = "key";
    public static final String androidns="http://schemas.android.com/apk/res/android";

    public static final String ADMOB_APP_ID_FROM_MANIFEST = "ADMOB_APP_ID";

    public static final String DIALOG_MESSAGE = "dialogMessage";
    public static final String TEXT = "text";
    public static final String DEFAULT_VALUE = "defaultValue";

    public static final String MAX = "max";
    public static final String RADIUS_SUMMARY_TEXT = "Current radius is ";
    public static final String BASE_URL = "https://maps.googleapis.com/";
    public static final String OK = "OK";

    //Strings matching parameter keys/values from Google API : https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters
    public static final String TYPE_OF_PLACE = "type";
    public static final String LOCATION = "location";
    public static final String RADIUS = "radius";
    public static final String RANK_BY = "rankby";
    public static final String DISTANCE = "distance";
    public static final String PROMINENCE = "prominence";


    public static final String LOCATION_NAME = "location name";
    public static final String LOCATION_COORDINATES = "location coordinates";
    public static final String LOCATION_SPECIFICS_SAPERATOR = ":::";

    //Intent headers and End points
    public static final String MAPS_END_POINT = "google.navigation:q=";
    public static final String SMS_INTENT_DATA = "sms:";
    public static final String CALL_INTENT_DATA = "tel:";
    public static final String SMS_BODY_HEADER = "sms_body";
    public static final String SMS_BODY = "Hey...\n Check this place out. \n";



    //Place Types:
    public static final String PHARMACY = "pharmacy";
    public static final String DOCTOR = "doctor";

    public static final String RESTAURANT = "restaurant";
    public static final String BAKERY = "bakery";
    public static final String CAFE = "cafe";

    public static final String AMUSEMENT_PARK = "amusement_park";
    public static final String CASINO = "casino";
    public static final String NIGHT_CLUB = "night_club";

    public static final String CAR_WASH = "car_wash";
    public static final String CAR_REPAIR = "car_repair";
    public static final String CAR_DEALER = "car_dealer";
    public static final String CAR_RENTAL = "car_rental";

    public static final int RADIUS_MULTIPLIER = 1000;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    public static final String NO_PLACES_EXPLANATION_TEXT = "Sorry. No Places found";
    public static final String NO_FAVORITES_EXPLANATION_TEXT = "No Favorite Places Yet.";
    public static final String FAVORITES_SWIPE_EXPLANATION_TEXT = "Here're your Favorite Places. \nSwipe left/right to delete a place.";

}
