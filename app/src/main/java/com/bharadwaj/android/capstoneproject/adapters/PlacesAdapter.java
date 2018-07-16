package com.bharadwaj.android.capstoneproject.adapters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract;
import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;
import com.bharadwaj.android.capstoneproject.utils.CustomPlace;
import com.bharadwaj.android.capstoneproject.utils.ExtractionUtils;
import com.bharadwaj.android.capstoneproject.widget.UpdatePlacesWidgetService;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private Cursor mCursor;
    private List<CustomPlace> mPlaces;
    private boolean isContextFavorites = false;
    public static final int FAVORITES_LOADER_ID = 51;

    private final Context mContext;

    public PlacesAdapter(Context context) {
        mContext = context;
        if(mContext.getClass().equals(FavoritesActivity.class)){
            isContextFavorites = true;
            Timber.v("PlaceAdapter tying to Favorites Context");
        }

    }

    public boolean isFavoriteContext(){
        return isContextFavorites;
    }

    public void fillPlacesData(List<CustomPlace> places) {
        Timber.v("Filling CustomPlace into Adapter...");
        this.mPlaces = places;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        this.mCursor = cursor;
        Timber.v("Setting cursor");
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PlaceViewHolder placeViewHolder, int position) {

        Timber.v("Binding PlaceViewHolder for position : %s", position);

        final String placeId;
        final String placeName;
        final String placeRating;
        final String placePriceLevel;
        final String placeAddress;
        final String placePhoneNumber;
        final String placeWebsiteUri;
        final String placeLatLong;

        CustomPlace currentPlace;

        if(isContextFavorites){
            mCursor.moveToPosition(position);
            placeId = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_ID));
            placeName = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_NAME));
            placeRating = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_RATING));
            placePriceLevel = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_PRICE_LEVEL));
            placeAddress = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_ADDRESS));
            placePhoneNumber = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_PHONE_NUMBER));
            placeWebsiteUri = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_WEBSITE_URI));
            placeLatLong = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_LATLONG));

        }else{
            currentPlace = mPlaces.get(position);
            placeId = currentPlace.getPlaceId();
            placeName = String.valueOf(currentPlace.getPlaceName());
            placeRating = String.valueOf(currentPlace.getPlaceRating());
            placePriceLevel = String.valueOf(currentPlace.getPlacePriceLevel());
            placeAddress = String.valueOf(currentPlace.getPlaceAddress());
            placePhoneNumber = String.valueOf(currentPlace.getPlacePhoneNumber());
            placeWebsiteUri = String.valueOf(currentPlace.getPlaceWebsiteUri());
            placeLatLong = currentPlace.getPlaceLatLong();
        }

        placeViewHolder.placeId = placeId;
        placeViewHolder.placeNameView.setText(placeName);
        placeViewHolder.placeRatingView.setText(placeRating);
        placeViewHolder.placeRatingBar.setRating(Float.valueOf(placeRating));
        placeViewHolder.placePriceLevelView.setText(setDollarsByPriceLevel(placePriceLevel));
        placeViewHolder.placeAddressView.setText(placeAddress);

        placeViewHolder.placeCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("Call button clicked. ");
                if (TextUtils.isEmpty(placePhoneNumber)) {
                    Toast.makeText(mContext, mContext.getString(R.string.phone_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Uri phoneCallUri = Uri.parse(Constants.CALL_INTENT_DATA + placePhoneNumber);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneCallUri);
                    if (isIntentAvailable(callIntent)) {
                        Timber.v("Opening call intent to call : %s", placePhoneNumber);
                        mContext.startActivity(callIntent);
                    }
                }
            }
        });

        placeViewHolder.placeMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("Maps button clicked. ");
                Uri googleMapsEndPointUri = Uri.parse(Constants.MAPS_END_POINT + placeLatLong);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMapsEndPointUri);
                if (isIntentAvailable(mapIntent)) {
                    Timber.v("Opening Maps intent for coordinates : %s", googleMapsEndPointUri.getAuthority());
                    mContext.startActivity(mapIntent);
                }
            }
        });

        placeViewHolder.placeWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("Website button clicked. ");
                if (TextUtils.isEmpty(placeWebsiteUri) || placeWebsiteUri.trim().equalsIgnoreCase(Constants.NULL)) {
                    Toast.makeText(mContext, mContext.getString(R.string.website_empty), Toast.LENGTH_SHORT).show();
                }else {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(placeWebsiteUri));
                    if (isIntentAvailable(websiteIntent)) {
                        Timber.v("Opening Website intent to Uri : %s", placeWebsiteUri);
                        mContext.startActivity(websiteIntent);
                    }
                }
            }
        });

        placeViewHolder.placeShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.v("Share button clicked. ");
                if (TextUtils.isEmpty(placeWebsiteUri) || placeWebsiteUri.trim().equalsIgnoreCase(Constants.NULL)) {
                    Toast.makeText(mContext, mContext.getString(R.string.no_website_to_share), Toast.LENGTH_SHORT).show();
                }else{
                    Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SMS_INTENT_DATA));
                    shareIntent.putExtra(Constants.SMS_BODY_HEADER, Constants.SMS_BODY + placeWebsiteUri);
                    if (isIntentAvailable(shareIntent)) {
                        Timber.v("Opening message intent");
                        mContext.startActivity(shareIntent);
                    }
                }
            }
        });

        if(isContextFavorites){
            placeViewHolder.placeFavoriteButton.setVisibility(View.GONE);
        }else{
            if(checkIfPlaceIsFavorite(placeId)){
                placeViewHolder.placeFavoriteButton.setImageResource(R.drawable.ic_favorite_filled);
            }else{
                placeViewHolder.placeFavoriteButton.setImageResource(R.drawable.ic_favorite_border);
            }
        }
        placeViewHolder.placeFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkIfPlaceIsFavorite(placeId)){
                    deletePlaceFromDB(placeId);
                    placeViewHolder.placeFavoriteButton.setImageResource(R.drawable.ic_favorite_border);
                }else {
                    insertPlaceIntoDB(placeId, placeName, placeRating, placePriceLevel, placeAddress,
                            placePhoneNumber, placeWebsiteUri, placeLatLong);
                    placeViewHolder.placeFavoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                }
                UpdatePlacesWidgetService.startFavoritePlacesIntentService(mContext,
                        ExtractionUtils.getPlacesNamesListFromCursor(mContext));
            }
        });

    }

    @Override
    public int getItemCount() {

        if(isContextFavorites){
           if (mCursor == null) return 0;
           else return mCursor.getCount();
        }else {
            if (mPlaces == null) return 0;
            return mPlaces.size();
        }

    }



    void insertPlaceIntoDB(String placeId,
                           String placeName,
                           String placeRating,
                           String placePriceLevel,
                           String placeAddress,
                           String phoneNumber,
                           String placeWebsiteUri,
                           String placeLatLong) {

        Timber.v("Inserting Place : %s", placeName);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Favorites.COLUMN_PLACE_ID, placeId);
        contentValues.put(Favorites.COLUMN_PLACE_NAME, placeName);
        contentValues.put(Favorites.COLUMN_PLACE_RATING, placeRating);
        contentValues.put(Favorites.COLUMN_PLACE_PRICE_LEVEL, placePriceLevel);
        contentValues.put(Favorites.COLUMN_ADDRESS, placeAddress);
        contentValues.put(Favorites.COLUMN_PLACE_PHONE_NUMBER, phoneNumber);
        contentValues.put(Favorites.COLUMN_PLACE_WEBSITE_URI, placeWebsiteUri);
        contentValues.put(Favorites.COLUMN_PLACE_LATLONG, placeLatLong);

        Uri insertedRowUri = mContext.getContentResolver().insert(FavoriteContract.Favorites.PLACES_CONTENT_URI, contentValues);
        Timber.v("Inserted Row Uri is : %s", insertedRowUri.getPath());
        SendBroadcastToupdateAppWidgets();
    }

    void deletePlaceFromDB(String placeID) {
        String selection = Favorites.COLUMN_PLACE_ID + "= ?";
        String[] selectionArgs = {placeID};

        int rowsDeleted = mContext.getContentResolver().delete(Favorites.PLACES_CONTENT_URI,
                selection,
                selectionArgs);
        if (rowsDeleted != 0) {
            Timber.v("Rows deleted : %s", rowsDeleted);
        }
        SendBroadcastToupdateAppWidgets();
    }

    private boolean checkIfPlaceIsFavorite(String placeID) {

        String selection = Favorites.COLUMN_PLACE_ID + "= ?";
        String[] selectionArgs = {placeID};

        Cursor cursor = mContext.getContentResolver().query(Favorites.PLACES_CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void SendBroadcastToupdateAppWidgets(){
        Intent newIntent = new Intent(Constants.UPDATE_ACTION);
        newIntent.putExtra(Constants.PLACES_NAMES_LIST, Parcels.wrap(ExtractionUtils.getPlacesNamesListFromCursor(mContext)));
        mContext.sendBroadcast(newIntent);
    }

    private String setDollarsByPriceLevel(String priceLevel) {

        float priceLevelValue = Float.valueOf(priceLevel);

        switch ((int) priceLevelValue){
            case 0:
                return "$";
            case -1:
            case 1:
                return "$";
            case 2:
                return "$$";
            case 3:
                return "$$$";
            case 4:
                return "$$$$";
            default:
                return "";
        }
    }

    public boolean isIntentAvailable(Intent intent) {
        final PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_name)
        TextView placeNameView;
        @BindView(R.id.place_rating)
        TextView placeRatingView;
        @BindView(R.id.rating_bar)
        RatingBar placeRatingBar;
        @BindView(R.id.place_price_level)
        TextView placePriceLevelView;
        @BindView(R.id.place_address)
        TextView placeAddressView;

        @BindView(R.id.place_call_button)
        ImageButton placeCallButton;
        @BindView(R.id.place_maps_button)
        ImageButton placeMapsButton;
        @BindView(R.id.place_website_button)
        ImageButton placeWebsiteButton;
        @BindView(R.id.place_share_button)
        ImageButton placeShareButton;
        @BindView(R.id.place_favorite)
        ImageButton placeFavoriteButton;


        final View mView;
        public String placeId;

        PlaceViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);

        }
    }
}
