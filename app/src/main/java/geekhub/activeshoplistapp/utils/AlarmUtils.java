package geekhub.activeshoplistapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import geekhub.activeshoplistapp.broadcasts.AlarmBroadcastReceiver;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 4/4/15.
 */
public class AlarmUtils {
    private Context context;

    public AlarmUtils(Context context) {
        this.context = context;
    }

    public void setListAlarm(PurchaseListModel list) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getDbId());
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                list.getTimeAlarm(),
                pendingIntent
        );
    }

    public void cancelListAlarm(PurchaseListModel list) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getDbId());
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.cancel(pendingIntent);
    }
}
