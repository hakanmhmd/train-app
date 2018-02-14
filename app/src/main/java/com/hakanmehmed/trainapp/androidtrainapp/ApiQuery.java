package com.hakanmehmed.trainapp.androidtrainapp;


import com.google.gson.annotations.SerializedName;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Query class
 */

class ApiQuery {
    @SerializedName("adults") private Integer adults;
    @SerializedName("children") private Integer children;
    @SerializedName("origin") private String origin;
    @SerializedName("destination") private String destination;
    @SerializedName("journeyType") private String journeyType; // different types
    @SerializedName("outboundJourney") private OutboundJourney outboundJourney;
    @SerializedName("showCancelledTrains") private Boolean showCancelledTrains;
    //TODO: more fields

    public ApiQuery() {
    }

    // TODO: Check which inputs can be dismissed here


    public ApiQuery(Integer adults, Integer children, String origin, String destination, String journeyType, Boolean showCancelledTrains) {
        this.adults = adults;
        this.children = children;
        this.origin = origin;
        this.destination = destination;
        this.journeyType = journeyType;
        this.showCancelledTrains = showCancelledTrains;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
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

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public OutboundJourney getOutboundJourney() {
        return outboundJourney;
    }

    public void setOutboundJourney(OutboundJourney outboundJourney) {
        this.outboundJourney = outboundJourney;
    }

    public Boolean getShowCancelledTrains() {
        return showCancelledTrains;
    }

    public void setShowCancelledTrains(Boolean showCancelledTrains) {
        this.showCancelledTrains = showCancelledTrains;
    }
}
