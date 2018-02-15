package com.hakanmehmed.trainapp.androidtrainapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hakanmehmed on 13/02/2018.
 * <JourneyLeg>
     <id>1</id>
     <Origin>
     <StationCode>EUS</StationCode>
     <ScheduledTime>2018-02-15T13:49:00+00:00</ScheduledTime>
     <RealTime i:nil="true" />
     <RealTimeStatus>NotAvailable</RealTimeStatus>
     <Platform i:nil="true" />
     <PlatformStatus i:nil="true" />
     </Origin>
     <Destination>
     <StationCode>FPK</StationCode>
     <ScheduledTime>2018-02-15T14:10:00+00:00</ScheduledTime>
     <RealTime i:nil="true" />
     <RealTimeStatus>NotAvailable</RealTimeStatus>
     <Platform i:nil="true" />
     <PlatformStatus i:nil="true" />
     </Destination>
     <transportMode>Tube</transportMode>
     <reservationFlag>N</reservationFlag>
     <RetailTrainIdentifier i:nil="true" />
     <trainId i:nil="true" />
     <serviceProviderCode></serviceProviderCode>
     <serviceProviderName></serviceProviderName>
     <seatingClass>Blank</seatingClass>
     <isCancelled>false</isCancelled>
     <FinalDestinations xmlns:d6p1="http://schemas.microsoft.com/2003/10/Serialization/Arrays" i:nil="true" />
     <BusyData>
     <Coaches />
     </BusyData>
 </JourneyLeg>
 */

class JourneyLeg {
    @SerializedName("id") private Integer id;
    @SerializedName("origin") private Origin origin;
    @SerializedName("destination") private Destination destination;
    @SerializedName("transportMode") private String transportMode;
    @SerializedName("reservationFlag") private String reservationFlag;
    @SerializedName("trainId") private String trainId;
    @SerializedName("serviceProviderCode") private String serviceProviderCode;
    @SerializedName("serviceProviderName") private String serviceProviderName;
    @SerializedName("seatingClass") private String seatingClass;
    @SerializedName("isCancelled") private Boolean isCancelled;

    public JourneyLeg(Integer id, Origin origin, Destination destination,
                      String transportMode, String reservationFlag, String trainId,
                      String serviceProviderCode, String serviceProviderName,
                      String seatingClass, Boolean isCancelled) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.transportMode = transportMode;
        this.reservationFlag = reservationFlag;
        this.trainId = trainId;
        this.serviceProviderCode = serviceProviderCode;
        this.serviceProviderName = serviceProviderName;
        this.seatingClass = seatingClass;
        this.isCancelled = isCancelled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getReservationFlag() {
        return reservationFlag;
    }

    public void setReservationFlag(String reservationFlag) {
        this.reservationFlag = reservationFlag;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getServiceProviderCode() {
        return serviceProviderCode;
    }

    public void setServiceProviderCode(String serviceProviderCode) {
        this.serviceProviderCode = serviceProviderCode;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getSeatingClass() {
        return seatingClass;
    }

    public void setSeatingClass(String seatingClass) {
        this.seatingClass = seatingClass;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
}
