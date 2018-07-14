package com.bharadwaj.android.capstoneproject.favorites;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    static final String AUTHORITY = "com.bharadwaj.android.capstoneproject";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Possible paths to access data:
    static final String FAVORITES = "favorites";
    static final String FAVORITE = "favorites/#";

    public static final class Favorites implements BaseColumns {

        public static final Uri PLACES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        // "_ID" column will be automatically generated due to the BaseColumns
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_PLACE_RATING = "place_rating";
        public static final String COLUMN_PLACE_PRICE_LEVEL = "place_price_level";
        public static final String COLUMN_ADDRESS = "place_address";
        //public static final String COLUMN_MOVIE_USER_RATING = "place_";
    }

}