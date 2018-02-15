package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    // ----------------- SUBSCRIBED JOURNEYS ------------------------

    public static void subscribedToJourney(Journey journey, Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        /* generate a random id to associate with the journey for alarms and notifications */
        journey.setId((int)(Math.random() * 10000));

        ArrayList<Journey> currentSubscribedJourneys = getSubscribedJourneys(context);
        currentSubscribedJourneys.add(0, journey);

        prefs.edit().putString("subscribed_journeys", new Gson().toJson(currentSubscribedJourneys)).apply();
        //NotificationReceiver.setupPolling(journey, context);
    }

    public static ArrayList<Journey> getSubscribedJourneys(Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        return new Gson().fromJson(prefs.getString("subscribed_journeys", "[]"),
                new TypeToken<ArrayList<Journey>>() {}.getType());
    }

    public static void unsubscribeJourney(int index, Context context) {
        ArrayList<Journey> journeys = getSubscribedJourneys(context);
        //NotificationReceiver.removePolling(journeys.get(index), context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        journeys.remove(index);

        prefs.edit().putString("subscribed_journeys", new Gson().toJson(journeys)).apply();
    }


    // ----------------- RECENT SEARCHES -----------------------------
    static void saveSearch(String from, String to, Context context){
        ArrayList<RecentSearch> recentSearches = getSearches(context);
        RecentSearch newSearch = new RecentSearch(from, to);
        if(recentSearches.size() != 0) {
            if(recentSearches.contains(newSearch)){
                recentSearches.remove(recentSearches.indexOf(newSearch));
            }
        }

        recentSearches.add(0, newSearch);

        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putString("recent_searches", new Gson().toJson(recentSearches)).apply();
    }

    static ArrayList<RecentSearch> getSearches(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);

        if(prefs.getString("recent_searches", "default").equals("default")){
            return new ArrayList<>();
        }

        return new Gson().fromJson(prefs.getString("recent_searches", ""), new TypeToken<ArrayList<RecentSearch>>() {}.getType());
    }
}
