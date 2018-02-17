package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class LiveDataSearchResponse {
    private String requestId;
    private Boolean isRealTimeDataAvailable;
    private Service service;

    public LiveDataSearchResponse(String requestId, Boolean isRealTimeDataAvailable, Service service) {
        this.requestId = requestId;
        this.isRealTimeDataAvailable = isRealTimeDataAvailable;
        this.service = service;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean getRealTimeDataAvailable() {
        return isRealTimeDataAvailable;
    }

    public void setRealTimeDataAvailable(Boolean realTimeDataAvailable) {
        isRealTimeDataAvailable = realTimeDataAvailable;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "LiveDataSearchResponse{" +
                "requestId='" + requestId + '\'' +
                ", isRealTimeDataAvailable=" + isRealTimeDataAvailable +
                ", service=" + service +
                '}';
    }
}
