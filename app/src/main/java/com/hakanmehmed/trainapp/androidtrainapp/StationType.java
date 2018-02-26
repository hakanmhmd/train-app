package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hakanmehmed on 14/02/2018.
 */

class StationType {
    @SerializedName("stationCode")
    private String stationCode;
    @SerializedName("scheduledTime")
    private String scheduledTime;
    @SerializedName("realTime")
    private String realTime;
    @SerializedName("realTimeStatus")
    private String realTimeStatus;
    @SerializedName("platform")
    private String platform;
    @SerializedName("platformStatus")
    private String platformStatus;

    public StationType(String stationCode, String scheduledTime, String realTime, String realTimeStatus, String platform, String platformStatus) {
        this.stationCode = stationCode;
        this.scheduledTime = scheduledTime;
        this.realTime = realTime;
        this.realTimeStatus = realTimeStatus;
        this.platform = platform;
        this.platformStatus = platformStatus;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getRealTimeStatus() {
        return realTimeStatus;
    }

    public void setRealTimeStatus(String realTimeStatus) {
        this.realTimeStatus = realTimeStatus;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }
}
