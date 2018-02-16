package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
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

    static String getTimeDifference(String departureTime, String arrivalTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if(departureTime == null || arrivalTime == null) return "";
        try {

            Date departureDate = format.parse(departureTime);
            Date arrivalDate = format.parse(arrivalTime);

            long difference = arrivalDate.getTime() - departureDate.getTime();

            long hours = TimeUnit.MILLISECONDS.toHours(difference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference));

            String strHours = String.valueOf(hours);
            String strMinutes = String.valueOf(minutes);

            String delayText = "";
            if(hours > 0){
                delayText += strHours + (hours > 1 ? " hours " : " hour ");
            }

            if(minutes > 0){
                delayText += strMinutes + (minutes > 1 ? " minutes" : " minute");
            }

            return delayText;
        } catch(ParseException e){
            return "";
        }
    }

    // ----------------- SUBSCRIBED JOURNEYS ------------------------

    public static void subscribedToJourney(Journey journey, Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        /* generate a random id to associate with the journey for alarms and notifications */
        int rand = (int) (Math.random() * 1000000);
        journey.setNotificationId(rand);
        android.util.Log.v(TAG, "Generated unique random id: " + rand);

        // TODO : ADD if not added before
        ArrayList<Journey> currentSubscribedJourneys = getSubscribedJourneys(context);
        currentSubscribedJourneys.add(0, journey);

        prefs.edit().putString("subscribed_journeys", new Gson().toJson(currentSubscribedJourneys)).apply();
        setupSubscription(journey, context);
    }

    public static void setupSubscription(Journey journey, Context context){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("journey", journeyToJson(journey));
        intent.putExtra("dismiss", false);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, journey.getNotificationId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String scheduledDepartureTime = journey.getLegs().get(0).getOrigin().getScheduledTime();

        try {
            Date departTime = simpleDateFormat.parse(scheduledDepartureTime);
            Calendar c = Calendar.getInstance();
            c.setTime(departTime);
            c.add(Calendar.MINUTE, -journey.getReminder());

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // will trigger even if device goes to sleep mode
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.v(TAG, "Notification is setup.");
        } catch(ParseException e){
            e.printStackTrace();
        }
    }

    private static String journeyToJson(Journey j){
        return new Gson().toJson(j);
    }

    public static ArrayList<Journey> getSubscribedJourneys(Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        return new Gson().fromJson(prefs.getString("subscribed_journeys", "[]"),
                new TypeToken<ArrayList<Journey>>() {}.getType());
    }

    public static void unsubscribeJourney(int index, Context context) {
        ArrayList<Journey> journeys = getSubscribedJourneys(context);
        //TODO: removeSubscription(journeys.get(index), context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        journeys.remove(index);

        prefs.edit().putString("subscribed_journeys", new Gson().toJson(journeys)).apply();
    }

    public static void removeSubscription(Journey journey, Context context){
        Intent intent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("journey", journeyToJson(journey));
        intent.putExtra("dismiss", true);

        context.sendBroadcast(intent);

        Log.v(TAG, "Remove notification with id" + String.valueOf(journey.getId()));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, journey.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
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
