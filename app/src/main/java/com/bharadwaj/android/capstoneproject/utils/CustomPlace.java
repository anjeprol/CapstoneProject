package com.bharadwaj.android.capstoneproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomPlace implements Parcelable {

    private final String placeId;
    private final String placeName;
    private final String placeRating;
    private final String placePriceLevel;
    private final String placeAddress;
    private final String placePhoneNumber;
    private final String placeWebsiteUri;
    private final String placeLatLong;

    public CustomPlace(String placeId, String placeName, String placeRating, String placePriceLevel,
                       String placeAddress, String placePhoneNumber, String placeWebsiteUri, String placeLatLong) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeRating = placeRating;
        this.placePriceLevel = placePriceLevel;
        this.placeAddress = placeAddress;
        this.placePhoneNumber = placePhoneNumber;
        this.placeWebsiteUri = placeWebsiteUri;
        this.placeLatLong = placeLatLong;
    }

    protected CustomPlace(Parcel placeData) {
        this.placeId = placeData.readString();
        this.placeName = placeData.readString();
        this.placeRating = placeData.readString();
        this.placePriceLevel = placeData.readString();
        this.placeAddress = placeData.readString();
        this.placePhoneNumber = placeData.readString();
        this.placeWebsiteUri = placeData.readString();
        this.placeLatLong = placeData.readString();
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceRating() {
        return placeRating;
    }

    public String getPlacePriceLevel() {
        return placePriceLevel;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public String getPlacePhoneNumber() {
        return placePhoneNumber;
    }

    public String getPlaceWebsiteUri() {
        return placeWebsiteUri;
    }

    public String getPlaceLatLong() {
        return placeLatLong;
    }

    public static final Creator<CustomPlace> CREATOR = new Creator<CustomPlace>() {
        @Override
        public CustomPlace createFromParcel(Parcel in) {
            return new CustomPlace(in);
        }

        @Override
        public CustomPlace[] newArray(int size) {
            return new CustomPlace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeId);
        parcel.writeString(placeName);
        parcel.writeString(placeRating);
        parcel.writeString(placePriceLevel);
        parcel.writeString(placeAddress);
        parcel.writeString(placePhoneNumber);
        parcel.writeString(placeWebsiteUri);
        parcel.writeString(placeLatLong);
    }
}
