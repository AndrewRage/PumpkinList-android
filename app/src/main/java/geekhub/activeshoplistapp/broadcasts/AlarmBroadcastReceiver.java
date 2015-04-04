package geekhub.activeshoplistapp.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.PurchaseActivity;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
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
                //showNotification(context, dbId);
                Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + dbId);
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
                };
                Cursor cursor = context.getContentResolver().query(
                        uri,
                        projection,
                        null,
                        null,
                        null
                );
                PurchaseListModel list = ContentHelper.getPurchaseList(cursor);
                showNotification(context, list);
            }
        }
    }

    private void showNotification(Context context, PurchaseListModel list) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("Appointment: " + list.getListName())
                        //.setContentInfo("info")
                        //.setTicker("ticker")
                ;
        Intent startIntent = new Intent(context, PurchaseActivity.class);
        startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, list.getDbId());
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
