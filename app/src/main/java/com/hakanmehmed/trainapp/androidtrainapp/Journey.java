package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class Journey {
    @SerializedName("id") private Integer id;
    @SerializedName("origin") private String origin;
    @SerializedName("destination") private String destination;
    @SerializedName("departureDateTime") private String departureDateTime;
    @SerializedName("arrivalDateTime") private String arrivalDateTime;
    @SerializedName("legs") private List<JouneryLeg> legs;
    @SerializedName("journeyStatus") private String journeyStatus;

    // used for subscriptions
    private Integer reminder;

    public Journey(Integer id, String origin, String destination, String departureDateTime, String arrivalDateTime, List<JouneryLeg> legs, String journeyStatus) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.legs = legs;
        this.journeyStatus = journeyStatus;
        reminder = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public List<JouneryLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<JouneryLeg> legs) {
        this.legs = legs;
    }

    public String getJourneyStatus() {
        return journeyStatus;
    }

    public void setJourneyStatus(String journeyStatus) {
        this.journeyStatus = journeyStatus;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDateTime='" + departureDateTime + '\'' +
                ", arrivalDateTime='" + arrivalDateTime + '\'' +
                ", legs=" + legs +
                ", journeyStatus='" + journeyStatus + '\'' +
                '}';
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }
}
