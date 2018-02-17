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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

import static com.hakanmehmed.trainapp.androidtrainapp.NotificationService.getDepartTime;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    private NotificationManager notificationManager;
    private final long updateInterval = 20000;
    private final Handler handler = new Handler();

    private final TrainFinderAPI api = new TrainFinderAPI();
    private final HashMap<Integer, Journey> subscribed = new HashMap<>();

    // uses journey ids to track notification text
    private final HashMap<Integer, String> journeyStatus = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            ArrayList<Journey> journeys = Utils.getSubscribedJourneys(context);
            for(int i = 0; i < journeys.size(); i++){
                Utils.setupAfterBoot(journeys.get(i), context);
            }
            Log.v(TAG, "Reboot setup.");
        } else {
//            Intent serviceIntent = new Intent(context, NotificationService.class);
//            Log.v(TAG, "onRecieve sevriceIntent");
//            serviceIntent.putExtra("journey", intent.getStringExtra("journey"));
//            serviceIntent.putExtra("dismiss", intent.getBooleanExtra("dismiss", false));
//
//            startWakefulService(context, serviceIntent);
            // TODO: API 18 makes notifications exact - decide what to use
            if(notificationManager == null) {
                notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            String json = intent.getExtras().getString("journey");
            Journey journey = Utils.jsonToJourney(json);
            if(journey == null) return;

            Log.v(TAG, "CHECK : " + journey.toString());
            Boolean unsubscribe = intent.getExtras().getBoolean("unsubscribe");
            if(unsubscribe){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_train_24dp)
                        .setContentTitle("Unsubscription")
                        .setContentText("Unsubscribed to journey :" + journey.getOrigin() + " to " + journey.getDestination())
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);

                notificationManager.notify(journey.getNotificationId(), builder.build());
                notificationManager.cancel(journey.getNotificationId());
                return;
            }

            subscribed.put(journey.getNotificationId(), journey);

            ApiQuery query = TrainFinderAPI.buildApiQuery(
                    StationUtils.getNameFromStationCode(journey.getOrigin()),
                    StationUtils.getNameFromStationCode(journey.getDestination()));

            startReminder(query, journey, context);
        }
    }

    private void startReminder(final ApiQuery query, Journey currentJourney, final Context context) {
        final Journey journey = subscribed.get(currentJourney.getNotificationId());
        if(journey == null) return; // not subscribed anymore

        Log.v(TAG, "Making API calls");

        api.getTrains(query, new CustomCallback<JourneySearchResponse>() {
            @Override
            public void onSuccess (Response<JourneySearchResponse> response) {
                List<Journey> journeys = response.body().getJourneys();
                if(journeys == null) return;
                for(Journey j : journeys){
                    if(j.equals(journey)){
                        Log.v(TAG, "FOUND ITTTTTTT");
                        buildNotification(j, journey.getNotificationId(), context);
                        remindAgain(query, journey);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                remindAgain(query, journey);
            }
        });
    }

    private void remindAgain(final ApiQuery query, final Journey savedJourney){

    }

    private void buildNotification(final Journey journey, int savedJourneyId, Context context){
        final Journey savedJourney = subscribed.get(savedJourneyId);
        if(savedJourney == null) return; // not subscribed anymore

        StringBuilder text = new StringBuilder();
        for(int i = 0; i < journey.getLegs().size(); i++){
            JourneyLeg leg = journey.getLegs().get(i);

            /* check if train is cancelled */
            if(leg.getCancelled()){
                text.append("Train from ")
                        .append(StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()))
                        .append(" cancelled!");
            }

            /* check if there's a generic "Delayed" with no given expected time */
            if(leg.getOrigin().getRealTimeStatus().equals("Delayed")){
                text.append("Train from ")
                        .append(StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()))
                        .append(" is delayed!");
            }

            /* check each train leg delay, will return "" if there is no delay */
            String delay = Utils.getTimeDifference(leg.getOrigin().getRealTime(), leg.getOrigin().getScheduledTime());
            if(!delay.equals("")){
                String prefix = "Train from " + leg.getOrigin().getStationCode() + " is delayed by ";
                String suffix = " (exp. " + Utils.formatTime(getDepartTime(leg.getOrigin())) + ")";
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
                //.addAction(R.drawable.ic_close_48px, "Dismiss", dismissPendingIntent)
                .setContentTitle("Train to " + StationUtils.getNameFromStationCode(journey.getDestination()))
                .setContentText(text.toString())
                .setSubText(journey.getOrigin() + " to " + journey.getDestination())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        String lastStatus = journeyStatus.get(savedJourneyId);
        if(lastStatus == null || !lastStatus.equals(text.toString())){
            builder.setVibrate(new long[] { 0, 250, 500, 250 });
            journeyStatus.put(savedJourneyId, text.toString());
        }

        notificationManager.notify(journey.getNotificationId(), builder.build());


    }
}
