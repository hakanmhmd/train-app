package com.hakanmehmed.trainapp.androidtrainapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class JourneyInformationActivity extends AppCompatActivity {

    private static final String TAG = "JourneyInfoActivity";
    @BindView(R.id.journeyInfoLayout)
    RelativeLayout journeyInfoLayout;
    @BindView(R.id.journeyStationsTv)
    TextView journeyStationsTv;
    @BindView(R.id.journeyDetailsTv)
    TextView journeyDetailsTv;
    @BindView(R.id.journeyLegsRv)
    RecyclerView journeyLegsRv;

    private LiveDataFeedApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_information);

        ButterKnife.bind(this);
        api = new LiveDataFeedApi();

        Journey journey = Utils.jsonToJourney(getIntent().getStringExtra("journey"));
        getLiveDataFeed(journey);

        String departStation = StationUtils.getNameFromStationCode(journey.getOrigin());
        String arriveStation = StationUtils.getNameFromStationCode(journey.getDestination());
        journeyStationsTv.setText(getString(R.string.route, departStation, arriveStation));

        String duration = Utils.getTimeDifference(journey.getArrivalDateTime(), journey.getDepartureDateTime(), false);

        Log.v(TAG, duration);
        int changes = journey.getLegs().size() - 1;
        if(changes == 0){
            journeyDetailsTv.setText(duration + ", " + getString(R.string.no_changes));
        } else if(changes == 1) {
            journeyDetailsTv.setText(duration + ", " + getString(R.string.one_change));
        } else {
            journeyDetailsTv.setText(duration + ", " + getString(R.string.more_changes, changes));
        }

        journeyLegsRv.setHasFixedSize(true);
        journeyLegsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down);
        journeyLegsRv.setLayoutAnimation(animation);
        journeyLegsRv.setAdapter(new JourneyInformationAdapter(journey, null, getApplicationContext(), this));

    }

    public void getLiveDataFeed(final Journey journey){
        final List<LiveDataSearchResponse> liveDataSearchResponses = new ArrayList<>();

        for(JourneyLeg leg : journey.getLegs()){

            final String trainId = leg.getTrainId();
            Log.v(TAG, "LegViewer " + leg.getTrainId());

            if(leg.getTransportMode().equals("Walk") || trainId == null){
                liveDataSearchResponses.add(null);
                continue;
            }

            api.getLiveData(trainId, new CustomCallback<LiveDataSearchResponse>() {
                @Override
                public void onSuccess(Response<LiveDataSearchResponse> response) {
                    liveDataSearchResponses.add(response.body());

                    if(liveDataSearchResponses.size() == journey.getLegs().size()){
                        showLiveData(journey, liveDataSearchResponses);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    private void showLiveData(Journey journey, List<LiveDataSearchResponse> liveDataSearchResponses) {
        HashMap<String, LiveDataSearchResponse> legInfo = new HashMap<>();
        for(LiveDataSearchResponse train : liveDataSearchResponses){
            if(train == null || train.getService() == null) continue;

            String trainId = train.getService().getServiceUid();
            legInfo.put(trainId, train);
        }


        journeyLegsRv.setAdapter(new JourneyInformationAdapter(journey, legInfo, getApplicationContext(), this));
        journeyLegsRv.invalidate();

        Log.v(TAG, "Updated adapter with live data");
    }
}
