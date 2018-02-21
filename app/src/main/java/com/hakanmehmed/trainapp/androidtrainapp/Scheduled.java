package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Scheduled {
    private String scheduledTime;
    private String scheduledPlatform;

    public Scheduled(String scheduledTime, String scheduledPlatform) {
        this.scheduledTime = scheduledTime;
        this.scheduledPlatform = scheduledPlatform;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getScheduledPlatform() {
        return scheduledPlatform;
    }

    public void setScheduledPlatform(String scheduledPlatform) {
        this.scheduledPlatform = scheduledPlatform;
    }

    @Override
    public String toString() {
        return "Scheduled{" +
                "scheduledTime='" + scheduledTime + '\'' +
                '}';
    }
}
