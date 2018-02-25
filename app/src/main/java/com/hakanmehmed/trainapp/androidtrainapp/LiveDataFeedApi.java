package com.hakanmehmed.trainapp.androidtrainapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

public class LiveDataFeedApi {
    private static final String TAG = "LiveDataFeedApi";
    private static final String API_BASE_URL = "https://realtime.thetrainline.com";
    private static final String BACKEND_BASE_URL = "http://10.0.2.2:5000";


    public LiveDataFeedApi() {
    }

    private final HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private final OkHttpClient okHTTP =
            new OkHttpClient.Builder().addInterceptor(logging).build();


    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHTTP).build();
    private final APIClient apiClient = retrofit.create(APIClient.class);

    private final Retrofit backendRetrofit = new Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final APIClient backendApiClient = backendRetrofit.create(APIClient.class);

    void getLiveData(String trainId, final CustomCallback<LiveDataSearchResponse> callback){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(Calendar.getInstance().getTime());

        Call<LiveDataSearchResponse> backendCall = backendApiClient.backendGetJourneyInfo(trainId, now);

        backendCall.enqueue(new Callback<LiveDataSearchResponse>() {
            @Override
            public void onResponse(Call<LiveDataSearchResponse> call, Response<LiveDataSearchResponse> response) {}

            @Override
            public void onFailure(Call<LiveDataSearchResponse> call, Throwable t) {}
        });

        Call<LiveDataSearchResponse> call = apiClient.getLiveData(trainId, now);
        call.enqueue(new Callback<LiveDataSearchResponse>() {
            @Override
            public void onResponse(Call<LiveDataSearchResponse> call, Response<LiveDataSearchResponse> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<LiveDataSearchResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
