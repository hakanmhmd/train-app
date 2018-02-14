package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class OutboundJourney {
    @SerializedName("time") private String time;
    @SerializedName("type") private String type;

    public OutboundJourney(String time, String type) {
        this.time = time;
        this.type = type;
    }

    public OutboundJourney() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
