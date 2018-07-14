package com.bharadwaj.android.capstoneproject.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bharadwaj.android.capstoneproject.R;
import com.bharadwaj.android.capstoneproject.constants.Constants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.bharadwaj.android.capstoneproject.constants.Constants.PLACE_PICKER_REQUEST;


public class SettingsFragment extends PreferenceFragment {

    boolean mBindingPreferences = false;
    Set<String> locationDataSet = new HashSet<>();

    public SettingsFragment() {}

    private SharedPreferences.OnSharedPreferenceChangeListener mListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Timber.d("Entering onSharedPreferenceChanged");
                    if (key.equals(getString(R.string.pref_location_key))) {
                        Preference locationPreference = findPreference(key);
                        locationPreference.setSummary(getLocationData(Constants.LOCATION_NAME));
                    }

                    if (key.equals(getString(R.string.radius_seekbar_key))) {
                        Preference radiusPreference = findPreference(key);

                        Integer integer = Integer.valueOf(getString(R.string.radius_default));
                        Integer value = sharedPreferences.getInt(key, integer);
                        radiusPreference.setSummary(Constants.RADIUS_SUMMARY_TEXT + value + getString(R.string.radius_units));
                    }

                    Timber.d("Leaving onSharedPreferenceChanged");
                }
            };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Entering onCreateView");


        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

        Preference locationPreference = findPreference(getString(R.string.pref_location_key));
        locationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Timber.v("Timber : Starting Place Picker Intent...");
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        bindPreferencesSummaryToValue();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);

        Timber.d("Leaving onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("Entering onActivityResult");
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                Timber.v("Place Name : %s", place.getAddress());

                SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
                sharedPreferences.edit().putString(getString(R.string.pref_location_key), prepareLocationData(place)).apply();

                getLocationData(Constants.LOCATION_NAME);
                getLocationData(Constants.LOCATION_COORDINATES);
            }
        }
        Timber.d("Leaving onActivityResult");
    }

    private String getFormattedLocationString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    private void bindPreferencesSummaryToValue() {
        mBindingPreferences = true;
        Timber.v("Entering bindPreferencesSummaryToValue");


        // Update the location preferences
        // Update can also be done with a listener
        findPreference(getString(R.string.pref_location_key)).setSummary(getLocationData(Constants.LOCATION_NAME));


        String key = getString(R.string.radius_seekbar_key);
        Preference radiusPreference = findPreference(key);
        Integer integer = Integer.valueOf(getString(R.string.radius_default));
        Integer value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(key, integer);
        radiusPreference.setSummary(Constants.RADIUS_SUMMARY_TEXT + value + getString(R.string.radius_units));

        mBindingPreferences = false;
        Timber.v("Leaving bindPreferencesSummaryToValue");
    }

    public String prepareLocationData(Place currentPlace) {
        return String.valueOf(currentPlace.getAddress())
                + Constants.LOCATION_SPECIFICS_SAPERATOR
                + getFormattedLocationString(currentPlace.getLatLng());
    }

    public String getLocationData(String position){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preferredLocation = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String[] preferredLocationSpecifics = preferredLocation.split(Constants.LOCATION_SPECIFICS_SAPERATOR);
        if(position.equalsIgnoreCase(Constants.LOCATION_NAME)){
            return preferredLocationSpecifics[0];
        }
        if(position.equalsIgnoreCase(Constants.LOCATION_COORDINATES)){
            return preferredLocationSpecifics[1];
        }
        return getString(R.string.pref_location_default);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("Entering onDestroy");
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        Timber.d("Leaving onDestroy");
    }

}
