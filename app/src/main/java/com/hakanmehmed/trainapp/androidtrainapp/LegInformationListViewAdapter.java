package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by hakanmehmed on 20/02/2018.
 */

public class LegInformationListViewAdapter extends ArrayAdapter {

    private Activity activity;

    private String[] legTime;
    private String[] legOperatingCompany;
    private String[] legPlatform;
    private String[] legStatus;
    private String[] legStation;
    private String[] legCurrentStation;

    public LegInformationListViewAdapter(Activity activity, String[] legTime,
                                         String[] legOperatingCompany, String[] legPlatform,
                                         String[] legStatus, String[] legStation, String[] legCurrentStation){

        super(activity, R.layout.activity_journey_information_single_leg);

        this.activity = activity;
        this.legTime = legTime;
        this.legOperatingCompany = legOperatingCompany;
        this.legPlatform = legPlatform;
        this.legStatus = legStatus;
        this.legStation = legStation;
        this.legCurrentStation = legCurrentStation;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_journey_information_single_leg, null,true);

        TextView legDepartureTv = (TextView) rowView.findViewById(R.id.legDepartureTv);
        TextView legOperatingCompanyTv = (TextView) rowView.findViewById(R.id.legOperatingCompanyTv);
        TextView legDeparturePlatformTv = (TextView) rowView.findViewById(R.id.legDeparturePlatformTv);
        TextView legDepartureStatusTv = (TextView) rowView.findViewById(R.id.legDepartureStatusTv);
        TextView legDepartureStationTv = (TextView) rowView.findViewById(R.id.legDepartureStationTv);
        TextView legCurrentStationTv = (TextView) rowView.findViewById(R.id.legCurrentStationTv);

        android.util.Log.v("Adapterrr", "here");

        legDepartureTv.setText(legTime[position]);
        legDeparturePlatformTv.setText(legPlatform[position]);
        legDepartureStationTv.setText(legStation[position]);

        return rowView;
    }
}
