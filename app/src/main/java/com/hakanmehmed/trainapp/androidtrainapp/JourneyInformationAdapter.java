package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.*;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class JourneyInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Journey journey;
    private final HashMap<String, LiveDataSearchResponse> legInfo;
    private final Context context;
    private final JourneyInformationActivity activity;

    public JourneyInformationAdapter(Journey journey, HashMap<String,
            LiveDataSearchResponse> legInfo, Context context, JourneyInformationActivity activity) {
        this.journey = journey;
        this.legInfo = legInfo;
        this.context = context;
        this.activity = activity;
    }

    public class ChangeViewer extends RecyclerView.ViewHolder{
        @BindView(R.id.clock)
        ImageView clock;
        @BindView(R.id.changeDurationTv)
        TextView changeDurationTv;
        @BindView(R.id.changeStationTv)
        TextView changeStationTv;

        public ChangeViewer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDuration(String duration){
            changeDurationTv.setText(duration);
        }

        public void setStation(String station){
            changeStationTv.setText(context.getString(R.string.change_message,
                    StationUtils.getNameFromStationCode(station)));
        }


    }

    public class LegViewer extends RecyclerView.ViewHolder{
        @BindView(R.id.legDepartureTv)
        TextView legDepartureTv;
        @BindView(R.id.legArrivalStatus)
        TextView legArrivalStatus;
        @BindView(R.id.legOperatingCompanyTv)
        TextView legOperatingCompanyTv;
        @BindView(R.id.legDeparturePlatformTv)
        TextView legDeparturePlatformTv;
        @BindView(R.id.legDurationTv)
        TextView legDurationTv;
        @BindView(R.id.legArrivalTv)
        TextView legArrivalTv;
        @BindView(R.id.legDepartureStatusTv)
        TextView legDepartureStatusTv;
        @BindView(R.id.legDeparuteStationTv)
        TextView legDeparuteStationTv;
        @BindView(R.id.legArrivalStationTv)
        TextView legArrivalStationTv;
        @BindView(R.id.legCurrentStationTv)
        TextView legCurrentStationTv;
        @BindView(R.id.green_circle)
        ImageView green_circle;

        public LegViewer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            green_circle.setVisibility(GONE);
            legCurrentStationTv.setVisibility(GONE);
        }

        public void setArrival(String time, String station){
            legArrivalTv.setText(time);
            legArrivalStationTv.setText(StationUtils.getNameFromStationCode(station));
        }

        public void setDeparture(String time, String station, String platform){
            legDepartureTv.setText(time);
            legDeparuteStationTv.setText(StationUtils.getNameFromStationCode(station));

            if(platform != null && !platform.isEmpty()){
                legDeparturePlatformTv.setText(context.getString(R.string.platform, platform));
            } else {
                legDeparturePlatformTv.setVisibility(GONE);
            }
        }

        public void setOperatingCompany(String company){
            legOperatingCompanyTv.setText(company);
        }

        public void setCancelled(boolean cancelled){
            if(!cancelled) return;

            setArriveStatus("-");
            setDepartStatus("-");

            int colour = ContextCompat.getColor(context, R.color.colorDarkGray);
            legDepartureTv.setTextColor(colour);
            legDeparuteStationTv.setTextColor(colour);
            legCurrentStationTv.setTextColor(colour);
            legOperatingCompanyTv.setTextColor(colour);
            legDeparturePlatformTv.setTextColor(colour);
            legDepartureStatusTv.setTextColor(colour);
            legArrivalStatus.setTextColor(colour);
            legArrivalTv.setTextColor(colour);
            legArrivalStationTv.setTextColor(colour);
            legDurationTv.setTextColor(colour);
        }

        public void setArriveStatus(String status){
            if(status.equals("On time")){
                legArrivalStatus.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
            } else {
                legArrivalStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            legArrivalStatus.setText(status);
        }

        public void setDepartStatus(String status){
            /* if the train is not on time, change the text color to red */
            if(status.equals("On time")){
                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
            } else {
                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            legDepartureStatusTv.setText(status);
        }

        public void setCurrentStation(String station, int stringResource){
            legCurrentStationTv.setVisibility(VISIBLE);
            legCurrentStationTv.setText(context.getString(stringResource, station));

            green_circle.setAlpha(0.f);
            green_circle.setVisibility(View.VISIBLE);
            green_circle.animate().setDuration(1000).setInterpolator(new LinearInterpolator()).alpha(1.f).start();
        }

        public void setDuration(String departTime, String arriveTime){
            legDurationTv.setText(Utils.getTimeDifference(arriveTime, departTime));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_journey_information_leg, parent, false);
            return new LegViewer(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_journey_information_change, parent, false);
            return new ChangeViewer(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == 0){
            setupLeg(holder, position);
        } else {
            setupChange(holder, position);
        }
    }

    private void setupChange(RecyclerView.ViewHolder holder, int position) {
        ChangeViewer h = (ChangeViewer) holder;

        JourneyLeg currentLeg = journey.getLegs().get((position - 1 ) / 2);
        JourneyLeg nextLeg = journey.getLegs().get((position + 1 ) / 2);

        String arriveTime = Utils.getArriveTime(currentLeg.getDestination());
        String departTime = Utils.getDepartTime(nextLeg.getOrigin());

        h.setDuration(Utils.getTimeDifference(arriveTime, departTime));
    }

    private void setupLeg(RecyclerView.ViewHolder holder, int position) {
        LegViewer h = (LegViewer) holder;


        JourneyLeg leg = journey.getLegs().get(position / 2);

        String arriveTime = Utils.formatTime(leg.getDestination().getScheduledTime());
        String arriveStation = leg.getDestination().getStationCode();
        h.setArrival(arriveTime, arriveStation);

        String departTime = Utils.formatTime(leg.getOrigin().getScheduledTime());
        String departStation = leg.getOrigin().getStationCode();
        String departPlatform = leg.getOrigin().getPlatform();
        h.setDeparture(departTime, departStation, departPlatform);

        boolean transportModeIsTrain = leg.getTransportMode().equals("Train");
        h.setOperatingCompany(transportModeIsTrain ? leg.getServiceProviderName() : "Walk");

        String bestArriveTime = Utils.getArriveTime(leg.getDestination());
        String bestDepartTime = Utils.getDepartTime(leg.getOrigin());

        if(leg.getOrigin().getRealTime() == null){
            h.setDepartStatus("On time");
        } else {
            h.setDepartStatus("Exp " + Utils.formatTime(bestDepartTime));
        }

        if(leg.getDestination().getRealTime() == null){
            h.setArriveStatus("On time");
        } else {
            h.setArriveStatus("Exp " + Utils.formatTime(bestArriveTime));
        }

        h.setDuration(bestDepartTime, bestArriveTime);

        h.setCancelled(leg.getCancelled());

        if(transportModeIsTrain || legInfo == null) return;

        if(legInfo.get(leg.getTrainId()) != null && legInfo.get(leg.getTrainId()).getRealTimeDataAvailable()){
            List<Stop> stops = legInfo.get(leg.getTrainId()).getService().getStops();

            for(int i = 0; i < stops.size(); i++){
                Stop stop = stops.get(i);

                boolean startingStation = stop.getArrival().getNotApplicable() != null
                        && stop.getArrival().getNotApplicable();
                boolean endingStation = stop.getDeparture().getNotApplicable() != null
                        && stop.getDeparture().getNotApplicable();

                String station = StationUtils.getNameFromStationCode(stop.getLocation().getCrs());

                /* default arrived to true if we're at the starting station */
                boolean arrived = startingStation || (stop.getArrival().getRealTime() != null
                        && stop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrivedOrDeparted());

                /* default departed to false if we're at the ending station */
                boolean departed = endingStation || (stop.getDeparture().getRealTime() != null
                        && stop.getDeparture().getRealTime().getRealTimeServiceInfo().getHasArrivedOrDeparted());

                if(arrived && !departed){
                    Log.d("Currently at", station);
                    h.setCurrentStation(station, R.string.current_station);
                    break;
                }

                if(endingStation) continue;

                Stop nextStop = stops.get(i + 1);
                boolean nextArrived = nextStop.getArrival().getRealTime() != null
                        && nextStop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrivedOrDeparted();
                if(departed && !nextArrived){
                    String nextStation = StationUtils.getNameFromStationCode(nextStop.getLocation().getCrs());
                    Log.d("Travelling to ", nextStation);
                    h.setCurrentStation(nextStation, R.string.travelling_towards);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position){
        return position % 2;
    }


    @Override
    public int getItemCount() {
        return (journey.getLegs().size() * 2) - 1;
    }
}
