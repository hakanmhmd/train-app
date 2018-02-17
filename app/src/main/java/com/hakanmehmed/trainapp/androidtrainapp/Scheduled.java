package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Scheduled {
    private String scheduledTime;

    public Scheduled(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return "Scheduled{" +
                "scheduledTime='" + scheduledTime + '\'' +
                '}';
    }
}
