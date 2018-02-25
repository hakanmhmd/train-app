package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
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

    static String formatTime(String time){
        Date date;
        try{
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(time);
        } catch(ParseException e){
            e.printStackTrace();
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return new SimpleDateFormat("HH:mm").format(cal.getTime());
    }

    static final String formatDate(String date){
        Date d;
        try{
            d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
        } catch(ParseException e){
            e.printStackTrace();
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }

    static long getMilliseconds(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = format.parse(dateString);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    static String getTimeDifference(String time1String, String time2String, boolean shorten) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if(time2String == null || time1String == null) return "";
        try {
            Date time1Date = format.parse(time1String);
            Date time2Date = format.parse(time2String);
            long difference;
            if(time1Date.after(time2Date)){
                difference = time1Date.getTime() - time2Date.getTime();
            } else {
                difference = time2Date.getTime() - time1Date.getTime();
            }

            long hours = TimeUnit.MILLISECONDS.toHours(difference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference));

            String strHours = String.valueOf(hours);
            String strMinutes = String.valueOf(minutes);

            String delayText = "";
            if(hours > 0){
                if(shorten) {
                    delayText += strHours + "h ";
                } else {
                    delayText += strHours + (hours > 1 ? " hours " : " hour ");
                }
            }

            if(minutes > 0){
                if(shorten){
                    delayText += strMinutes + "m ";
                } else {
                    delayText += strMinutes + (minutes > 1 ? " minutes" : " minute");
                }
            }

            return delayText;
        } catch(ParseException e){
            Log.v(TAG, "Could not parse");
            return "";
        }
    }

    static boolean isSameTime(String time1String, String time2String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (time1String == null || time2String == null) return false;
        try {
            Date time1Date = format.parse(time1String);
            Date time2Date = format.parse(time2String);
            if (time1Date.compareTo(time2Date) == 0) {
                return true;
            } else {
                if(!time2Date.after(time1Date)) {
                    return true;
                }
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    static String getDepartTime(Origin origin){
        if(origin.getRealTime() == null) {
            return origin.getScheduledTime();
        } else {
            return origin.getRealTime();
        }
    }

    static String getArriveTime(Destination destination){
        if(destination.getRealTime() == null) {
            return destination.getScheduledTime();
        } else {
            return destination.getRealTime();
        }
    }

    static String journeyToJson(Journey j){
        return new Gson().toJson(j);
    }

    static Journey jsonToJourney(String json){
        return new Gson().fromJson(json, Journey.class);
    }

    // ----------------- SUBSCRIBED JOURNEYS ------------------------

    public static void subscribedToJourney(Journey journey, Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        /* generate a random id to associate with the journey for alarms and notifications */
        //int rand = (int) (Math.random() * 1000000);
        int rand = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        journey.setNotificationId(rand);
        android.util.Log.v(TAG, "Generated unique random id: " + rand);

        ArrayList<Journey> currentSubscribedJourneys = getSubscribedJourneys(context);
        if(!currentSubscribedJourneys.contains(journey)) {
            currentSubscribedJourneys.add(0, journey);
        } else {
            int index = currentSubscribedJourneys.indexOf(journey);
            currentSubscribedJourneys.remove(index);
            currentSubscribedJourneys.add(0, journey);
        }
        prefs.edit().putString("subscribed_journeys", new Gson().toJson(currentSubscribedJourneys)).apply();
        setupSubscription(journey, context);
    }

    public static void setupAfterBoot(Journey journey, Context context){
        int rand = (int) (Math.random() * 1000000);
        journey.setNotificationId(rand);
        setupSubscription(journey, context);
    }

    public static void setupSubscription(Journey journey, Context context){
        Intent intent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("journey", journeyToJson(journey));
        intent.putExtra("unsubscribe", false);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, journey.getNotificationId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String scheduledDepartureTime = journey.getLegs().get(0).getOrigin().getScheduledTime();

        try {
            Date departTime = simpleDateFormat.parse(scheduledDepartureTime);
            Log.v(TAG, departTime.toString());
            Calendar c = Calendar.getInstance();
            c.setTime(departTime);
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - journey.getReminder());
            c.set(Calendar.SECOND, c.get(Calendar.SECOND));

            Log.v(TAG, c.get(Calendar.HOUR_OF_DAY) + " " + c.get(Calendar.MINUTE) + " " + c.get(Calendar.SECOND));
            //c.add(Calendar.MINUTE, -journey.getReminder());
            //c.set(Calendar.HOUR_OF_DAY, 22);
            ///c.set(Calendar.MINUTE, journey.getReminder());
            //c.set(Calendar.SECOND, 10);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // will trigger even if device goes to sleep mode
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.v(TAG, "Notification is setup.");
        } catch(ParseException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<Journey> getSubscribedJourneys(Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        return new Gson().fromJson(prefs.getString("subscribed_journeys", "[]"),
                new TypeToken<ArrayList<Journey>>() {}.getType());
    }

    public static void unsubscribeJourney(int index, Context context) {
        ArrayList<Journey> journeys = getSubscribedJourneys(context);
        removeSubscription(journeys.get(index), context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        journeys.remove(index);

        prefs.edit().putString("subscribed_journeys", new Gson().toJson(journeys)).apply();
    }

    public static void removeSubscription(Journey journey, Context context){
        Intent intent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("journey", journeyToJson(journey));
        intent.putExtra("unsubscribe", true);

        context.sendBroadcast(intent);
        Log.v(TAG, "Remove notification with id " + String.valueOf(journey.getId()));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, journey.getNotificationId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
