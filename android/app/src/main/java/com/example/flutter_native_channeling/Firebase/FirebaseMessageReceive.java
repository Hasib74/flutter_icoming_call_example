package com.example.flutter_native_channeling.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.flutter_native_channeling.BuildConfig;
import com.example.flutter_native_channeling.Notification.NotificationActivity;
import com.example.flutter_native_channeling.R;
import com.example.flutter_native_channeling.Service.CallNotificationActionReceiver;
import com.example.flutter_native_channeling.Service.HeadsUpNotificationService;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceive extends FirebaseMessagingService {

    String TAG = "FirebaseMessageReceive";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        System.out.println("Notification message :: " + remoteMessage.getData().toString());
        System.out.println("Notification message :: " + remoteMessage.getData().toString().isEmpty());
        Log.d(TAG, "Notification From: " + remoteMessage.getFrom());


        if (!remoteMessage.getData().toString().isEmpty()) {

            System.out.println("Notification message ::  tiggered");
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("inititator", "test");
                    mBundle.putString("call_type", "normal");
                    serviceIntent.putExtras(mBundle);
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);


                    Thread.sleep(30000);


                    System.out.println("Notification After 30 sec");

                    getApplicationContext().stopService(new Intent(getApplicationContext(), HeadsUpNotificationService.class));
                    Intent istop = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    getApplicationContext().sendBroadcast(istop);

                }

                // startActivity();
               /* Intent intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("data_info", "testing");
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

            } catch (Exception err) {
                System.out.println("Notification error :: " + err.toString());

            }
        }


    }


    @Override
    public void onNewToken(@NonNull String s) {
        System.out.println("Notification token :: " + s);
        Log.d(TAG, "Notification Token: " + s);
        super.onNewToken(s);
    }


    private void startActivity() {

        //   Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
            assert notificationManager != null;

            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //  mChannel.setSound(sound, attributes);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

            builder.setSmallIcon(R.drawable.launch_background)
                    .setContentTitle(getString(R.string.hello_second_fragment))
                    .setContentText(getString(R.string.hello_second_fragment))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(openScreen(10
                    ), true)
                    .setAutoCancel(true)
                    .setOngoing(true);

            Notification notification = builder.build();
            notificationManager.notify(10, notification);
        } else {
            startActivity(new Intent(this, NotificationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    private PendingIntent openScreen(int notificationId) {
        Intent fullScreenIntent = new Intent(this, NotificationActivity.class);
        fullScreenIntent.putExtra("id", notificationId);
        return PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
