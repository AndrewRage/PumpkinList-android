package geekhub.activeshoplistapp.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.PurchaseActivity;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 4/3/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            Long dbId = intent.getExtras().getLong(AppConstants.EXTRA_LIST_ID, -1);
            if (dbId > 0) {
                showNotification(context, dbId);
            }
        }
    }

    private void showNotification(Context context, Long dbId) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("Appointment: " + dbId)
                //.setContentInfo("info")
                //.setTicker("ticker")
                ;
        Intent startIntent = new Intent(context, PurchaseActivity.class);
        startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, dbId);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(
                        context,
                        new Random().nextInt(),
                        startIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
    }

}
