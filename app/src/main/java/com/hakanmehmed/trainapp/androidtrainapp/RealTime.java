package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class RealTime {
    private RealTimeServiceInfo realTimeServiceInfo;
    private DelayReason delayReason;

    public RealTime(RealTimeServiceInfo realTimeServiceInfo, DelayReason delayReason) {
        this.realTimeServiceInfo = realTimeServiceInfo;
        this.delayReason = delayReason;
    }

    public RealTimeServiceInfo getRealTimeServiceInfo() {
        return realTimeServiceInfo;
    }

    public void setRealTimeServiceInfo(RealTimeServiceInfo realTimeServiceInfo) {
        this.realTimeServiceInfo = realTimeServiceInfo;
    }

    public DelayReason getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(DelayReason delayReason) {
        this.delayReason = delayReason;
    }

    @Override
    public String toString() {
        return "RealTime{" +
                "realTimeServiceInfo=" + realTimeServiceInfo +
                ", delayReason=" + delayReason +
                '}';
    }
}
