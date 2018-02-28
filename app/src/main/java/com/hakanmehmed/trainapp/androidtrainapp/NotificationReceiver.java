package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    private NotificationManager notificationManager;
    private final long interval = 60000;
    private final Handler handler = new Handler();
    private final JourneyFinderApi api = new JourneyFinderApi();
    private final HashMap<Integer, Journey> subscribed = new HashMap<>(); // track journeys
    private final HashMap<Integer, String> journeyStatus = new HashMap<>(); // track notification text

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            ArrayList<Journey> journeys = Utils.getSubscribedJourneys(context);
            for(int i = 0; i < journeys.size(); i++){
                Utils.setupAfterBoot(journeys.get(i), context);
            }
            Log.v(TAG, "Reboot setup.");
        } else {
            if(notificationManager == null) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            String json = intent.getExtras().getString("journey");
            Journey journey = Utils.jsonToJourney(json);
            if(journey == null) return;

            Log.v(TAG, "CHECK : " + journey.toString());
            Boolean unsubscribe = intent.getExtras().getBoolean("unsubscribe");
            if(unsubscribe){
                subscribed.remove(journey.getNotificationId());
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_train_24dp)
                        .setContentTitle("Unsubscribe")
                        .setContentText("Unsubscribed from journey " + journey.getOrigin() +
                                " to " + journey.getDestination())
                        .setSubText(" running at " + Utils.formatTime(journey.getLegs().get(0).getOrigin().getScheduledTime()))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);

                notificationManager.notify(journey.getNotificationId(), builder.build());
                notificationManager.cancel(journey.getNotificationId());
                return;
            }

            subscribed.put(journey.getNotificationId(), journey);

            StationUtils.initStations(context);
            ApiQuery query = JourneyFinderApi.buildApiQuery(
                    StationUtils.getNameFromStationCode(journey.getOrigin()),
                    StationUtils.getNameFromStationCode(journey.getDestination()),
                    Utils.getCurrentTime());

            startReminder(query, journey, context);
        }
    }

    private void startReminder(final ApiQuery query, Journey currentJourney, final Context context) {
        final Journey journey = subscribed.get(currentJourney.getNotificationId());
        if(journey == null) return; // not subscribed anymore

        Log.v(TAG, "Making API calls");

        api.getJourneys(query, new CustomCallback<JourneySearchResponse>() {
            @Override
            public void onSuccess (Response<JourneySearchResponse> response) {
                List<Journey> journeys = response.body().getJourneys();
                if(journeys == null) return;
                for(Journey j : journeys){
                    if(j.equals(journey)){
                        if(Utils.isDateAfter(Utils.getCurrentTime(), journey.getDepartureDateTime())){
                            Log.v(TAG, "Stopped notification for " + journey.getNotificationId());
                            break;
                        }
                        buildNotification(j, journey.getNotificationId(), context);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startReminder(query, journey, context);
                            }
                        }, interval);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // try again after some time
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startReminder(query, journey, context);
                    }
                }, interval);
            }
        });
    }

    private void buildNotification(final Journey journey, int savedJourneyId, Context context){
        final Journey savedJourney = subscribed.get(savedJourneyId);
        if(savedJourney == null) return; // not subscribed anymore

        StringBuilder text = new StringBuilder();
        for(int i = 0; i < journey.getLegs().size(); i++){
            JourneyLeg leg = journey.getLegs().get(i);

            if(leg.getCancelled()){
                text.append("Train from ")
                        .append(StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()))
                        .append(" cancelled!");
            }

            if(leg.getOrigin().getRealTimeStatus().equals("Delayed")){
                text.append("Train from ")
                        .append(StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()))
                        .append(" is delayed!");
            }

            String delay = Utils.getTimeDifference(leg.getOrigin().getRealTime(), leg.getOrigin().getScheduledTime(), false);
            if(!delay.equals("")){
                String prefix = "Train from " + leg.getOrigin().getStationCode() + " is delayed by ";
                String suffix = " (exp. " + Utils.formatTime(Utils.getDepartTime(leg.getOrigin())) + ")";
                text.append(prefix).append(delay).append(suffix);
            }
        }

        if(text.toString().isEmpty()){
            // everything is fine
            text.append("Train appears to be on time!");
        }

        Intent moreDetailsIntent = new Intent(context, JourneyInformationActivity.class);
        moreDetailsIntent.putExtra("journey", Utils.journeyToJson(journey));
        moreDetailsIntent.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, journey.getNotificationId(),
                moreDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_train_24dp)
                .setContentTitle("Train to " + StationUtils.getNameFromStationCode(journey.getDestination()))
                .setContentText(text.toString())
                .setSubText(journey.getOrigin() + " to " + journey.getDestination())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        String lastStatus = journeyStatus.get(savedJourneyId);
        Log.v(TAG, "Current status " + text.toString());
        Log.v(TAG, "Last status " + lastStatus);
        if(lastStatus == null || !lastStatus.equals(text.toString())){
            builder.setVibrate(new long[] { 0, 250, 500, 250 });
            journeyStatus.put(savedJourneyId, text.toString());

            // send notification if there is any change of schedule
            notificationManager.notify(journey.getNotificationId(), builder.build());
        } else {
            Log.v(TAG, "Nothing has changed.");
        }
    }
}
