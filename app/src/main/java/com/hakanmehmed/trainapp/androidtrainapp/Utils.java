package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

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

    static void saveSearch(String from, String to, Context context){
        ArrayList<RecentSearch> recent = getSearches(context);

        if(recent.size() != 0) {
            if (recent.get(0).getFrom() != null && recent.get(0).getTo() != null
                    && recent.get(0).getFrom().equals(from)
                    && recent.get(0).getTo().equals(to)) return;
        }


        recent.add(0, new RecentSearch(from, to));

        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putString("recent_searches", new Gson().toJson(recent)).apply();
    }

    static ArrayList<RecentSearch> getSearches(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);

        if(prefs.getString("recent_searches", "default").equals("default")){
            return new ArrayList<>();
        }

        return new Gson().fromJson(prefs.getString("recent_searches", ""), new TypeToken<ArrayList<RecentSearch>>() {}.getType());
    }
}
