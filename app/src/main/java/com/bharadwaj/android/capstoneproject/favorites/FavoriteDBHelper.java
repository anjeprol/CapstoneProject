package com.bharadwaj.android.capstoneproject.favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites;
import timber.log.Timber;

public class FavoriteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritesDB.db";
    private static final int VERSION = 1;

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Timber.v("FavoriteDBHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase favoritesDB) {

        Timber.v("Entering onCreate");
        final String TABLE_NAME = "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                Favorites._ID + " INTEGER PRIMARY KEY, " +
                Favorites.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                Favorites.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                Favorites.COLUMN_PLACE_RATING + " TEXT NOT NULL, " +
                Favorites.COLUMN_PLACE_PRICE_LEVEL + " TEXT NOT NULL, " +
                Favorites.COLUMN_ADDRESS + " TEXT NOT NULL);";

        Timber.v("Creating Table: %s", TABLE_NAME);
        favoritesDB.execSQL(TABLE_NAME);
        Timber.v("Leaving onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase favoritesDB, int i, int i1) {
        favoritesDB.execSQL("DROP TABLE IF EXISTS " + Favorites.TABLE_NAME);
        onCreate(favoritesDB);
    }
}
