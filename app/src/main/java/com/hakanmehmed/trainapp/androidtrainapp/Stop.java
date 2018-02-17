package com.hakanmehmed.trainapp.androidtrainapp;


/**
 * Created by hakanmehmed on 17/02/2018.
 */

class Stop {
    private Location location;
    private Arrival arrival;
    private Departure departure;
    private String callingType;

    public Stop(Location location, Arrival arrival, Departure departure, String callingType) {
        this.location = location;
        this.arrival = arrival;
        this.departure = departure;
        this.callingType = callingType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public String getCallingType() {
        return callingType;
    }

    public void setCallingType(String callingType) {
        this.callingType = callingType;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "location=" + location +
                ", arrival=" + arrival +
                ", departure=" + departure +
                ", callingType='" + callingType + '\'' +
                '}';
    }
}
