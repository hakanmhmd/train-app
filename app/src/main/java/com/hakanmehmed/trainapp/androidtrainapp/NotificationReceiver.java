package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by hakanmehmed on 15/02/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

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
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent moreDetailsIntent = new Intent(context, JourneyInformationActivity.class);
            moreDetailsIntent.setAction(Long.toString(System.currentTimeMillis()));


            String strObj = intent.getExtras().getString("journey");
            Journey journey = new Gson().fromJson(strObj, Journey.class);
            Log.v(TAG, "CHECK : " + journey.toString());
            Boolean unsub = intent.getExtras().getBoolean("unsubscribe");
            if(unsub){
                PendingIntent pendingIntent = PendingIntent.getActivity(context, journey.getNotificationId(),
                        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        //.setContentIntent(pendingIntent)
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

            PendingIntent pendingIntent = PendingIntent.getActivity(context, journey.getNotificationId(),
                    moreDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_train_24dp)
                    .setContentTitle("Train to " + StationUtils.getNameFromStationCode(journey.getDestination()))
                    .setContentText("SOME TEXT HERE")
                    .setSubText(journey.getOrigin() + " to " + journey.getDestination())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            notificationManager.notify(journey.getNotificationId(), builder.build());

        }
    }

//    public static void setupSubscription(Journey journey, Context context){
//        Intent intent = new Intent(context, NotificationReceiver.class);
//        intent.putExtra("journey", journeyToJson(journey));
//        intent.putExtra("dismiss", false);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, journey.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        String scheduledDepartureTime = journey.getLegs().get(0).getOrigin().getScheduledTime();
//
//        try {
//            Date departTime = simpleDateFormat.parse(scheduledDepartureTime);
//            Calendar c = Calendar.getInstance();
//            c.setTime(departTime);
//            c.add(Calendar.MINUTE, -journey.getReminder());
//
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            // will trigger even if device goes to sleep mode
//            Log.v(TAG, "Time is " + c.getTimeInMillis());
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//            Log.v(TAG, "Notification is setup.");
//        } catch(ParseException e){
//           e.printStackTrace();
//        }
//    }




}
