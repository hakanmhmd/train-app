package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

class SubscribeJourneyAdapter extends RecyclerView.Adapter<SubscribeJourneyAdapter.ViewHolder>{
    private static final String TAG = "SubscribeJourneyAdapter";

    private final List<Journey> journeys;
    private final Context context;
    private final SubscribeJourneyFragment fragment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listItemLayout)
        RelativeLayout listItemLayout;
        @BindView(R.id.departureTimeTv)
        TextView departureTimeTv;
        @BindView(R.id.departureStationTv)
        TextView departureStationTv;
        @BindView(R.id.departurePlatformTv)
        TextView departurePlatformTv;

        @BindView(R.id.times_arrow)
        ImageView arrow;

        @BindView(R.id.arrivalTimeTv)
        TextView arrivalTimeTv;
        @BindView(R.id.arrivalStationTv)
        TextView arrivalStationTv;
        @BindView(R.id.arrivalPlatformTv)
        TextView arrivalPlatformTv;

        @BindView(R.id.routeTv)
        TextView routeTv;
        @BindView(R.id.numberOfChangesTv)
        TextView numberOfChangesTv;
        @BindView(R.id.durationTv)
        TextView durationTv;
        @BindView(R.id.remindAtTv)
        TextView remindAtTv;

        @BindView(R.id.deleteBtn)
        ImageView deleteBtn;

        @OnClick(R.id.deleteBtn) void buttonPressed(){ removeJourney(getAdapterPosition()); }

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
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

        void setRoute(String origin, String dest){
            if(origin != null && dest != null){
                routeTv.setText(context.getString(R.string.route, origin, dest));
            } else {
                routeTv.setText("");
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
            durationTv.setText(Utils.getDuration(departureTime, arrivalTime));
        }

        void setReminder(String departureDateTime, int reminder){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date departTime = format.parse(departureDateTime);
                Calendar c = Calendar.getInstance();
                c.setTime(departTime);
                c.add(Calendar.MINUTE, -reminder);

                String time = new SimpleDateFormat("HH:mm").format(c.getTime());
                remindAtTv.setText(context.getString(R.string.remind_at, time));
            } catch(ParseException e){
                remindAtTv.setVisibility(GONE);
                e.printStackTrace();
            }
        }

    }

    private void removeJourney(final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Delete journey")
                .setMessage(R.string.delete_question_text)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.deleteJourney(adapterPosition);
                        journeys.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public SubscribeJourneyAdapter(List<Journey> journeys, Context context, SubscribeJourneyFragment savedJourneyFragment) {
        this.journeys = journeys;
        this.context = context;
        this.fragment = savedJourneyFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_journey_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Journey journey = journeys.get(position);

        if(journey == null) return;
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

        holder.setRoute(StationUtils.getNameFromStationCode(departureLeg.getOrigin().getStationCode()),
              StationUtils.getNameFromStationCode(arrivalLeg.getDestination().getStationCode()));

        holder.setDuration(journey.getDepartureDateTime(), journey.getArrivalDateTime());
        holder.setNumberOfChanges(journeyLegs.size()-1);

        Integer reminder = journey.getReminder();
        holder.setReminder(departureDateTime, reminder);
    }

    @Override
    public int getItemCount() {
        return journeys == null ? 0 : journeys.size();
    }
}
