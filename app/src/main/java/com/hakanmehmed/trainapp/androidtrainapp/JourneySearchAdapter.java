package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.*;

/**
 * Created by hakanmehmed on 13/02/2018.
 * Recycle view adapter
 */

class JourneySearchAdapter extends RecyclerView.Adapter<JourneySearchAdapter.ViewHolder> {
    private static final String TAG = "JourneySearchAdapter";
    private final ArrayList<Object> animation;
    private List<Journey> allJourneys;
    private Context context;

    public JourneySearchAdapter(List<Journey> results, Context context) {
        allJourneys = results;
        this.context = context;
        animation = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        void setRoute(String route){

        }

        // TODO: make sure this works
        void setNumberOfChanges(int changes){
            if(changes == 0) numberOfChangesTv.setText(R.string.no_changes);
            else numberOfChangesTv.setText(context.getResources().getQuantityString(R.plurals.changes, changes));
        }

        void setDuration(){
            //arrivetime - depart time
        }

        void setReminder(){

        }

        void showDeleteIcon(boolean show){
            if(!show)  deleteBtn.setVisibility(GONE);
        }

        void playAnimation(boolean animated, int position){
            itemView.setTranslationY(getScreenHeight(context));
            itemView
                    .animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(animated ? 0 : 500)
                    .setStartDelay(animated ? 0 : (100 * position)).start();
        }
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_journey, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Journey journey = allJourneys.get(position);

        if(journey == null) return;
        Log.v(TAG, journey.toString());

        List<JouneryLeg> journeyLegs = journey.getLegs();

        JouneryLeg departureLeg = journeyLegs.get(0);
        String departureStation = journey.getOrigin();
        String departurePlatform = departureLeg.getOrigin().getPlatform();
        String departureDateTime = journey.getDepartureDateTime();
        holder.setDeparture(departureStation, departurePlatform, Utils.formatTime(departureDateTime));

        JouneryLeg arrivalLeg = journeyLegs.get(journeyLegs.size() - 1);
        String arrivalStation = journey.getDestination();
        String arrivalPlatform = arrivalLeg.getDestination().getPlatform();
        String arrivalDateTime = journey.getArrivalDateTime();
        holder.setArrival(arrivalStation, arrivalPlatform, Utils.formatTime(arrivalDateTime));

        boolean animated = animation.indexOf(position) > -1;
        holder.playAnimation(animated, position);
        if(!animated) animation.add(position);

    }

    @Override
    public int getItemCount() {
        return allJourneys == null ? 0 : allJourneys.size();
    }

    private void removeJourney(int adapterPosition) {
        allJourneys.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, allJourneys.size());
        // TODO: add popup are you sure you want to delete?
        Toast.makeText(context ,"Removed : ", Toast.LENGTH_SHORT).show();
    }
}
