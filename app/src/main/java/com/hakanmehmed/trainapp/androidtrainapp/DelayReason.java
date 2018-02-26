package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hakanmehmed on 20/02/2018.
 */

class DelayReason {
    @SerializedName("code")
    private int code;
    @SerializedName("reason")
    private String reason;

    public DelayReason(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DelayReason{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                '}';
    }
}
