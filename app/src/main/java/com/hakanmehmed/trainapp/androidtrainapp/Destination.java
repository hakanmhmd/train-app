package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 14/02/2018.
 * <Destination>
 <StationCode>FPK</StationCode>
 <ScheduledTime>2018-02-15T14:10:00+00:00</ScheduledTime>
 <RealTime i:nil="true" />
 <RealTimeStatus>NotAvailable</RealTimeStatus>
 <Platform i:nil="true" />
 <PlatformStatus i:nil="true" />
 </Destination>
 */

class Destination extends StationType{

    public Destination(String stationCode, String scheduledTime, String realTime,
                       String realTimeStatus, String platform, String platformStatus) {
        super(stationCode, scheduledTime, realTime, realTimeStatus, platform, platformStatus);
    }
}
