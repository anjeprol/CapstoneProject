package com.bharadwaj.android.capstoneproject.favorites;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import timber.log.Timber;

import static com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites.TABLE_NAME;

public class FavoritePlaceContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteDBHelper mFavoriteDBHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.FAVORITES, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.FAVORITE, FAVORITES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Timber.v("Entering onCreate");
        Context context = getContext();
        mFavoriteDBHelper = new FavoriteDBHelper(context);
        Timber.v("Leaving onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase favoritesDB = mFavoriteDBHelper.getReadableDatabase();
        Timber.v("Getting Readable DB instance");
        Timber.v("DB open status : %s", favoritesDB.isOpen());
        Timber.v("-------------------------------------------------------");
        Timber.v("Projection : %s", projection);
        Timber.v("Selection : %s", selection);
        Timber.v("Selection arguments : %s", selectionArgs);
        Timber.v("Sort Order : %s", sortOrder);
        Timber.v("-------------------------------------------------------");

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case FAVORITES:
                cursor =  favoritesDB.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase favoritesDB = mFavoriteDBHelper.getWritableDatabase();
        Uri returningUri;

        int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITES:
                long insertedRowID = favoritesDB.insert(TABLE_NAME, null, contentValues);
                if (insertedRowID > 0){
                    returningUri = ContentUris.withAppendedId(FavoriteContract.Favorites.PLACES_CONTENT_URI, insertedRowID);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returningUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase favoritesDB = mFavoriteDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        int deletedRowCount;
        switch (match){
            case FAVORITES:
                deletedRowCount = favoritesDB.delete(TABLE_NAME, selection, selectionArgs);
                if (deletedRowCount != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }else {
                    throw new android.database.SQLException("Failed to delete row from " + uri);
                }
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = "_id = ?";
                selectionArgs = new String[]{id};

                deletedRowCount = favoritesDB.delete(TABLE_NAME, selection, selectionArgs);
                if (deletedRowCount != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }else {
                    throw new android.database.SQLException("Failed to delete row from " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deletedRowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 1;
    }
}
