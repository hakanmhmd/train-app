package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class RealTime {
    private RealTimeServiceInfo realTimeServiceInfo;

    public RealTime(RealTimeServiceInfo realTimeServiceInfo) {
        this.realTimeServiceInfo = realTimeServiceInfo;
    }

    public RealTimeServiceInfo getRealTimeServiceInfo() {
        return realTimeServiceInfo;
    }

    public void setRealTimeServiceInfo(RealTimeServiceInfo realTimeServiceInfo) {
        this.realTimeServiceInfo = realTimeServiceInfo;
    }

    @Override
    public String toString() {
        return "RealTime{" +
                "realTimeServiceInfo=" + realTimeServiceInfo +
                '}';
    }
}
