package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Arrival {
    private Boolean notApplicable;
    private Scheduled scheduled;
    private RealTime realTime;

    public Arrival(Boolean notApplicable, Scheduled scheduled, RealTime realTime) {
        this.notApplicable = notApplicable;
        this.scheduled = scheduled;
        this.realTime = realTime;
    }

    public Boolean getNotApplicable() {
        return notApplicable;
    }

    public void setNotApplicable(Boolean notApplicable) {
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

    @Override
    public String toString() {
        return "Arrival{" +
                "notApplicable=" + notApplicable +
                ", scheduled=" + scheduled +
                ", realTime=" + realTime +
                '}';
    }
}
