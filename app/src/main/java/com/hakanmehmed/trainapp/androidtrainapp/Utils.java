package com.hakanmehmed.trainapp.androidtrainapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hakanmehmed on 14/02/2018.
 */

public class Utils {
    private static final String TAG = "Utils";

    static String formatTime(String time) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return new SimpleDateFormat("HH:mm").format(date);
    }

    static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return df.format(Calendar.getInstance().getTime());
    }
}
