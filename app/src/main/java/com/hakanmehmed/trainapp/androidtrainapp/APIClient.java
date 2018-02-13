package com.hakanmehmed.trainapp.androidtrainapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Retofit interface client
 */

public interface APIClient {
    @Headers({"X-Api-Version: 2.0", "X-Platform-Type: Android", "X-Consumer-Version: 930"})
    @POST("mobile/journeys")
    Call<JourneySearchResponse> getJourneys(@Body ApiQuery apiQuery);

}
