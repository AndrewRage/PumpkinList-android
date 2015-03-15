package geekhub.activeshoplistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.GpsAppointmentModel;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 3/15/15.
 */
public class GpsAppointment {
    private static final String TAG = GpsAppointment.class.getSimpleName();
    private static final int UPDATE_TIME_NETWORK = 1000 * 60;
    private static final int UPDATE_TIME_GPS = 1000 * 10;
    private static final int UPDATE_PLACE_NETWORK = 10;
    private static final int UPDATE_PLACE_GPS = 10;
    private static final int STATIC_RADIUS = 100;
    private Context context;
    private LocationManager locationManager;
    private List<GpsAppointmentModel> appointmentList;

    public GpsAppointment(Context context) {
        this.context = context;
        appointmentList = new ArrayList<>();
    }

    public void update() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, networkListener);
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, passiveListener);
        }

        appointmentList.clear();
        for (PurchaseListModel purchaseItem : ShoppingHelper.getInstance().getPurchaseLists()) {
            if (!purchaseItem.isDone() && purchaseItem.getPlaceId() != 0) {
                Location placeLocation = new Location("locA");
                for (PlacesModel placeItem : ShoppingHelper.getInstance().getPlacesList()) {
                    if (purchaseItem.getPlaceId() > 0) {
                        if (purchaseItem.getPlaceId() == placeItem.getServerId()) {
                            placeLocation.setLatitude(placeItem.getGpsLatitude());
                            placeLocation.setLongitude(placeItem.getGpsLongitude());
                        }
                    }
                    if (purchaseItem.getPlaceId() < 0) {
                        if (purchaseItem.getPlaceId() == (placeItem.getDbId() * (-1))) {
                            placeLocation.setLatitude(placeItem.getGpsLatitude());
                            placeLocation.setLongitude(placeItem.getGpsLongitude());
                        }
                    }
                }
                GpsAppointmentModel appointmentModel = new GpsAppointmentModel(
                        purchaseItem.getDbId(),
                        placeLocation.getLatitude(),
                        placeLocation.getLongitude(),
                        0,
                        false
                );
                appointmentList.add(appointmentModel);
            }
            if (!purchaseItem.isDone() && purchaseItem.getShopId() != 0) {
                Location shopLocation = new Location("locA");
                for (PlacesModel placeItem : ShoppingHelper.getInstance().getPlacesList()) {
                    if (purchaseItem.getShopId() > 0) {
                        if (purchaseItem.getPlaceId() == placeItem.getServerId()) {
                            shopLocation.setLatitude(placeItem.getGpsLatitude());
                            shopLocation.setLongitude(placeItem.getGpsLongitude());
                        }
                    }
                    if (purchaseItem.getShopId() < 0) {
                        if (purchaseItem.getPlaceId() == (placeItem.getDbId() * (-1))) {
                            shopLocation.setLatitude(placeItem.getGpsLatitude());
                            shopLocation.setLongitude(placeItem.getGpsLongitude());
                        }
                    }
                }
                GpsAppointmentModel appointmentModel = new GpsAppointmentModel(
                        purchaseItem.getDbId(),
                        shopLocation.getLatitude(),
                        shopLocation.getLongitude(),
                        0,
                        false
                );
                appointmentList.add(appointmentModel);
            }
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
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
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
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
    }

    private Location midPoint(Location location1, Location location2) {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();

        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        Location location = new Location("locA");
        location.setLatitude(Math.toDegrees(lat3));
        location.setLongitude(Math.toDegrees(lon3));
        return location;
    }
}
