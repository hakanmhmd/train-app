package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class Journey {
    @SerializedName("id")
    private Integer id;
    @SerializedName("origin")
    private String origin;
    @SerializedName("destination")
    private String destination;
    @SerializedName("departureDateTime")
    private String departureDateTime;
    @SerializedName("arrivalDateTime")
    private String arrivalDateTime;
    @SerializedName("legs")
    private List<JourneyLeg> legs;
    @SerializedName("journeyStatus")
    private String journeyStatus;

    // used for subscriptions
    private Integer reminder;
    private int notificationId;

    public Journey(Integer id, String origin, String destination, String departureDateTime, String arrivalDateTime, List<JourneyLeg> legs, String journeyStatus) {
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

    public List<JourneyLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<JourneyLeg> legs) {
        this.legs = legs;
    }

    public String getJourneyStatus() {
        return journeyStatus;
    }

    public void setJourneyStatus(String journeyStatus) {
        this.journeyStatus = journeyStatus;
    }

    public void setReminder(Integer reminder) {
        this.reminder = reminder;
    }

    public Integer getReminder() {
        return reminder;
    }

    void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    @Override
    public boolean equals(Object obj) {
        Journey other = null;
        if(obj instanceof Journey){
            other = (Journey) obj;
        }
        if(!this.getDestination().equals(other.getDestination())) return false;
        if(!this.getOrigin().equals(other.getOrigin())) return false;
        if(this.getLegs().size() !=  other.getLegs().size()) return false;
        if(!this.getLegs().get(0).getOrigin().getScheduledTime().equals(other.getLegs().get(0).getOrigin().getScheduledTime())) return false;
        if(!this.getLegs().get(0).getDestination().getScheduledTime().equals(other.getLegs().get(0).getDestination().getScheduledTime())) return false;

        /* sometimes the platform may be null, so let's check if there is any platform given */
        for(int i = 0; i < this.getLegs().size(); i++){
            String originPlatform1 = this.getLegs().get(i).getOrigin().getPlatform();
            String originPlatform2 = other.getLegs().get(i).getOrigin().getPlatform();
            if(originPlatform1 != null && originPlatform2 != null){
                if(!originPlatform1.equals(originPlatform2)) return false;
            }

            String destinationPlatform1 = this.getLegs().get(i).getDestination().getPlatform();
            String destinationPlatform2 = other.getLegs().get(i).getDestination().getPlatform();
            if(destinationPlatform1 != null && destinationPlatform2 != null){
                if(!destinationPlatform1.equals(destinationPlatform2)) return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", notificationId=" + notificationId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDateTime='" + departureDateTime + '\'' +
                ", arrivalDateTime='" + arrivalDateTime + '\'' +
                ", legs=" + legs +
                ", journeyStatus='" + journeyStatus + '\'' +
                ", reminder=" + reminder +
                '}';
    }
}
