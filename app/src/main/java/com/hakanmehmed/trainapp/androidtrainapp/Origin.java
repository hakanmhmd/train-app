package com.hakanmehmed.trainapp.androidtrainapp;

/**
 * Created by hakanmehmed on 14/02/2018.
 * <Origin>
     <StationCode>EUS</StationCode>
     <ScheduledTime>2018-02-15T13:49:00+00:00</ScheduledTime>
     <RealTime i:nil="true" />
     <RealTimeStatus>NotAvailable</RealTimeStatus>
     <Platform i:nil="true" />
     <PlatformStatus i:nil="true" />
  </Origin>
 */

class Origin extends StationType{

    public Origin(String stationCode, String scheduledTime, String realTime,
                  String realTimeStatus, String platform, String platformStatus) {
        super(stationCode, scheduledTime, realTime, realTimeStatus, platform, platformStatus);
    }
}
