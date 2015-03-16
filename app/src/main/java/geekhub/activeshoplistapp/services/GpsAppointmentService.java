package geekhub.activeshoplistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
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
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
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
        Log.d(TAG, "onDestroy");
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
        List<Long> appointmentLists = ShoppingHelper.newInstance(getApplicationContext()).getAppointmentLists();
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
        }
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
