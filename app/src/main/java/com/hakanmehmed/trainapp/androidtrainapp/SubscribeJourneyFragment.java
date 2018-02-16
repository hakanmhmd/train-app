package com.hakanmehmed.trainapp.androidtrainapp;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeJourneyFragment extends Fragment {
    private static final String TAG = "SubscribeJourneyFragment";

    @BindView(R.id.subscribedJourneyLayout)
    LinearLayout subscribedJourneyLayout;
    @BindView(R.id.subscribedJourneys)
    RecyclerView subscribedJourneys;
    @BindView(R.id.no_subscribed_journeys)
    TextView no_subscribed_journeys;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribed_journey, container, false);

        ButterKnife.bind(this, view);

        subscribedJourneys.setHasFixedSize(true);
        subscribedJourneys.setLayoutManager(new LinearLayoutManager(getContext()));

        loadSubscribedJourneys();

        return view;
    }

    private void loadSubscribedJourneys() {
        ArrayList<Journey> journeys = Utils.getSubscribedJourneys(getContext());

        if(journeys.size() == 0){
            no_subscribed_journeys.setVisibility(VISIBLE);
            subscribedJourneys.setVisibility(GONE);
        } else {
            no_subscribed_journeys.setVisibility(GONE);
            subscribedJourneys.setVisibility(VISIBLE);
        }

        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        subscribedJourneys.setLayoutAnimation(animation);
        subscribedJourneys.setAdapter(new SubscribeJourneyAdapter(journeys, getContext(), this));
    }

    public void deleteJourney(final int index){
        Utils.unsubscribeJourney(index, getContext());
    }

    public SubscribeJourneyFragment() {
        // Required empty public constructor
    }

}
