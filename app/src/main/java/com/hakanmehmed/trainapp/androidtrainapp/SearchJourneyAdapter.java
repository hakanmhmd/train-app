package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Recycle view adapter
 */

class SearchJourneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SearchJourneyAdapter";
    private final Fragment fragment;
    private List<Journey> allJourneys;
    private Context context;

    public SearchJourneyAdapter(List<Journey> results, Context context, Fragment fragment) {
        allJourneys = results;
        this.context = context;
        this.fragment = fragment;
    }

    class ButtonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.buttonLayout)
        RelativeLayout buttonLayout;
        @BindView(R.id.moreTrainsButton)
        Button moreTrainsButton;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            moreTrainsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "lalal", Toast.LENGTH_LONG).show();
                }
            });
        }

        void setButtonText(String text){
            moreTrainsButton.setText(text);
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listItemLayout)
        RelativeLayout listItemLayout;
        @BindView(R.id.departureTimeTv)
        TextView departureTimeTv;
        @BindView(R.id.departureStationTv)
        TextView departureStationTv;
        @BindView(R.id.departurePlatformTv)
        TextView departurePlatformTv;

        @BindView(R.id.times_arrow)
        ImageView arrow; //TODO: FIX ARROW

        @BindView(R.id.arrivalTimeTv)
        TextView arrivalTimeTv;
        @BindView(R.id.arrivalStationTv)
        TextView arrivalStationTv;
        @BindView(R.id.arrivalPlatformTv)
        TextView arrivalPlatformTv;

        @BindView(R.id.numberOfChangesTv)
        TextView numberOfChangesTv;
        @BindView(R.id.durationTv)
        TextView durationTv;

        @OnClick(R.id.trackLiveBtn) void buttonPressed(){ inflateMoreInfoActivity(getAdapterPosition()); }

        public SearchViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ((SearchJourneyFragment)fragment).
                            displayJourneyInfo(allJourneys.get(position-1));
                }
            });
        }

        void setDeparture(String station, String platform, String time){
            departureTimeTv.setText(time);
            departureStationTv.setText(station);

            if(platform == null || platform.isEmpty()){
                departurePlatformTv.setText(R.string.no_platform);
            } else {
                departurePlatformTv.setText(context.getString(R.string.platform, platform));
            }
        }

        void setArrival(String station, String platform, String time){
            arrivalTimeTv.setText(time);
            arrivalStationTv.setText(station);

            if(platform == null || platform.isEmpty()){
                arrivalPlatformTv.setText(R.string.no_platform);
            } else {
                arrivalPlatformTv.setText(context.getString(R.string.platform, platform));
            }
        }

        void setNumberOfChanges(int changes){
            if(changes == 0) {
                numberOfChangesTv.setText(R.string.no_changes);
            } else if(changes == 1){
                numberOfChangesTv.setText(R.string.one_change);
            } else {
                numberOfChangesTv.setText(context.getString(R.string.more_changes, changes));
            }
        }

        void setDuration(String departureTime, String arrivalTime){
            durationTv.setText(Utils.getTimeDifference(departureTime, arrivalTime, true));
        }
    }

    private void inflateMoreInfoActivity(int position) {
        Journey journey = allJourneys.get(position - 1);

        Intent moreDetailsIntent = new Intent(context, JourneyInformationActivity.class);
        moreDetailsIntent.putExtra("journey", Utils.journeyToJson(journey));
        moreDetailsIntent.setAction(Long.toString(System.currentTimeMillis()));
        context.startActivity(moreDetailsIntent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_more_button, parent, false);
            return new ButtonViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_list_journey, parent, false);
            return new SearchViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if(h.getItemViewType() == 0) {
            ButtonViewHolder holder = (ButtonViewHolder) h;
            if(position == 0){
                holder.setButtonText("Search for earlier trains.");
            } else {
                holder.setButtonText("Search for later trains.");
            }
        } else {
            SearchViewHolder holder = (SearchViewHolder) h;
            Journey journey = allJourneys.get(position-1);

            if (journey == null) return;
            Log.v(TAG, journey.toString());

            List<JourneyLeg> journeyLegs = journey.getLegs();

            JourneyLeg departureLeg = journeyLegs.get(0);
            String departureStation = journey.getOrigin();
            String departurePlatform = departureLeg.getOrigin().getPlatform();
            String departureDateTime = departureLeg.getOrigin().getScheduledTime();
            holder.setDeparture(departureStation, departurePlatform, Utils.formatTime(departureDateTime));

            JourneyLeg arrivalLeg = journeyLegs.get(journeyLegs.size() - 1);
            String arrivalStation = journey.getDestination();
            String arrivalPlatform = arrivalLeg.getDestination().getPlatform();
            String arrivalDateTime = arrivalLeg.getDestination().getScheduledTime();
            holder.setArrival(arrivalStation, arrivalPlatform, Utils.formatTime(arrivalDateTime));

            holder.setDuration(journey.getDepartureDateTime(), journey.getArrivalDateTime());
            holder.setNumberOfChanges(journeyLegs.size() - 1);
        }

    }

    @Override
    public int getItemViewType(int position){
        if(position == 0 || position == allJourneys.size()+1){
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return allJourneys == null ? 0 : allJourneys.size() + 2;
    }


}
