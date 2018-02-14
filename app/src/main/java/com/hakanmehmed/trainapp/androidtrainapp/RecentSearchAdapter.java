package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by hakanmehmed on 14/02/2018.
 */

class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private static final String TAG = "RecentSearchAdapter";
    private final Fragment fragment;
    private final ArrayList<RecentSearch> recentSearches;
    private final Context context;

    public RecentSearchAdapter(ArrayList<RecentSearch> recentSearches, Context context, Fragment fragment) {
        this.recentSearches = recentSearches;
        this.context = context;
        this.fragment = fragment;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_search_list_journey, parent, false);
        return new RecentSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecentSearch recentSearch = recentSearches.get(position);

        String departureStationCode = StationUtils.getCodeFromStationName(recentSearch.getFrom());
        String arrivalStationCode = StationUtils.getCodeFromStationName(recentSearch.getTo());

        holder.setDepartureStation(departureStationCode);
        holder.setArrivalStation(arrivalStationCode);
        holder.setRouteText(recentSearch.getFrom(), recentSearch.getTo());
    }

    @Override
    public int getItemCount() {
        return recentSearches == null ? 0 : recentSearches.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.departureStationTv)
        TextView departureStationTv;
        @BindView(R.id.arrivalStationTv)
        TextView arrivalStationTv;
        @BindView(R.id.routeTv)
        TextView routeTv;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // populate fields
                    ((SearchJourneyFragment)fragment).fillUpFields(recentSearches.get(getAdapterPosition()));
                }
            });
        }

        void setDepartureStation(String stationCode){
            departureStationTv.setText(stationCode);
        }

        void setArrivalStation(String stationCode){
            arrivalStationTv.setText(stationCode);
        }

        void setRouteText(String arrivalStation, String departureStation){
            routeTv.setText(context.getString(R.string.route, arrivalStation, departureStation));
        }
    }




}
