package com.bharadwaj.android.capstoneproject.adapters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bharadwaj.android.capstoneproject.PlaceFragment;
import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract;
import com.bharadwaj.android.capstoneproject.favorites.FavoriteContract.Favorites;
import com.bharadwaj.android.capstoneproject.favorites.FavoritesActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private Cursor mCursor;
    private List<Place> mPlaces;
    private boolean isContextFavorites = false;
    public static final int FAVORITES_LOADER_ID = 51;

    private final Context mContext;
    private final PlaceFragment.OnCallButtonInteractionListener mCallButtonListener;
    private final PlaceFragment.OnMapsButtonInteractionListener mMapsButtonListener;
    private final PlaceFragment.OnWebsiteButtonInteractionListener mWebsiteButtonListener;
    private final PlaceFragment.OnShareButtonInteractionListener mShareButtonListener;
    private final PlaceFragment.OnFavoriteButtonInteractionListener mFavoriteButtonListener;

    public PlacesAdapter(Context context,
                         PlaceFragment.OnCallButtonInteractionListener callListener,
                         PlaceFragment.OnMapsButtonInteractionListener mapsListener,
                         PlaceFragment.OnWebsiteButtonInteractionListener websiteListener,
                         PlaceFragment.OnShareButtonInteractionListener shareListener,
                         PlaceFragment.OnFavoriteButtonInteractionListener favoriteListener) {
        mContext = context;
        mCallButtonListener = callListener;
        mMapsButtonListener = mapsListener;
        mWebsiteButtonListener = websiteListener;
        mShareButtonListener = shareListener;
        mFavoriteButtonListener = favoriteListener;


        if(mContext.getClass().equals(FavoritesActivity.class)){
            isContextFavorites = true;
            Timber.v("PlaceAdapter tying to Favorites Context");
        }

    }

    public boolean isFavoriteContext(){
        return isContextFavorites;
    }

    public void fillPlacesData(List<Place> places) {
        Timber.v("Filling Places into Adapter...");
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

        final Place currentPlace;

        if(isContextFavorites){
            currentPlace = null;
            mCursor.moveToPosition(position);
            placeId = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_ID));
            placeName = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_NAME));
            placeRating = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_RATING));
            placePriceLevel = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_PLACE_PRICE_LEVEL));
            placeAddress = mCursor.getString(mCursor.getColumnIndex(Favorites.COLUMN_ADDRESS));

        }else{
            currentPlace = mPlaces.get(position);
            placeId = currentPlace.getId();
            placeName = String.valueOf(currentPlace.getName());
            placeRating = String.valueOf(currentPlace.getRating());
            placePriceLevel = String.valueOf(currentPlace.getPriceLevel());
            placeAddress = String.valueOf(currentPlace.getAddress());
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
                if (null != mCallButtonListener) {
                    mCallButtonListener.onCallButtonInteraction(String.valueOf(currentPlace.getPhoneNumber()));
                } else {
                    Timber.e("Call Button Listener is NULL");
                }
            }
        });
        placeViewHolder.placeMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mMapsButtonListener) {
                    mMapsButtonListener.onMapsButtonInteraction(getFormattedLocationString(currentPlace.getLatLng()));
                } else {
                    Timber.e("Maps Button Listener is NULL");
                }
            }
        });
        placeViewHolder.placeWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mWebsiteButtonListener) {
                    Timber.e("Website : " + currentPlace.getWebsiteUri());
                    mWebsiteButtonListener.onWebsiteButtonInteraction(currentPlace.getWebsiteUri());
                } else {
                    Timber.e("Website Button Listener is NULL");
                }
            }
        });
        placeViewHolder.placeShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mShareButtonListener) {
                    Timber.e("Website : " + currentPlace.getWebsiteUri());
                    mShareButtonListener.onShareButtonInteraction(currentPlace.getWebsiteUri());
                } else {
                    Timber.e("Share Button Listener is NULL");
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
                    insertPlaceIntoDB(placeId, placeName, placeRating, placePriceLevel, placeAddress);
                    placeViewHolder.placeFavoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                }

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



    void insertPlaceIntoDB(String placeId, String placeName, String placeRating, String placePriceLevel, String placeAddress) {

        Timber.v("Inserting Place : %s", placeName);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Favorites.COLUMN_PLACE_ID, placeId);
        contentValues.put(Favorites.COLUMN_PLACE_NAME, placeName);
        contentValues.put(Favorites.COLUMN_PLACE_RATING, placeRating);
        contentValues.put(Favorites.COLUMN_PLACE_PRICE_LEVEL, placePriceLevel);
        contentValues.put(Favorites.COLUMN_ADDRESS, placeAddress);

        Uri insertedRowUri = mContext.getContentResolver().insert(FavoriteContract.Favorites.PLACES_CONTENT_URI, contentValues);
        Timber.v("Inserted Row Uri is : %s", insertedRowUri.getPath());
    }

    void deletePlaceFromDB(String placeID) {
        String selection = Favorites.COLUMN_PLACE_ID + "= ?";
        String[] selectionArgs = {placeID};

        int rowsDeleted = mContext.getContentResolver().delete(Favorites.PLACES_CONTENT_URI,
                selection,
                selectionArgs);
        if (rowsDeleted != 0) {
            Timber.v("Rows deleted : %s", rowsDeleted);
            Timber.v("This movie removed as a favorite ");
        } else {
            Timber.v("This movie is not in th favorites DB ");
        }
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
            //implement
            Timber.v("This movie isn't a favorite ");
            return false;
        } else {
            Timber.v("This movie is a favorite one ");
            return true;
        }
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

    private String getFormattedLocationString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
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
