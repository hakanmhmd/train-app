package com.hakanmehmed.trainapp.androidtrainapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.util.Log;
import android.view.View;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Finds journeys
 */

public class TrainFinderAPI {
    private static final String TAG = "TrainFinderAPI";
    private static final String API_BASE_URL = "https://api.thetrainline.com";

    public TrainFinderAPI() {
    }

    private final HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private final OkHttpClient okHTTP =
            new OkHttpClient.Builder().addInterceptor(logging).build();

    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHTTP);

    private final Retrofit retrofit = builder.build();
    private final APIClient apiClient = retrofit.create(APIClient.class);

    void getTrains(ApiQuery apiQuery, final CustomCallback<JourneySearchResponse> callback){
        Call<JourneySearchResponse> call = apiClient.getJourneys(apiQuery);

        call.enqueue(new Callback<JourneySearchResponse>() {
            @Override
            public void onResponse(Call<JourneySearchResponse> call, Response<JourneySearchResponse> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<JourneySearchResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    static ApiQuery buildApiQuery(String from, String to){
        String fromCode = StationUtils.getCode(from);
        String toCode = StationUtils.getCode(to);
        int adults = 1;
        int children = 0;
        String journeyType = "Single";
        boolean showCancelled = true;
        OutboundJourney outboundJourney = new OutboundJourney(Utils.getCurrentTime(), "LeaveAfter");

        ApiQuery q = new ApiQuery(adults, children, fromCode, toCode, journeyType, showCancelled);
        q.setOutboundJourney(outboundJourney);

        return q;
    }


}
