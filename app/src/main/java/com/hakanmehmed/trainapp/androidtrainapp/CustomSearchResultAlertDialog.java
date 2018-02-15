package com.hakanmehmed.trainapp.androidtrainapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    CustomSearchResultAlertDialog(FragmentActivity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);

    }

    int getReminder(){
        return timeSlider.getProgress() * 5;
    }

    void inflateDialog(Journey journey){
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.search_result_dialog, null);
        builder.setView(view);

        ButterKnife.bind(this, view);

        timeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int minutes = i * 5;
                if(minutes == 1){
                    reminderText.setText(activity.getString(R.string.remind_one_minute));
                } else {
                    reminderText.setText(activity.getString(R.string.remind_more_minutes, minutes));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    void setListener(View.OnClickListener listener){
        subscribeBtn.setOnClickListener(listener);
    }

    void cancel() {
        alertDialog.cancel();
    }

    void show(){
        alertDialog = builder.create();
        alertDialog.show();
    }
}
