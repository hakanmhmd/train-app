package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class JourneySearchResponse {
    @SerializedName("journeySearchId")
    private String journeySearchId;
    @SerializedName("journeys")
    private List<Journey> journeys;

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
