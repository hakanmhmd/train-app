package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by hakanmehmed on 17/02/2018.
 */

class JourneyInformationAdapter extends RecyclerView.Adapter {

    public JourneyInformationAdapter(Journey journey, HashMap<String,
            LiveDataSearchResponse> legInfo, Context context, JourneyInformationActivity activity) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
