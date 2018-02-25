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
 * Created by hakanmehmed on 13/02/2018.
 * Finds journeys
 */

public class JourneyFinderApi {
    private static final String TAG = "JourneyFinderApi";
    private static final String API_BASE_URL = "https://api.thetrainline.com";
    private static final String BACKEND_BASE_URL = "http://10.0.2.2:5000";

    public JourneyFinderApi() {
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

    void getJourneys(ApiQuery apiQuery, final CustomCallback<JourneySearchResponse> callback){
        Call<JourneySearchResponse> backendCall = backendApiClient.backendGetSchedule(apiQuery);
        backendCall.enqueue(new Callback<JourneySearchResponse>() {
            @Override
            public void onResponse(Call<JourneySearchResponse> call, Response<JourneySearchResponse> response) {}

            @Override
            public void onFailure(Call<JourneySearchResponse> call, Throwable t) {}
        });

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

    static ApiQuery buildApiQuery(String from, String to, String time){
        String fromCode = StationUtils.getCodeFromStationName(from);
        String toCode = StationUtils.getCodeFromStationName(to);
        int adults = 1;
        int children = 0;
        String journeyType = "Single";
        boolean showCancelled = true;
        OutboundJourney outboundJourney = new OutboundJourney(time, "LeaveAfter");

        ApiQuery q = new ApiQuery(adults, children, fromCode, toCode, journeyType, showCancelled);
        q.setOutboundJourney(outboundJourney);

        return q;
    }


}
