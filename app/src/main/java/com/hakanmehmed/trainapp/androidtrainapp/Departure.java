package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Departure {
    private Scheduled scheduled;
    private RealTime realTime;
    private Boolean notApplicable;

    public Departure(Scheduled scheduled, RealTime realTime, Boolean notApplicable) {
        this.scheduled = scheduled;
        this.realTime = realTime;
        this.notApplicable = notApplicable;
    }

    public Scheduled getScheduled() {
        return scheduled;
    }

    public void setScheduled(Scheduled scheduled) {
        this.scheduled = scheduled;
    }

    public RealTime getRealTime() {
        return realTime;
    }

    public void setRealTime(RealTime realTime) {
        this.realTime = realTime;
    }

    public Boolean getNotApplicable() {
        return notApplicable;
    }

    public void setNotApplicable(Boolean notApplicable) {
        this.notApplicable = notApplicable;
    }

    @Override
    public String toString() {
        return "Departure{" +
                "scheduled=" + scheduled +
                ", realTime=" + realTime +
                ", notApplicable=" + notApplicable +
                '}';
    }
}
