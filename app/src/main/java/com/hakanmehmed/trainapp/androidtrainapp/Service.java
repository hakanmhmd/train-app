package com.hakanmehmed.trainapp.androidtrainapp;

import java.util.List;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

public class Service {
    private String serviceUid;
    private String serviceOperator;
    private String transportMode;
    private List<String> serviceOrigins = null;
    private List<String> serviceDestinations = null;
    private List<Stop> stops = null;

    public Service(String serviceUid, String serviceOperator, String transportMode, List<String> serviceOrigins, List<String> serviceDestinations, List<Stop> stops) {
        this.serviceUid = serviceUid;
        this.serviceOperator = serviceOperator;
        this.transportMode = transportMode;
        this.serviceOrigins = serviceOrigins;
        this.serviceDestinations = serviceDestinations;
        this.stops = stops;
    }

    public String getServiceUid() {
        return serviceUid;
    }

    public void setServiceUid(String serviceUid) {
        this.serviceUid = serviceUid;
    }

    public String getServiceOperator() {
        return serviceOperator;
    }

    public void setServiceOperator(String serviceOperator) {
        this.serviceOperator = serviceOperator;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public List<String> getServiceOrigins() {
        return serviceOrigins;
    }

    public void setServiceOrigins(List<String> serviceOrigins) {
        this.serviceOrigins = serviceOrigins;
    }

    public List<String> getServiceDestinations() {
        return serviceDestinations;
    }

    public void setServiceDestinations(List<String> serviceDestinations) {
        this.serviceDestinations = serviceDestinations;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceUid='" + serviceUid + '\'' +
                ", serviceOperator='" + serviceOperator + '\'' +
                ", transportMode='" + transportMode + '\'' +
                ", serviceOrigins=" + serviceOrigins +
                ", serviceDestinations=" + serviceDestinations +
                ", stops=" + stops +
                '}';
    }
}
