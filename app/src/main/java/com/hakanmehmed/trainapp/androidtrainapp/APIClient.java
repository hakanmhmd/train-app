package com.hakanmehmed.trainapp.androidtrainapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Retrofit interface client
 */

public interface APIClient {
    @Headers({"X-Api-Version: 2.9", "X-Platform-Type: Android", "X-Consumer-Version: 1000"})
    @POST("mobile/journeys")
    Call<JourneySearchResponse> getJourneys(@Body ApiQuery apiQuery);

    @Headers({"X-Api-Version: 1.0.0", "X-Platform-Type: Android", "X-Consumer-Version: 1000"})
    @GET("callingpattern/{trainId}/{date}")
    Call<LiveDataSearchResponse> getLiveData(@Path("trainId") String trainId, @Path("date") String date);
}
