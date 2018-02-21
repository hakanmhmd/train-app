package com.hakanmehmed.trainapp.androidtrainapp;

import android.support.v4.util.Pair;

/**
 * Created by hakanmehmed on 21/02/2018.
 */

class StopInfo {
    private String station;
    private String arrivalTime;
    private String platform;
    private String status;
    private String service;
    private Pair<String, Integer> pair;

    public StopInfo(String station, String arrivalTime, String platform, String status, String service, Pair<String, Integer> pair) {
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.platform = platform;
        this.status = status;
        this.service = service;
        this.pair = pair;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Pair<String, Integer> getPair() {
        return pair;
    }

    public void setPair(Pair<String, Integer> pair) {
        this.pair = pair;
    }

    @Override
    public String toString() {
        return "StopInfo{" +
                "station='" + station + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", platform='" + platform + '\'' +
                ", status='" + status + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
