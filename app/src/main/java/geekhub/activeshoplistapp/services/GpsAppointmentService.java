package geekhub.activeshoplistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
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
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.utils.Coordinate;

/**
 * Created by rage on 3/14/15.
 */
public class GpsAppointmentService extends Service {
    private static final String TAG = GpsAppointmentService.class.getSimpleName();
    private static final int UPDATE_TIME_NETWORK = 1000;
    private static final int UPDATE_TIME_GPS = 1000;
    private static final int UPDATE_PLACE_NETWORK = 0;
    private static final int UPDATE_PLACE_GPS = 10;
    private static final int GPS_PASIVE_TIME = 1000 * 60;
    private static final int GPS_RADIUS = 2000;
    private static final int MIN_RADIUS = 150;
    private static final int APPOINTMENT_RADIUS = 300;
    private static final int LOCATION_CHECK_SIZE = 3;
    private static final int LOCATION_CHECK_MAX_DISTANCE = 5000;
    private Handler handler;
    private LocationManager locationManager;
    private List<PurchaseListModel> purchaseLists;
    private List<Location> locationList;
    private boolean isGps;
    private int ringCount = 0;
    private Location latencyLocation;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        locationList = new ArrayList<>();
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
        Log.d(TAG, "onStartCommand");
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, passiveListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, networkListener);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, gpsListener);
            //startGps();
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
        {
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
                    SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_SHOP_DISTANCE,
                    SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_PLACE_DISTANCE,
                    SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_POINT_DISTANCE,
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
                int indexMaxShopDistance = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_SHOP_DISTANCE);
                int indexMaxPlaceDistance = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_PLACE_DISTANCE);
                int indexMaxPointDistance = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_POINT_DISTANCE);
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
                        cursor.getInt(indexIsUserShop) > 0,
                        cursor.getInt(indexPlace),
                        cursor.getInt(indexIsUserPlace) > 0,
                        cursor.getInt(indexDone) > 0,
                        cursor.getInt(indexIsAlarm) > 0,
                        cursor.getLong(indexAlarm),
                        cursor.getLong(indexCreate),
                        cursor.getLong(indexTimestamp),
                        null
                );
                listModel.setMaxShopDistance(cursor.getFloat(indexMaxShopDistance));
                listModel.setMaxPlaceDistance(cursor.getFloat(indexMaxPlaceDistance));
                listModel.setMaxPointDistance(cursor.getFloat(indexMaxPointDistance));
                purchaseLists.add(listModel);
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "purchaseList.size = " + purchaseLists.size());

        for (int i = 0; i < purchaseLists.size(); i++) {
            Location point = null;
            Location shop = null;
            Location place = null;

            if (purchaseLists.get(i).getShopId() != 0) {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                };
                StringBuilder selectionBuilder = new StringBuilder();
                if (purchaseLists.get(i).isUserShop()) {
                    selectionBuilder.append(SqlDbHelper.COLUMN_ID);
                } else {
                    selectionBuilder.append(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
                }
                selectionBuilder.append("=?");
                String selection = selectionBuilder.toString();
                String[] args = new String[]{Long.toString(purchaseLists.get(i).getShopId())};
                Cursor cursor = getContentResolver().query(
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        selection,
                        args,
                        null
                );
                cursor.moveToFirst();
                int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
                int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
                shop = new Location(AppConstants.LOCATION);
                shop.setLatitude(cursor.getDouble(indexLatitude));
                shop.setLongitude(cursor.getDouble(indexLongitude));
                cursor.close();
            }

            if (purchaseLists.get(i).getPlaceId() != 0) {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                };
                StringBuilder selectionBuilder = new StringBuilder();
                if (purchaseLists.get(i).isUserPlace()) {
                    selectionBuilder.append(SqlDbHelper.COLUMN_ID);
                } else {
                    selectionBuilder.append(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
                }
                selectionBuilder.append("=?");
                String selection = selectionBuilder.toString();
                String[] args = new String[]{Long.toString(purchaseLists.get(i).getPlaceId())};
                Cursor cursor = getContentResolver().query(
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        selection,
                        args,
                        null
                );
                cursor.moveToFirst();
                int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
                int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
                place = new Location(AppConstants.LOCATION);
                place.setLatitude(cursor.getDouble(indexLatitude));
                place.setLongitude(cursor.getDouble(indexLongitude));
                cursor.close();
            }

            if (shop != null && place != null) {
                point = Coordinate.middlePoint(shop, place);
                purchaseLists.get(i).setPointRadius(point.distanceTo(place));
            } else if (shop != null){
                point = shop;
            } else if (place != null){
                point = place;
            }

            purchaseLists.get(i).setPointLocation(point);
        }

        if (purchaseLists.size() == 0) {
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);
            locationManager.removeUpdates(passiveListener);
            locationManager = null;
            handler = null;
            purchaseLists = null;
            stopSelf();
            Log.d(TAG, "serviceStop");
        }
    }

    private LocationListener passiveListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Log.d(TAG, "passiveListener");
            //locationAction(location);
            checkLocation(location);
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
            //Log.d(TAG, "networkListener");
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
            //Log.d(TAG, "gpsListener");
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

    private void checkLocation(Location location) {
        //Log.d(TAG, "checkLocation: " + location.getProvider() + ", time: " + location.getTime());
        if (!isGps | (isGps && location.getProvider().equals(LocationManager.GPS_PROVIDER))) {
            if (locationList.size() < LOCATION_CHECK_SIZE) {
                locationList.add(location);
            } else {
                //float distanceAvr = 0;
                float distanceLast = Coordinate.distance(location, locationList.get(locationList.size() - 1));
                float speedAvr = 0;
                float speedLast = distanceLast / (location.getTime() - (locationList.get(locationList.size() - 1)).getTime()) * 1000;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < locationList.size() - 1; i++) {
                    float distanceCur = Coordinate.distance(locationList.get(i), locationList.get(i + 1));
                    float time = (float) ((locationList.get(i + 1)).getTime() - (locationList.get(i)).getTime()) / 1000;
                    float speedCur = distanceCur / time;

                    //distanceAvr += distanceCur;
                    speedAvr += (speedCur * time);

                    stringBuilder.append(speedCur).append(" | ");
                }
                //distanceAvr /= locationList.size();
                speedAvr /= ((locationList.get(locationList.size() - 1).getTime() - locationList.get(0).getTime()) / 1000);

                float speedCheck = (float) ((Math.atan(speedAvr * 0.04)) * 50) + 3;

                Log.d(TAG, "---------------------------------");
                //Log.d(TAG, "distanceLast: " + distanceLast + " distanceAvr: " + distanceAvr);
                Log.d(TAG, "speedCheck: " + speedCheck + " speedAvr: " + speedAvr);
                Log.d(TAG, "speedLast: " + speedLast + " speedAvr: " + speedAvr);
                Log.d(TAG, stringBuilder.toString());

                if (speedLast < speedCheck) {
                    /*if (speedLast != 0) {
                        locationAction(location);
                    }*/
                    locationAction(location);
                    addNewLocation(location);
                    ringCount = 0;
                } else {
                    if (ringCount < 3) {
                        ringCount++;
                    } else {
                        /*if (speedLast < 50) {
                            addNewLocation(location);
                        }*/
                        addNewLocation(location);
                    }
                }
            }
        }
    }

    private void addNewLocation(Location location) {
        locationList.remove(0);
        locationList.add(location);
    }

    private void locationAction(Location location) {
        Log.d(TAG, "locationAction: " + location.getProvider() + ", accuracy: " + location.getAccuracy() + ", speed: " + location.getSpeed());

        List<PurchaseListModel> lists = new ArrayList<>(purchaseLists);
        boolean needGps = false;

        for (PurchaseListModel list : lists) {
            if (!list.isDone()) {
                //float distance = location.distanceTo(list.getPoint());
                float distance = Coordinate.distance(location, list.getPlaceLocation());
                Log.d(TAG, list.getListName() + " distance: " + distance + "/" + list.getMaxPointDistance());
                if (distance < GPS_RADIUS) {
                    /*if (!isGps) {
                        if (latencyLocation == null
                                | (latencyLocation != null
                                && Coordinate.distance(latencyLocation, list.getPoint()) > MIN_RADIUS)) {
                            needGps = true;
                        }
                    } else {
                        if (latencyLocation == null) {
                            latencyLocation = location;
                            needGps = true;
                        }
                    }*/

                    if (latencyLocation == null
                            | (latencyLocation != null
                            && Coordinate.distance(latencyLocation, list.getPointLocation()) > MIN_RADIUS)) {
                        needGps = true;
                        if (isGps) {
                            latencyLocation = location;
                        }
                    }
                } else {
                    latencyLocation = null;
                }
                if (!needGps | (needGps & isGps)) {
                    if (list.getMaxPointDistance() < distance && distance > MIN_RADIUS + location.getAccuracy()) {
                        list.setMaxPointDistance(distance);
                        updatePurchaseList(list);
                        Log.d(TAG, " --- Update!!!");
                        //Log.d("LOCATION", "location: " + location.toString());
                        //Log.d("LOCATION", "point: " + list.getPoint().toString());
                    } else if (list.getMaxPointDistance() / 2 > distance) {
                        showNotification("/2 distance: " + list.getListName());
                        list.setMaxPointDistance(0);
                        list.setIsAlarm(false);
                        updatePurchaseList(list);
                    }
                    if (!list.isAlarm() && distance < list.getPointRadius() + APPOINTMENT_RADIUS) {
                        showNotification("Appointment distance: " + list.getListName());
                        list.setIsAlarm(true);
                        updatePurchaseList(list);
                    }
                }
            }
        }

        if (needGps) {
            if (!isGps) {
                startGps();
            }
        } else if (isGps){
            if (latencyLocation != null) {
                int time = (int) (location.getTime() - latencyLocation.getTime() / 1000);
                if (locationList.size() == LOCATION_CHECK_SIZE
                        && time > 30) {
                    stopGps();
                }
            } else {
                stopGps();
            }
        }
    }

    private int updatePurchaseList(PurchaseListModel list) {
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_APPOINTMENT_URI + "/" + list.getDbId());
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_POINT_DISTANCE, list.getMaxPointDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM, list.isAlarm() ? 1 : 0);
        return getContentResolver().update(
                uri,
                values,
                null,
                null
        );
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

    private void startGps() {
        Log.d(TAG, "startGps");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.removeUpdates(networkListener);
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, UPDATE_TIME_GPS, UPDATE_PLACE_GPS, gpsListener);
        isGps = true;
    }

    private void stopGps() {
        Log.d(TAG, "stopGps");
        locationManager.removeUpdates(gpsListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, networkListener);
        isGps = false;
    }
}
