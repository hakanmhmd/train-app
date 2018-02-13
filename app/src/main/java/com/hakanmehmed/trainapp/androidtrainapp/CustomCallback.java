package com.hakanmehmed.trainapp.androidtrainapp;

import retrofit2.Response;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Callback interface for methods
 */

public interface CustomCallback<T> {
    void onSuccess(Response<T> response);
    void onFailure(Throwable throwable);
}
