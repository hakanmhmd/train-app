package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

class SubscribeJourneyAdapter extends RecyclerView.Adapter {
    private final List<Journey> journeys;
    private final Context context;
    private final SubscribeJourneyFragment fragment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    public SubscribeJourneyAdapter(List<Journey> journeys, Context context, SubscribeJourneyFragment savedJourneyFragment) {
        this.journeys = journeys;
        this.context = context;
        this.fragment = savedJourneyFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribe_journey_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return journeys == null ? 0 : journeys.size();
    }
}
