package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class OutboundJourney {
    private String Time;
    private String Type;

    public OutboundJourney(String time, String type) {
        Time = time;
        Type = type;
    }

    public OutboundJourney() {

    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
