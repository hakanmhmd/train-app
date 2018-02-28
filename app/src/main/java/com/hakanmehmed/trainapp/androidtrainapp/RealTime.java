package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class RealTime {
    private RealTimeServiceInfo realTimeServiceInfo;
    private DelayReason delayReason;
    private Cancelled cancelled;

    public RealTime(RealTimeServiceInfo realTimeServiceInfo, DelayReason delayReason, Cancelled cancelled) {
        this.realTimeServiceInfo = realTimeServiceInfo;
        this.delayReason = delayReason;
        this.cancelled = cancelled;
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

    public Cancelled getCancelled() {
        return cancelled;
    }

    public void setCancelled(Cancelled cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return "RealTime{" +
                "realTimeServiceInfo=" + realTimeServiceInfo +
                ", delayReason=" + delayReason +
                '}';
    }


}
