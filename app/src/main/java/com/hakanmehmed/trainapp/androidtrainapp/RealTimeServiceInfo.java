package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class RealTimeServiceInfo {
    private Boolean hasArrived;
    private Boolean hasDeparted;
    private String realTime;
    private String realTimePlatform;
    private String realTimeFlag;

    public RealTimeServiceInfo(Boolean hasArrived, Boolean hasDeparted, String realTime, String realTimePlatform, String realTimeFlag) {
        this.hasArrived = hasArrived;
        this.hasDeparted = hasDeparted;
        this.realTime = realTime;
        this.realTimePlatform = realTimePlatform;
        this.realTimeFlag = realTimeFlag;
    }

    public Boolean getHasArrived() {
        return hasArrived;
    }

    public void setHasArrived(Boolean hasArrived) {
        this.hasArrived = hasArrived;
    }

    public Boolean getHasDeparted() {
        return hasDeparted;
    }

    public void setHasDeparted(Boolean hasDeparted) {
        this.hasDeparted = hasDeparted;
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
                "hasArrived=" + hasArrived +
                ", hasDeparted=" + hasDeparted +
                ", realTime='" + realTime + '\'' +
                ", realTimePlatform='" + realTimePlatform + '\'' +
                ", realTimeFlag='" + realTimeFlag + '\'' +
                '}';
    }
}
