package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

public class NotificationService extends Service{
    private final String DISMISS_EVENT_NAME = "dismiss_event";
    private final String TAG = "NotificationService";

    private NotificationManager manager;
    private final long updateInterval = 20000;
    private final Handler handler = new Handler();

    private final TrainFinderAPI api = new TrainFinderAPI();
    private final HashMap<Integer, Journey> journeys = new HashMap<>();

    private final HashMap<Integer, String> notificationText = new HashMap<>();

    private final NotificationDismissReceiver dismissReceiver = new NotificationDismissReceiver();
    public class NotificationDismissReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            Journey journey = jsonToJourney(intent.getStringExtra("journey"));
            dismissNotification(journey);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        /* if our service has been started, this function (onStartCommand) will
           be called regardless if the service is already running */

        Log.v(TAG, "Journey id: " +
                String.valueOf(jsonToJourney(intent.getStringExtra("journey")).getNotificationId()));

        registerReceiver(dismissReceiver, new IntentFilter(DISMISS_EVENT_NAME));

        /* attempt to get our extras, if we can't, return */
        final Journey savedJourney = jsonToJourney(intent.getStringExtra("journey"));
        if(savedJourney == null) return Service.START_NOT_STICKY;

        if(intent.getBooleanExtra("dismiss", false)){
            dismissNotification(savedJourney);
            Log.d("NotificationService", "Told to dismiss notification with id" + String.valueOf(savedJourney.getNotificationId()));
            return Service.START_NOT_STICKY;
        }

        /* save journey in hash map */
        journeys.put(savedJourney.getNotificationId(), savedJourney);

        Log.d(TAG, "Updating notification for #" + String.valueOf(savedJourney.getNotificationId()));

        /* create the query for the journey we want to check on */
        ApiQuery query = TrainFinderAPI.buildApiQuery(
                StationUtils.getNameFromStationCode(savedJourney.getOrigin()),
                StationUtils.getNameFromStationCode(savedJourney.getDestination()));

        pollForUpdates(query, savedJourney);

        return Service.START_NOT_STICKY;
    }


    private void pollForUpdates(final ApiQuery query, final Journey savedJourney){
        /* attempt to get the journey, if it's null it means it was dismissed and we no
           longer need to poll for it */
        Journey journey = journeys.get(savedJourney.getNotificationId());
        if(journey == null) return;

        Log.d(TAG, "Making JourneyData request to update the notification");

        /* execute the query, callback will handle response */
        api.getTrains(query, new CustomCallback<JourneySearchResponse>() {
            @Override
            public void onSuccess (Response<JourneySearchResponse> response) {
                List<Journey> journeys = response.body().getJourneys();
                for(Journey j : journeys){
                    if(j.equals(savedJourney)){
                        buildNotification(j, savedJourney.getNotificationId());
                        postDelayed(query, savedJourney);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                postDelayed(query, savedJourney);
            }
        });
    }

    private void postDelayed(final ApiQuery query, final Journey savedJourney){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pollForUpdates(query, savedJourney);
            }
        }, updateInterval);
    }

    private void dismissNotification(Journey journey){
        journeys.remove(journey.getNotificationId());
        Log.v(TAG, "Dismiss called for #" + String.valueOf(journey.getNotificationId()));
        manager.cancel(journey.getNotificationId());


        Log.v(TAG, "Journeys left to poll for: " + String.valueOf(journeys.size()));

        if(journeys.size() == 0){
            Log.v(TAG, "Halting the service");
            stopSelf();
        }
    }

    private void buildNotification(final Journey journey, int savedJourneyId){
        final Journey savedJourney = journeys.get(savedJourneyId);
        if(savedJourney == null) return;

        Log.d(TAG, "Building notification for #" + String.valueOf(savedJourney.getNotificationId()));

        /* our notification text, build up as we check response */
        String text = "";

        /* check every leg/change of the journey */
        for(int i = 0; i < journey.getLegs().size(); i++){
            JourneyLeg leg = journey.getLegs().get(i);

            /* check if train is cancelled */
            if(leg.getCancelled()){
                text += "Train from " + StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()) + " cancelled!";
            }

            /* check if there's a generic "Delayed" with no given expected time */
            if(leg.getOrigin().getRealTimeStatus().equals("Delayed")){
                text += "Train from " + StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode()) + " is delayed!";
            }

            /* check each train leg delay, will return "" if there is no delay */
            String delay = Utils.getTimeDifference(leg.getOrigin().getRealTime(), leg.getOrigin().getScheduledTime());
            if(!delay.equals("")){
                String prefix = "Train from " + leg.getOrigin().getStationCode() + " is delayed by ";
                String suffix = " (exp. " + formatTime(getDepartTime(leg.getOrigin())) + ")";
                text += prefix + delay + suffix;
            }
        }

        /* if our text hasn't changed, the train is as scheduled */
        if(text.isEmpty()) text = "Train appears to be on time!";

        /* create the dismiss intent */
        Intent dismissIntent = new Intent(DISMISS_EVENT_NAME);
        dismissIntent.putExtra("journey", journeyToJson(savedJourney));
        dismissIntent.putExtra("dismiss", true);

        /* create the pending intent for the notification */
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, savedJourney.getNotificationId(), dismissIntent, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_train_24dp)
                .setOngoing(true)
                .addAction(R.drawable.ic_close_48px, "Dismiss", dismissPendingIntent)
                .setSubText(savedJourney.getOrigin() + " to " + savedJourney.getDestination())
                .setContentTitle("Train to " + StationUtils.getNameFromStationCode(savedJourney.getDestination()))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        /* when the user clicks the notification, it'll open up an activity giving a full updated
           view of the journey, we pass on the journey information through an intent */
        Intent detailsIntent = new Intent(getApplicationContext(), JourneyInformationActivity.class);
        detailsIntent.putExtra("journey", journeyToJson(journey));

        /* dummy action is necessary to stop extras being dropped: http://stackoverflow.com/a/3168653/5631268 */
        detailsIntent.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent detailsPending = PendingIntent.getActivity(getApplicationContext(), savedJourneyId, detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(detailsPending);

        /* if the last stored version of our notification text is null it means we haven't saved it
           and therefore haven't shown a notification - so we save the text and vibrate with the
           the notification, we also do this if the text content has changed so the user can be
           alerted */
        String lastNotificationText = notificationText.get(savedJourneyId);
        if(lastNotificationText == null || !lastNotificationText.equals(text)){
            builder.setVibrate(new long[] { 0, 250, 500, 250 });
            notificationText.put(savedJourneyId, text);
        }

        /* use the manager to update the current notification */
        manager.notify(savedJourneyId, builder.build());
    }

    public static String formatTime(String time){
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

    public static String getDepartTime(Origin origin){
        if(origin.getRealTime() == null) {
            return origin.getScheduledTime();
        } else {
            return origin.getRealTime();
        }
    }

    private String journeyToJson(Journey j){
        return new Gson().toJson(j);
    }
    private Journey jsonToJourney(String json) {
        return new Gson().fromJson(json, Journey.class);
    }


    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "Service created.");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(dismissReceiver);
        Log.d(TAG, "Service destroyed.");
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent){
        return null;
    }
}
