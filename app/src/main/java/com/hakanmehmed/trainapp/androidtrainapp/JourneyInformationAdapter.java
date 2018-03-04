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

    public JourneyInformationAdapter(Journey journey, HashMap<String, LiveDataSearchResponse> legInfo,
                                     Context context, JourneyInformationActivity activity) {
        this.journey = journey;
        this.legInfo = legInfo;
        this.context = context;
        this.activity = activity;
        android.util.Log.v(TAG, String.valueOf(legInfo == null ? 0 : legInfo.size()));
    }

    public class ChangeViewer extends RecyclerView.ViewHolder {
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

        public void setDuration(String duration) {
            changeDurationTv.setText(duration);
        }

        public void setStation(String station) {
            changeStationTv.setText(context.getString(R.string.change_message,
                    StationUtils.getNameFromStationCode(station)));
        }
    }

    public class LegViewer extends RecyclerView.ViewHolder {
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

            listView.setHasFixedSize(true);
            listView.setLayoutManager(new LinearLayoutManager(context));
        }

        public void setViewAdapter(ArrayList<StopInfo> info) {
            listView.setAdapter(new LegInformationListViewAdapter(context, info));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
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
        if (holder.getItemViewType() == 0) {
            setupLeg(holder, position);
        } else {
            setupChange(holder, position);
        }
    }

    private void setupChange(RecyclerView.ViewHolder holder, int position) {
        ChangeViewer h = (ChangeViewer) holder;

        JourneyLeg currentLeg = journey.getLegs().get((position - 1) / 2);
        JourneyLeg nextLeg = journey.getLegs().get((position + 1) / 2);

        String arriveTime = Utils.getArriveTime(currentLeg.getDestination());
        String departTime = Utils.getDepartTime(nextLeg.getOrigin());

        String timeDifference = Utils.getTimeDifference(departTime, arriveTime, false);
        h.setDuration(timeDifference);
        h.setStation(currentLeg.getDestination().getStationCode());
    }

    private void setupLeg(RecyclerView.ViewHolder holder, int position) {
        LegViewer h = (LegViewer) holder;

        JourneyLeg leg = journey.getLegs().get(position / 2);

        if (legInfo == null) {
            android.util.Log.v(TAG, "legInfo is null");
            return;
        }

        boolean transportModeIsTrain = leg.getTransportMode().equals("Train");
        String departStation = leg.getOrigin().getStationCode();
        String arriveStation = leg.getDestination().getStationCode();

        if (leg.getCancelled()) {
            ArrayList<StopInfo> stopInfo = new ArrayList<>();
            StopInfo departureInfo = new StopInfo(departStation,
                    leg.getOrigin().getScheduledTime(),
                    null,
                    "Cancelled",
                    transportModeIsTrain ? leg.getServiceProviderName() : leg.getTransportMode(),
                    new Pair<String, Integer>(null, null));

            StopInfo arrivalInfo = new StopInfo(arriveStation,
                    leg.getDestination().getScheduledTime(),
                    null,
                    "Cancelled",
                    "",
                    new Pair<String, Integer>(null, null));

            stopInfo.add(departureInfo);
            stopInfo.add(arrivalInfo);

            h.setViewAdapter(stopInfo);
            return;
        }

        // When the connection is via bus or tube, legInfo does not exist
        if (legInfo.get(leg.getTrainId()) == null) {
            ArrayList<StopInfo> stopInfo = new ArrayList<>();
            StopInfo departureInfo = new StopInfo(departStation,
                    leg.getOrigin().getScheduledTime(),
                    null,
                    "",
                    transportModeIsTrain ? leg.getServiceProviderName() : leg.getTransportMode(),
                    new Pair<String, Integer>(null, null));

            StopInfo arrivalInfo = new StopInfo(arriveStation,
                    leg.getDestination().getScheduledTime(),
                    null,
                    "",
                    "",
                    new Pair<String, Integer>(null, null));

            stopInfo.add(departureInfo);
            stopInfo.add(arrivalInfo);

            h.setViewAdapter(stopInfo);
            return;
        }

        String legDepartureStation = leg.getOrigin().getStationCode();
        String legArrivalStation = leg.getDestination().getStationCode();
        boolean shouldAddStop = true;
        boolean firstStop = true;


        List<Stop> stops = legInfo.get(leg.getTrainId()).getService().getStops();

        ArrayList<StopInfo> stopInfo = new ArrayList<>();
        for (int i = 0; i < stops.size(); i++) {
            Stop stop = stops.get(i);

            String station = stop.getLocation().getCrs();

            if(!shouldAddStop){
              continue;
            }

            Arrival arrival = stop.getArrival();
            Departure departure = stop.getDeparture();

            boolean isStartingStation = arrival != null &&
                    arrival.getNotApplicable() != null &&
                    arrival.getNotApplicable();
            boolean isEndingStation = departure != null &&
                    departure.getNotApplicable() != null &&
                    departure.getNotApplicable();

            String arrivalTime = null;
            if (arrival != null) {
                arrivalTime = isStartingStation ?
                        null : (arrival.getScheduled() == null) ?
                        null : arrival.getScheduled().getScheduledTime();
            }

            String departureTime = null;
            if (departure != null) {
                departureTime = isEndingStation ?
                        null : (departure.getScheduled() == null) ?
                        null : departure.getScheduled().getScheduledTime();
            }

            String time;
            if (station.equals(legArrivalStation)) {
                time = arrivalTime == null ? (departureTime == null ? null : departureTime) : arrivalTime;
            } else {
                time = departureTime == null ? (arrivalTime == null ? null : arrivalTime) : departureTime;
            }


            String platform = "";
            if (!isEndingStation && departure != null) {
                platform = (departure.getScheduled() == null) ?
                        null : departure.getScheduled().getScheduledPlatform();
                if (platform == null && departure.getRealTime() != null &&
                        departure.getRealTime().getRealTimeServiceInfo() != null) {
                    platform = departure.getRealTime().getRealTimeServiceInfo().getRealTimePlatform();
                }
            }

            String status = "On time";
            boolean cancelled = false;
            if (departure != null && departure.getRealTime() != null
                    && departure.getRealTime().getCancelled() != null
                    && departure.getRealTime().getCancelled().isCancelled()) {
                status = "Cancelled";
                cancelled = true;
            }

            if (departure != null && departure.getRealTime() != null
                    && departure.getRealTime().getRealTimeServiceInfo() != null
                    && departureTime != null) {
                String realTime = departure.getRealTime().getRealTimeServiceInfo().getRealTime();
                if (realTime != null) {
                    if (!Utils.isSameTime(departureTime, realTime)) {
                        status = "Exp. " + Utils.formatTime(realTime);
                    }
                }
            }

            if (departure != null && departure.getRealTime() != null
                    && departure.getRealTime().getDelayReason() != null) {
                status = "Delayed";
            }

            String service = null;
            if (firstStop) {
                firstStop = false;
                String transportMode = leg.getTransportMode();
                String provider = leg.getServiceProviderName();
                service = transportMode.equals("Train") ? provider : transportMode;
            }


            boolean arrived = isStartingStation || (arrival != null
                    && arrival.getRealTime() != null
                    && arrival.getRealTime().getRealTimeServiceInfo() != null
                    && arrival.getRealTime().getRealTimeServiceInfo().getHasArrived());

            boolean departed = isEndingStation || (departure != null
                    && departure.getRealTime() != null
                    && departure.getRealTime().getRealTimeServiceInfo() != null
                    && departure.getRealTime().getRealTimeServiceInfo().getHasDeparted());

            String progress = null;
            int resource = 0;
            if (arrived && !departed && !isStartingStation && !isEndingStation && !cancelled) {
                progress = station;
                resource = R.string.current_station;
            }

            if (!isEndingStation && !cancelled) {
                Stop nextStop = stops.get(i + 1);
                boolean nextArrived = nextStop.getArrival() != null && nextStop.getArrival().getRealTime() != null
                        && nextStop.getArrival().getRealTime().getRealTimeServiceInfo() != null
                        && nextStop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrived();

                if (departed && !nextArrived) {
                    progress = nextStop.getLocation().getCrs();
                    resource = R.string.travelling_to;
                }
            }

            Pair<String, Integer> pair = new Pair<>(progress, resource);
            if (station.equals(legArrivalStation)) {
                shouldAddStop = false;
                pair = new Pair<>(null, 0);
            }
            StopInfo stopinfo = new StopInfo(station, time, platform, status, service, pair);
            stopInfo.add(stopinfo);


        }


        h.setViewAdapter(stopInfo);

    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }


    @Override
    public int getItemCount() {
        return (journey.getLegs().size() * 2) - 1;
    }
}
