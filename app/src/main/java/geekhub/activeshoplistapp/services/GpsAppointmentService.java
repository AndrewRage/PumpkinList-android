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

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.GpsAppointmentModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 3/14/15.
 */
public class GpsAppointmentService extends Service {
    private static final String TAG = GpsAppointmentService.class.getSimpleName();
    private static final int UPDATE_TIME_NETWORK = 1000 * 60;
    private static final int UPDATE_TIME_GPS = 1000 * 10;
    private static final int UPDATE_PLACE_NETWORK = 10;
    private static final int UPDATE_PLACE_GPS = 10;
    private static final int STATIC_RADIUS = 100;
    private LocationManager locationManager;
    private List<PurchaseListModel> appointmentLists;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        appointmentLists = ShoppingHelper.newInstance(getApplicationContext()).getAppointmentLists();
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
        for (GpsAppointmentModel item : appointmentList) {
            Location place = new Location("locA");
            place.setLatitude(item.getLatitude());
            place.setLongitude(item.getLongitude());
            float distance = location.distanceTo(place);
            /*if (distance < 1000) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, UPDATE_TIME_GPS, UPDATE_PLACE_GPS, gpsListener);
            } else {
                locationManager.removeUpdates(gpsListener);
            }*/
            if (item.getMaxDistance() < distance && distance > STATIC_RADIUS) {
                item.setMaxDistance(distance);
            } else if (item.getMaxDistance() / 2 > distance) {
                showNotification("id: " + item.getPurchaseId());
                item.setMaxDistance(0);
            }
            if (!item.isAlert() && distance < STATIC_RADIUS) {
                showNotification("id: " + item.getPurchaseId());
                item.setIsStartAlertPosition(true);
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
