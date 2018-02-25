package com.hakanmehmed.trainapp.androidtrainapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

class CustomSearchResultAlertDialog {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

    @BindView(R.id.subscribeBtn)
    Button subscribeBtn;
    @BindView(R.id.reminderText)
    TextView reminderText;
    @BindView(R.id.timeSlider)
    SeekBar timeSlider;
    @BindView(R.id.departureTimeTv)
    TextView departureTimeTv;
    @BindView(R.id.departureStationTv)
    TextView departureStationTv;
    @BindView(R.id.departurePlatformTv)
    TextView departurePlatformTv;
    @BindView(R.id.remindAtTv)
    TextView remindAtTv;
    @BindView(R.id.arrivalStationTv)
    TextView arrivalStationTv;
    @BindView(R.id.arrivalPlatformTv)
    TextView arrivalPlatformTv;
    @BindView(R.id.arrivalTimeTv)
    TextView arrivalTimeTv;

    String departureDateTime;


    CustomSearchResultAlertDialog(FragmentActivity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
        this.builder.setTitle("Subscribe to a journey");
    }

    int getReminder() {
        return timeSlider.getProgress() * 5;
    }

    void inflateDialog(Journey journey) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.subscribe_dialog, null);
        builder.setView(view);

        ButterKnife.bind(this, view);

        timeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int minutes = i * 5;
                if (minutes == 1) {
                    reminderText.setText(activity.getString(R.string.remind_one_minute));
                } else {
                    reminderText.setText(activity.getString(R.string.remind_more_minutes, minutes));
                }
                setReminder(minutes);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        List<JourneyLeg> journeyLegs = journey.getLegs();
        JourneyLeg departureLeg = journeyLegs.get(0);
        String departureStation = journey.getOrigin();
        String departurePlatform = departureLeg.getOrigin().getPlatform();
        String departureDateTime = departureLeg.getOrigin().getScheduledTime();
        this.departureDateTime = departureDateTime;
        setDeparture(departureStation, departurePlatform, Utils.formatTime(departureDateTime));

        JourneyLeg arrivalLeg = journeyLegs.get(journeyLegs.size() - 1);
        String arrivalStation = journey.getDestination();
        String arrivalPlatform = arrivalLeg.getDestination().getPlatform();
        String arrivalDateTime = arrivalLeg.getDestination().getScheduledTime();
        setArrival(arrivalStation, arrivalPlatform, Utils.formatTime(arrivalDateTime));

        setReminder(0);
    }

    private void setReminder(int reminder){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date departTime = format.parse(departureDateTime);
            Calendar c = Calendar.getInstance();
            c.setTime(departTime);
            c.add(Calendar.MINUTE, -reminder);

            String time = new SimpleDateFormat("HH:mm").format(c.getTime());
            remindAtTv.setText(activity.getApplicationContext().getString(R.string.remind_at, time));
        } catch(ParseException e){
            remindAtTv.setVisibility(GONE);
            e.printStackTrace();
        }
    }

    private void setArrival(String arrivalStation, String arrivalPlatform, String arrivalTime) {
        arrivalStationTv.setText(arrivalStation);
        arrivalTimeTv.setText(arrivalTime);

        if(arrivalPlatform == null || arrivalPlatform.isEmpty()){
            arrivalPlatformTv.setText(R.string.no_platform);
        } else {
            arrivalPlatformTv.setText(activity.getApplicationContext().getString(R.string.platform, arrivalPlatform));
        }
    }

    private void setDeparture(String departureStation, String departurePlatform, String departureTime) {
        departureTimeTv.setText(departureTime);
        departureStationTv.setText(departureStation);

        if(departurePlatform == null || departurePlatform.isEmpty()){
            arrivalPlatformTv.setText(R.string.no_platform);
        } else {
            arrivalPlatformTv.setText(activity.getApplicationContext().getString(R.string.platform, departurePlatform));
        }
    }

    void setListener(View.OnClickListener listener) {
        subscribeBtn.setOnClickListener(listener);
    }

    void cancel() {
        alertDialog.cancel();
    }

    void show() {
        alertDialog = builder.create();
        alertDialog.show();
    }
}
