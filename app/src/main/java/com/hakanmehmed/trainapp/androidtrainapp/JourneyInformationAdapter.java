package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class JourneyInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JourneyInfoAdapter";
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
//        @BindView(R.id.legDepartureTv)
//        TextView legDepartureTv;
//        @BindView(R.id.legArrivalStatus)
//        TextView legArrivalStatus;
//        @BindView(R.id.legOperatingCompanyTv)
//        TextView legOperatingCompanyTv;
//        @BindView(R.id.legDeparturePlatformTv)
//        TextView legDeparturePlatformTv;
//        @BindView(R.id.legDurationTv)
//        TextView legDurationTv;
//        @BindView(R.id.legArrivalTv)
//        TextView legArrivalTv;
//        @BindView(R.id.legDepartureStatusTv)
//        TextView legDepartureStatusTv;
//        @BindView(R.id.legDepartureStationTv)
//        TextView legDepartureStationTv;
//        @BindView(R.id.legArrivalStationTv)
//        TextView legArrivalStationTv;
//        @BindView(R.id.legCurrentStationTv)
//        TextView legCurrentStationTv;
//        @BindView(R.id.green_circle)
//        ImageView green_circle;
        @BindView(R.id.listView)
        RecyclerView listView;

        public LegViewer(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //green_circle.setVisibility(GONE);
            //legCurrentStationTv.setVisibility(GONE);

            listView.setHasFixedSize(true);
            listView.setLayoutManager(new LinearLayoutManager(context));
        }

        public void setViewAdapter(ArrayList<StopInfo> info) {
            listView.setAdapter(new LegInformationListViewAdapter(context, info));
        }

//        public void setArrival(String time, String station){
//            legArrivalTv.setText(time);
//            legArrivalStationTv.setText(StationUtils.getNameFromStationCode(station));
//        }
//
//        public void setDeparture(String time, String station, String platform){
//            legDepartureTv.setText(time);
//            legDepartureStationTv.setText(StationUtils.getNameFromStationCode(station));
//
//            if(platform != null && !platform.isEmpty()){
//                legDeparturePlatformTv.setText(context.getString(R.string.platform, platform));
//            } else {
//                legDeparturePlatformTv.setVisibility(GONE);
//            }
//        }
//
//        public void setOperatingCompany(String company){
//            legOperatingCompanyTv.setText(company);
//        }

//        public void setCancelled(boolean cancelled){
//            if(!cancelled) return;
//
//            setArriveStatus("Cancelled");
//            setDepartStatus("Cancelled");
//
//            int colour = ContextCompat.getColor(context, R.color.colorDarkGray);
//            legDepartureTv.setTextColor(colour);
//            legDepartureStationTv.setTextColor(colour);
//            legCurrentStationTv.setTextColor(colour);
//            legOperatingCompanyTv.setTextColor(colour);
//            legDeparturePlatformTv.setTextColor(colour);
//            legDepartureStatusTv.setTextColor(colour);
//            legArrivalStatus.setTextColor(colour);
//            legArrivalTv.setTextColor(colour);
//            legArrivalStationTv.setTextColor(colour);
//            legDurationTv.setTextColor(colour);
//        }

//        public void setArriveStatus(String status){
//            if(status.equals("On time")){
//                legArrivalStatus.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
//            } else {
//                legArrivalStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
//            }
//            legArrivalStatus.setText(status);
//        }
//
//        public void setDepartStatus(String status){
//            /* if the train is not on time, change the text color to red */
//            if(status.equals("On time")){
//                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
//            } else {
//                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.red));
//            }
//            legDepartureStatusTv.setText(status);
//        }

//        public void setCurrentStation(String station, int stringResource){
//            legCurrentStationTv.setVisibility(VISIBLE);
//            legCurrentStationTv.setText(context.getString(stringResource, station));
//
//            green_circle.setAlpha(0.f);
//            green_circle.setVisibility(View.VISIBLE);
//            green_circle.animate().setDuration(1000).setInterpolator(new LinearInterpolator()).alpha(1.f).start();
//        }
//
//        public void setDuration(String departTime, String arriveTime){
//            legDurationTv.setText(Utils.getTimeDifference(arriveTime, departTime, false));
//        }
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

        String timeDifference = Utils.getTimeDifference(departTime, arriveTime, false);
        h.setDuration(timeDifference);
        h.setStation(currentLeg.getDestination().getStationCode());
    }

    private void setupLeg(RecyclerView.ViewHolder holder, int position) {
        LegViewer h = (LegViewer) holder;

        JourneyLeg leg = journey.getLegs().get(position / 2);

        //////////////////////////////////////////////////////////////////////////////
//        String departTime = Utils.formatTime(leg.getOrigin().getScheduledTime());
//        String departStation = leg.getOrigin().getStationCode();
//        String departPlatform = leg.getOrigin().getPlatform();
//        h.setDeparture(departTime, departStation, departPlatform);
//
//        String arriveTime = Utils.formatTime(leg.getDestination().getScheduledTime());
//        String arriveStation = leg.getDestination().getStationCode();
//        h.setArrival(arriveTime, arriveStation);
//
//
//        h.setOperatingCompany(transportModeIsTrain ? leg.getServiceProviderName() : leg.getTransportMode());
//
//        String bestArriveTime = Utils.getArriveTime(leg.getDestination());
//        String bestDepartTime = Utils.getDepartTime(leg.getOrigin());
//
//        if(leg.getOrigin().getRealTime() == null){
//            h.setDepartStatus("On time");
//        } else {
//            h.setDepartStatus("Exp " + Utils.formatTime(bestDepartTime));
//        }
//
//        if(leg.getDestination().getRealTime() == null){
//            h.setArriveStatus("On time");
//        } else {
//            h.setArriveStatus("Exp " + Utils.formatTime(bestArriveTime));
//        }
//
//        h.setDuration(bestDepartTime, bestArriveTime);

        // TODO : CHECK THIS
        //h.setCancelled(leg.getCancelled());
        //////////////////////////////////////////////////////////////////////////////

        if(legInfo == null) {
            return;
        }
        boolean transportModeIsTrain = leg.getTransportMode().equals("Train");
        String departStation = leg.getOrigin().getStationCode();
        String arriveStation = leg.getDestination().getStationCode();

        // When the connection is via bus or tube, legInfo does not exist
        if(legInfo.get(leg.getTrainId()) == null){
            ArrayList<StopInfo> stopInfo = new ArrayList<>();
            StopInfo departureInfo = new StopInfo(departStation,
                    leg.getOrigin().getScheduledTime(),
                    null,
                    "",
                    transportModeIsTrain ? leg.getServiceProviderName() : leg.getTransportMode(),
                    new Pair<String, Integer>(null ,null));

            StopInfo arrivalInfo = new StopInfo(arriveStation,
                    leg.getDestination().getScheduledTime(),
                    null,
                    "",
                    "",
                    new Pair<String, Integer>(null ,null));

            stopInfo.add(departureInfo);
            stopInfo.add(arrivalInfo);

            h.setViewAdapter(stopInfo);
            return;
        }

        String legDepartureStation = leg.getOrigin().getStationCode();
        String legArrivalStation = leg.getDestination().getStationCode();
        boolean shouldAddStop = false;
        boolean firstStop = true;


        if(legInfo.get(leg.getTrainId()).getRealTimeDataAvailable()){
            List<Stop> stops = legInfo.get(leg.getTrainId()).getService().getStops();

            ArrayList<StopInfo> stopInfo = new ArrayList<>();
            for(int i = 0; i < stops.size(); i++){
                Stop stop = stops.get(i);

                String station = stop.getLocation().getCrs();
                if(station.equals(legDepartureStation)){
                    shouldAddStop = true;
                }

                if(!shouldAddStop){
                    continue;
                }

                boolean isStartingStation = stop.getArrival().getNotApplicable() != null
                        && stop.getArrival().getNotApplicable();
                boolean isEndingStation = stop.getDeparture().getNotApplicable() != null
                        && stop.getDeparture().getNotApplicable();

                String arrivalTime = isStartingStation ?
                        null : (stop.getArrival().getScheduled() == null) ?
                        null : stop.getArrival().getScheduled().getScheduledTime();
                String departureTime = isEndingStation ?
                        null : (stop.getDeparture().getScheduled() == null) ?
                        null : stop.getDeparture().getScheduled().getScheduledTime();

                String time = departureTime == null ? (arrivalTime == null ? null : arrivalTime) : departureTime;

                String platform = "";
                if(!isEndingStation){
                    platform = (stop.getDeparture().getScheduled() == null) ?
                            null : stop.getDeparture().getScheduled().getScheduledPlatform();
                    if(platform == null && stop.getDeparture().getRealTime() != null){
                        platform = stop.getDeparture().getRealTime().getRealTimeServiceInfo().getRealTimePlatform();
                    }
                }

                String status = "On time";
                if(stop.getDeparture().getRealTime() != null && departureTime != null){
                    String realTime = stop.getDeparture().getRealTime().getRealTimeServiceInfo().getRealTime();
                    if(realTime != null) {
                        if (!Utils.isSameTime(departureTime, realTime)) {
                            status = "Exp. " + Utils.formatTime(realTime);
                        }
                    }
                }

                if(stop.getDeparture().getRealTime() != null
                        && stop.getDeparture().getRealTime().getDelayReason() != null){
                    status = "Delayed";
                }

                String service = null;
                if(firstStop){
                    firstStop = false;
                    String transportMode = leg.getTransportMode();
                    String provider = leg.getServiceProviderName();
                    service = transportMode.equals("Train") ? provider : transportMode;
                }


                boolean arrived = isStartingStation || (stop.getArrival().getRealTime() != null
                        && stop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrived());

                boolean departed = isEndingStation || (stop.getDeparture().getRealTime() != null
                        && stop.getDeparture().getRealTime().getRealTimeServiceInfo().getHasDeparted());

                String progress = null;
                int resource = 0;
                if(arrived && !departed && !isStartingStation && !isEndingStation){
                    //Log.v("Currently at", station);
                    progress = station;
                    resource = R.string.current_station;
                    //h.setCurrentStation(station, R.string.current_station);
                }

                if(!isEndingStation) {
                    Stop nextStop = stops.get(i + 1);
                    boolean nextArrived = nextStop.getArrival().getRealTime() != null
                            && nextStop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrived();

                    if (departed && !nextArrived) {
                        progress = nextStop.getLocation().getCrs();
                        resource = R.string.travelling_to;
                    }
                }

                Pair<String, Integer> pair = new Pair<>(progress, resource);
                StopInfo stopinfo = new StopInfo(station, time, platform, status, service, pair);
                stopInfo.add(stopinfo);

                if(station.equals(legArrivalStation)){
                    shouldAddStop = false;
                }
            }


            h.setViewAdapter(stopInfo);
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
