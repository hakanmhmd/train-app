package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class RealTimeServiceInfo {
    private Boolean hasArrivedOrDeparted;
    private String realTime;
    private String realTimePlatform;
    private String realTimeFlag;

    public RealTimeServiceInfo(Boolean hasArrivedOrDeparted, String realTime, String realTimePlatform, String realTimeFlag) {
        this.hasArrivedOrDeparted = hasArrivedOrDeparted;
        this.realTime = realTime;
        this.realTimePlatform = realTimePlatform;
        this.realTimeFlag = realTimeFlag;
    }

    public Boolean getHasArrivedOrDeparted() {
        return hasArrivedOrDeparted;
    }

    public void setHasArrivedOrDeparted(Boolean hasArrivedOrDeparted) {
        this.hasArrivedOrDeparted = hasArrivedOrDeparted;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getRealTimePlatform() {
        return realTimePlatform;
    }

    public void setRealTimePlatform(String realTimePlatform) {
        this.realTimePlatform = realTimePlatform;
    }

    public String getRealTimeFlag() {
        return realTimeFlag;
    }

    public void setRealTimeFlag(String realTimeFlag) {
        this.realTimeFlag = realTimeFlag;
    }

    @Override
    public String toString() {
        return "RealTimeServiceInfo{" +
                "hasArrivedOrDeparted=" + hasArrivedOrDeparted +
                ", realTime='" + realTime + '\'' +
                ", realTimePlatform='" + realTimePlatform + '\'' +
                ", realTimeFlag='" + realTimeFlag + '\'' +
                '}';
    }
}
