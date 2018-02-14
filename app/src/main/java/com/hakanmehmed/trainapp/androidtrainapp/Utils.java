package com.hakanmehmed.trainapp.androidtrainapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    static String getDifference(String departureTime, String arrivalTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date departureDate = format.parse(departureTime);
            Date arrivalDate = format.parse(arrivalTime);

            long difference = arrivalDate.getTime() - departureDate.getTime();

            long hours = TimeUnit.MILLISECONDS.toHours(difference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference));

            return (hours > 0 ? hours + "h " : "") + minutes + "m";


        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }
}
