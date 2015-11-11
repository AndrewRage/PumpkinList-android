package geekhub.activeshoplistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.PurchaseActivity;
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
    private static final int GPS_PASSIVE_TIME = 1000 * 60;
    private static final int GPS_RADIUS = 1000;
    private static final int MIN_RADIUS = 150;
    private static final int APPOINTMENT_RADIUS = 500;
    private static final int LOCATION_CHECK_SIZE = 3;
    private static final int APPOINTMENT_TIME = 1000 * 60 * 60;

    private Handler handler;
    private LocationManager locationManager;
    private List<PurchaseListModel> purchaseLists;
    private List<Location> locationList;
    private boolean isGps;
    private int ringCount = 0;
    private long gpsTimeStamp;

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
        getContentResolver().registerContentObserver(
                ShoppingContentProvider.PLACE_CONTENT_URI,
                true,
                contentObserver
        );
        //readPurchaseList();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        readPurchaseList();
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
            String[] args = new String[]{"0", "0", "0"};
            String selection = "(" + SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID + "!=? OR "
                    + SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID + "!=?) AND "
                    + SqlDbHelper.PURCHASE_LIST_COLUMN_DONE + "=?";
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

        //Log.d(TAG, "purchaseList.size = " + purchaseLists.size());

        Iterator<PurchaseListModel> iterator = purchaseLists.iterator();
        while (iterator.hasNext()) {
            PurchaseListModel purchaseList = iterator.next();

            Location point = null;
            Location shop = null;
            Location place = null;
            float pointRadius = 0;

            if (purchaseList.getShopId() != 0) {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                };
                StringBuilder selectionBuilder = new StringBuilder();
                if (purchaseList.isUserShop()) {
                    selectionBuilder.append(SqlDbHelper.COLUMN_ID);
                } else {
                    selectionBuilder.append(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
                }
                selectionBuilder.append("=?");
                String selection = selectionBuilder.toString();
                String[] args = new String[]{Long.toString(purchaseList.getShopId())};
                Cursor cursor = getContentResolver().query(
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        selection,
                        args,
                        null
                );
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
                    int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
                    shop = new Location(AppConstants.LOCATION);
                    shop.setLatitude(cursor.getDouble(indexLatitude));
                    shop.setLongitude(cursor.getDouble(indexLongitude));
                }
                cursor.close();
            }

            if (purchaseList.getPlaceId() != 0) {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                };
                StringBuilder selectionBuilder = new StringBuilder();
                if (purchaseList.isUserPlace()) {
                    selectionBuilder.append(SqlDbHelper.COLUMN_ID);
                } else {
                    selectionBuilder.append(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
                }
                selectionBuilder.append("=?");
                String selection = selectionBuilder.toString();
                String[] args = new String[]{Long.toString(purchaseList.getPlaceId())};
                Cursor cursor = getContentResolver().query(
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        selection,
                        args,
                        null
                );
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
                    int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
                    place = new Location(AppConstants.LOCATION);
                    place.setLatitude(cursor.getDouble(indexLatitude));
                    place.setLongitude(cursor.getDouble(indexLongitude));
                }
                cursor.close();
            }

            if (shop != null && place != null) {
                point = Coordinate.middlePoint(shop, place);
                pointRadius = Coordinate.distance(point, place);
                if (pointRadius < 1500) {
                    purchaseList.setPointRadius(pointRadius);
                    purchaseList.setPointLocation(point);
                }
            }
            if (pointRadius == 0 || pointRadius > MIN_RADIUS * 2) {
                if (shop != null) {
                    purchaseList.setShopLocation(shop);
                }
                if (place != null) {
                    purchaseList.setPlaceLocation(place);
                }
            }
            if (purchaseList.getPointLocation() == null
                    && purchaseList.getShopLocation() == null
                    && purchaseList.getPlaceLocation() == null) {
                purchaseLists.remove(purchaseList);
            }
        }

        if (purchaseLists.size() != 0) {
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
        } else {
            if (locationManager != null) {
                locationManager.removeUpdates(gpsListener);
                locationManager.removeUpdates(networkListener);
                locationManager.removeUpdates(passiveListener);
                locationManager = null;
            }
            //handler = null;
            //purchaseLists = null;
            Log.d(TAG, "serviceStop");
            this.stopSelf();
        }
    }

    private LocationListener passiveListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Log.d(TAG, "passiveListener");
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
        if (!isGps
                | (isGps && location.getProvider().equals(LocationManager.GPS_PROVIDER))
                | (isGps && location.getTime() - gpsTimeStamp / 1000 > 30)) {
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                gpsTimeStamp = System.currentTimeMillis();
            }
            if (locationList.size() < LOCATION_CHECK_SIZE) {
                locationList.add(location);
                Log.d(TAG, "locationList.size = " + locationList.size());
            } else {
                float distanceLast = Coordinate.distance(location, locationList.get(locationList.size() - 1));
                float speedAvr = 0;
                float speedLast = distanceLast / (location.getTime() - (locationList.get(locationList.size() - 1)).getTime()) * 1000;
                for (int i = 0; i < locationList.size() - 1; i++) {
                    float distanceCur = Coordinate.distance(locationList.get(i), locationList.get(i + 1));
                    float time = (float) ((locationList.get(i + 1)).getTime() - (locationList.get(i)).getTime()) / 1000;
                    float speedCur = distanceCur / time;
                    speedAvr += (speedCur * time);
                }
                speedAvr /= ((locationList.get(locationList.size() - 1).getTime() - locationList.get(0).getTime()) / 1000);

                float speedCheck = (float) ((Math.atan(speedAvr * 0.04)) * 50) + 3;

                if (speedLast < speedCheck) {
                    locationAction(location);
                    addNewLocation(location);
                    ringCount = 0;
                } else {
                    if (ringCount < 3) {
                        ringCount++;
                    } else {
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
        //Log.d(TAG, "locationAction: " + location.getProvider() + ", accuracy: " + location.getAccuracy() + ", speed: " + location.getSpeed());

        List<PurchaseListModel> lists = new ArrayList<>(purchaseLists);
        boolean needGps = false;
        long latencyMaxTime = 0;

        for (PurchaseListModel list : lists) {
            if (!list.isDone()) {
                boolean needUpdate = false;

                List<Integer> placeList = new ArrayList<>();
                if (list.getShopLocation() != null) {
                    placeList.add(PurchaseListModel.PLACE_SHOP);
                }
                if (list.getPlaceLocation() != null) {
                    placeList.add(PurchaseListModel.PLACE_PLACE);
                }
                if (list.getPointLocation() != null) {
                    placeList.add(PurchaseListModel.PLACE_POINT);
                }

                for (int place : placeList) {
                    float distance = Coordinate.distance(location, list.getLocation(place));
                    Log.d(TAG, list.getListName() + "/" + place + " distance: " + distance + "/" + list.getMaxDistance(place));
                    if ((place != PurchaseListModel.PLACE_POINT
                            | list.isSinglePoint()) & distance < GPS_RADIUS) {
                        if (list.getLatencyLocation(place) == null
                                | (list.getLatencyLocation(place) != null
                                && Coordinate.distance(list.getLatencyLocation(place), location) > MIN_RADIUS)) {
                            needGps = true;
                            if (list.getLatencyLocation(place) != null
                                    && list.getLatencyLocation(place).getTime() > latencyMaxTime) {
                                latencyMaxTime = list.getLatencyLocation(place).getTime();
                            }
                            if (isGps) {
                                list.setLatencyLocation(place, location);
                            }
                        }
                    } else {
                        list.setLatencyLocation(place, null);
                    }
                    if (!needGps | (needGps & isGps)) {
                        if (list.getMaxDistance(place) < distance
                                && distance > MIN_RADIUS + location.getAccuracy() + list.getRadius(place)) {
                            list.setMaxDistance(place, distance);
                        } else if (list.getMaxDistance(place) * 0.3f > distance
                                | (distance > list.getRadius(place) + APPOINTMENT_RADIUS
                                && list.getMaxDistance(place) * 0.5f > distance )) {
                            list.setMaxDistance(place, 0);
                            if (list.getAlarmTimeStamp() == 0
                                    | (list.getAlarmTimeStamp() > 0
                                    && System.currentTimeMillis() - list.getAlarmTimeStamp() > APPOINTMENT_TIME)) {
                                list.setIsAlarm(false);
                            }
                            needUpdate = true;
                        }
                        if (distance < MIN_RADIUS && list.getMaxDistance(place) > 0) {
                            list.setMaxDistance(0);
                            needUpdate = true;
                        }
                        if (!list.isAlarm() && distance < list.getRadius(place) + APPOINTMENT_RADIUS) {
                            showNotification(list);
                            list.setMaxDistance(0);
                            list.setIsAlarm(true);
                            list.setAlarmTimeStamp(System.currentTimeMillis());
                            needUpdate = true;
                            Log.d(TAG, " --- Alarm!!!");
                        }
                    }
                }
                if (needUpdate) {
                    updatePurchaseList(list);
                }
            }
        }

        if (needGps) {
            if (!isGps) {
                startGps();
            }
        } else if (isGps){

            if (latencyMaxTime > 0) {
                int time = (int) (location.getTime() - latencyMaxTime);
                if (locationList.size() == LOCATION_CHECK_SIZE
                        && time > GPS_PASSIVE_TIME) {
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
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_SHOP_DISTANCE, list.getMaxShopDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_PLACE_DISTANCE, list.getMaxPlaceDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_POINT_DISTANCE, list.getMaxPointDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM, list.isAlarm() ? 1 : 0);
        return getContentResolver().update(
                uri,
                values,
                null,
                null
        );
    }

    private void showNotification(PurchaseListModel list) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(GpsAppointmentService.this)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setLargeIcon(bitmap)
                        .setContentTitle(list.getListName())
                        .setContentText(getApplicationContext().getString(R.string.notification_gps_description))
                        //.setContentInfo("info")
                        //.setTicker("ticker")
                ;
        Intent startIntent = new Intent(getApplicationContext(), PurchaseActivity.class);
        startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, list.getDbId());
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(
                        GpsAppointmentService.this,
                        new Random().nextInt(),
                        startIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
    }

    private void startGps() {
        Log.d(TAG, "startGps");
        /*if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.removeUpdates(networkListener);
        }*/
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, UPDATE_TIME_GPS, UPDATE_PLACE_GPS, gpsListener);
        isGps = true;
        gpsTimeStamp = System.currentTimeMillis();
    }

    private void stopGps() {
        Log.d(TAG, "stopGps");
        locationManager.removeUpdates(gpsListener);
        /*locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, UPDATE_TIME_NETWORK, UPDATE_PLACE_NETWORK, networkListener);*/
        isGps = false;
    }
}
