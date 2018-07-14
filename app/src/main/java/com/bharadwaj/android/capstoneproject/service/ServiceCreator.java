package com.bharadwaj.android.capstoneproject.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Bharadwaj on 6/1/18.
 */

public class ServiceCreator {

    private static final OkHttpClient.Builder sOkHttpClient = new OkHttpClient.Builder();
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;

    //Preventing instantiation.
    private ServiceCreator() {
        Timber.plant(new Timber.DebugTree());
    }

    public static <C> C createService(Class<C> service, String baseUrl){
        Timber.d("Entering createService");
        sOkHttpClient.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(sOkHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Timber.d("Leaving createService");
        return retrofit.create(service);

    }

}
