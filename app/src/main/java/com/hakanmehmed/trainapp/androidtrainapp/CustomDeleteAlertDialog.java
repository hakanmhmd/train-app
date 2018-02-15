package com.hakanmehmed.trainapp.androidtrainapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

public class CustomDeleteAlertDialog {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private FragmentActivity activity;

    @BindView(R.id.yesBtn)
    Button yesBtn;

    @BindView(R.id.noBtn)
    Button noBtn;


    CustomDeleteAlertDialog(FragmentActivity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }


    void inflateDialog(Journey journey){
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.delete_alert, null);
        builder.setView(view);

        ButterKnife.bind(this, view);
    }

    void setYesBtnListener(View.OnClickListener listener){
        yesBtn.setOnClickListener(listener);
    }

    void setNoBtnListener(View.OnClickListener listener){
        noBtn.setOnClickListener(listener);
    }

    void cancel() {
        alertDialog.cancel();
    }

    void show(){
        alertDialog = builder.create();
        alertDialog.show();
    }
}
