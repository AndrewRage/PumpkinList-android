package geekhub.activeshoplistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 3/14/15.
 */
public class GpsAppointmentService extends Service {
    private static final String TAG = GpsAppointmentService.class.getSimpleName();
    private static final int UPDATE_TIME_NETWORK = 0;
    private static final int UPDATE_TIME_GPS = 1000 * 10;
    private static final int UPDATE_PLACE_NETWORK = 0;
    private static final int UPDATE_PLACE_GPS = 10;
    private static final int GPS_RADIUS = 1500;
    private static final int MIN_RADIUS = 100;
    private static final int APPOINTMENT_RADIUS = 150;
    private Handler handler;
    private LocationManager locationManager;
    private List<PurchaseListModel> purchaseLists;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        purchaseLists = new ArrayList<>();
        handler = new Handler();
        getContentResolver().registerContentObserver(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                true,
                contentObserver
        );
        readPurchaseList();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, networkListener);
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, passiveListener);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(contentObserver);
        Log.d(TAG, "onDestroy");
    }

    private ContentObserver contentObserver = new ContentObserver(handler) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            readPurchaseList();
        }
    };

    private void readPurchaseList() {
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
                SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        String[] args = new String[]{"0", "0"};
        String selection = SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID + "!=? OR "
                + SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID + "!=?";
        Cursor cursor = getContentResolver().query(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                projection,
                selection,
                args,
                null
        );

        purchaseLists.clear();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID);
            int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
            int indexUser = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID);
            int indexShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID);
            int indexIsUserShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP);
            int indexPlace = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID);
            int indexIsUserPlace = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE);
            int indexDone = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE);
            int indexMaxDistance = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE);
            int indexIsAlarm = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM);
            int indexAlarm = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM);
            int indexCreate = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP);
            PurchaseListModel listModel = new PurchaseListModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getString(indexName),
                    cursor.getInt(indexUser),
                    cursor.getInt(indexShop),
                    cursor.getInt(indexIsUserShop)>0,
                    cursor.getInt(indexPlace),
                    cursor.getInt(indexIsUserPlace)>0,
                    cursor.getInt(indexDone)>0,
                    cursor.getFloat(indexMaxDistance),
                    cursor.getInt(indexIsAlarm)>0,
                    cursor.getLong(indexAlarm),
                    cursor.getLong(indexCreate),
                    cursor.getLong(indexTimestamp),
                    null
            );
            purchaseLists.add(listModel);
            cursor.moveToNext();
        }
        cursor.close();

        Log.d(TAG, "purchaseList.size = " + purchaseLists.size());

        if (purchaseLists.size() == 0) {
            stopSelf();
            Log.d(TAG, "serviceStop");
        }
    }

    private LocationListener passiveListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationAction(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void locationAction(Location location) {
        Log.d(TAG, "locationAction: " + location.getProvider());
        /*List<Long> appointmentLists = ShoppingHelper.newInstance(getApplicationContext()).getAppointmentLists();
        if (appointmentLists.size() == 0) {
            stopSelf();
            Log.d(TAG, "serviceStop");
        } else {
            for (long dbId : appointmentLists) {
                PurchaseListModel list = ShoppingHelper.getInstance().getPurchaseListByDbId(dbId);
                float distance = location.distanceTo(list.getPoint());
                if (list.getMaxDistance() < distance && distance > MIN_RADIUS) {
                    ShoppingHelper.getInstance().updatePurchaseListMaxDistance(list.getDbId(), distance);
                } else if (list.getMaxDistance() / 2 > distance) {
                    showNotification("/2 distance: " + list.getListName());
                    ShoppingHelper.getInstance().updatePurchaseListMaxDistance(list.getDbId(), 0);
                    //ShoppingHelper.getInstance().updatePurchaseListIsAlarm(list.getDbId(), false);
                }
                if (!list.isAlarm() && distance < list.getRadius() + APPOINTMENT_RADIUS) {
                    showNotification("Appointment distance: " + list.getListName());
                    ShoppingHelper.getInstance().updatePurchaseListIsAlarm(list.getDbId(), true);
                }
            }
        }*/
    }

    private void showNotification(String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(GpsAppointmentService.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(text)
                //.setContentInfo("" + count)
                //.setTicker(getString(R.string.notification_new_feeds))
                ;
        /*Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(ReaderService.this, 0, startIntent, 0);
        builder.setContentIntent(pendingIntent);*/
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
    }
}
