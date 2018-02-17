package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Location {
    private String crs;

    public Location(String crs) {
        this.crs = crs;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    @Override
    public String toString() {
        return "Location{" +
                "crs='" + crs + '\'' +
                '}';
    }
}
