package com.hakanmehmed.trainapp.androidtrainapp;

import java.util.List;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class JourneySearchResponse {
    private String journeySearchId;
    private List<Journey> journeys = null;

    public JourneySearchResponse(String journeySearchId, List<Journey> journeys) {
        this.journeySearchId = journeySearchId;
        this.journeys = journeys;
    }

    public String getJourneySearchId() {
        return journeySearchId;
    }

    public void setJourneySearchId(String journeySearchId) {
        this.journeySearchId = journeySearchId;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
    }
}
