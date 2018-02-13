package com.hakanmehmed.trainapp.androidtrainapp;


/**
 * Created by hakanmehmed on 13/02/2018.
 * Query class
 */

class ApiQuery {
    private Integer Adults;
    private Integer Children;
    private String Origin;
    private String Destination;
    private String JourneyType; // different types
    private OutboundJourney outboundJourney;
    private Boolean ShowCancelledTrains;
    //TODO: more field

    public ApiQuery() {
    }

    // TODO: Check which inputs can be dismissed here


    public ApiQuery(Integer adults, Integer children, String origin, String destination, String journeyType, Boolean showCancelledTrains) {
        Adults = adults;
        Children = children;
        Origin = origin;
        Destination = destination;
        JourneyType = journeyType;
        ShowCancelledTrains = showCancelledTrains;
    }

    public Integer getAdults() {
        return Adults;
    }

    public void setAdults(Integer adults) {
        Adults = adults;
    }

    public Integer getChildren() {
        return Children;
    }

    public void setChildren(Integer children) {
        Children = children;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getJourneyType() {
        return JourneyType;
    }

    public void setJourneyType(String journeyType) {
        JourneyType = journeyType;
    }

    public OutboundJourney getOutboundJourney() {
        return outboundJourney;
    }

    public void setOutboundJourney(OutboundJourney outboundJourney) {
        this.outboundJourney = outboundJourney;
    }

    public Boolean getShowCancelledTrains() {
        return ShowCancelledTrains;
    }

    public void setShowCancelledTrains(Boolean showCancelledTrains) {
        ShowCancelledTrains = showCancelledTrains;
    }
}
