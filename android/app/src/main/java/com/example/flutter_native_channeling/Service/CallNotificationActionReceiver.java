package com.example.flutter_native_channeling.Service;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.example.flutter_native_channeling.Notification.NotificationActivity;

public class CallNotificationActionReceiver extends BroadcastReceiver {


    Context mContext;
    int _notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;


        _notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        if (intent != null && intent.getExtras() != null) {

            String action = "";
            action = intent.getStringExtra("ACTION_TYPE");

            if (action != null && !action.equalsIgnoreCase("")) {
                performClickAction(context, action);
            }

            // Close the notification after the click action is performed.
            Intent iclose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(iclose);
            context.stopService(new Intent(context, NotificationActivity.class));

        }


    }

    private void performClickAction(Context context, String action) {


        System.out.println("Notification Action is : " + action);
        System.out.println("Notification Permission : " + checkAppPermissions());
        if (action.equalsIgnoreCase("RECEIVE_CALL")) {


            if (/*checkAppPermissions()*/ 1 == 1) {

                cancelNotification(context, _notificationId);


                Intent intentCallReceive = new Intent(mContext, NotificationActivity.class);
                intentCallReceive.putExtra("Call", "incoming");
                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentCallReceive);
            } else {

                cancelNotification(context, _notificationId);

                Intent intent = new Intent(this.mContext, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("CallFrom", "call from push");
                mContext.startActivity(intent);

            }
        } else if (action.equalsIgnoreCase("DIALOG_CALL")) {
            //  cancelNotification(context, _notificationId);
            // show ringing activity when phone is locked
            Intent intent = new Intent(this.mContext, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        } else {
            cancelNotification(context, _notificationId);
            mContext.stopService(new Intent(context, NotificationActivity.class));
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(it);
        }
    }

    private Boolean checkAppPermissions() {
        return hasReadPermissions() && hasWritePermissions() && hasCameraPermissions() && hasAudioPermissions();
    }

    private boolean hasAudioPermissions() {
        return (ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }


    public static void cancelNotification(Context ctx, int notifyId) {

        System.out.println("Notification cancel id : " + notifyId);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);

        ctx.stopService(new Intent(ctx, HeadsUpNotificationService.class));
        Intent istop = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        ctx.sendBroadcast(istop);
    }
}