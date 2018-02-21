package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.view.View.GONE;

/**
 * Created by hakanmehmed on 20/02/2018.
 */

public class LegInformationListViewAdapter extends RecyclerView.Adapter<LegInformationListViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StopInfo> info;

    public LegInformationListViewAdapter(Context context, ArrayList<StopInfo> info) {
        this.context = context;
        this.info = info;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.legDepartureTv)
        TextView legDepartureTv;
        @BindView(R.id.legDeparturePlatformTv)
        TextView legDeparturePlatformTv;
        @BindView(R.id.legDepartureStationTv)
        TextView legDepartureStationTv;
        @BindView(R.id.legOperatingCompanyTv)
        TextView legServiceTv;
        @BindView(R.id.legDepartureStatusTv)
        TextView legDepartureStatusTv;
        @BindView(R.id.legCurrentStationTv)
        TextView legCurrentStationTv;
        @BindView(R.id.circle)
        ImageView circle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void setLegDepartureTv(String time){
            legDepartureTv.setText(Utils.formatTime(time));
        }

        void setLegDeparturePlatformTv(String plat){
            if(plat == null || plat.equals("")){
                legDeparturePlatformTv.setVisibility(GONE);
            } else {
                legDeparturePlatformTv.setText(context.getString(R.string.platform, plat));
            }
        }

        void setLegDepartureStationTv(String station){
            legDepartureStationTv.setText(StationUtils.getNameFromStationCode(station));
        }

        void setLegDepartureStatusTv(String status){
            if(status.equals("On time")){
                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGray));
            } else {
                legDepartureStatusTv.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            legDepartureStatusTv.setText(status);
        }

        void setLegServiceTv(String service){
            if(service == null || service.equals("")){
                legServiceTv.setVisibility(GONE);
            } else {
                legServiceTv.setText(service);
            }
        }

        void setLegCurrentStationTv(String current, Integer stringResource){
            if(current == null){
                legCurrentStationTv.setVisibility(GONE);
                circle.setVisibility(GONE);
            } else {
                legCurrentStationTv.setText(context.getString(stringResource,
                        StationUtils.getNameFromStationCode(current)));
                circle.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_journey_information_single_leg, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StopInfo stopInfo = info.get(position);

        holder.setLegDepartureTv(stopInfo.getArrivalTime());
        holder.setLegDeparturePlatformTv(stopInfo.getPlatform());
        holder.setLegDepartureStationTv(stopInfo.getStation());
        holder.setLegServiceTv(stopInfo.getService());
        holder.setLegDepartureStatusTv(stopInfo.getStatus());

        Pair<String, Integer> pair = stopInfo.getPair();
        holder.setLegCurrentStationTv(pair.first, pair.second);

        AlphaAnimation anim = new AlphaAnimation(1, 0);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        holder.circle.setHasTransientState(true);
        holder.circle.setAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return info == null ? 0 : info.size();
    }
}
