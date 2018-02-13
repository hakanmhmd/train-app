package com.hakanmehmed.trainapp.androidtrainapp;

import java.util.List;

/**
 * Created by hakanmehmed on 13/02/2018.
 */

class Journey {
    private Integer id;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private List<Leg> legs = null;
    private String journeyStatus;
}
