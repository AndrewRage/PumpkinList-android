package geekhub.activeshoplistapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import geekhub.activeshoplistapp.BuildConfig;

/**
 * Created by rage on 5/17/15.
 */
public class GeoLocationService extends IntentService {
    public static final String GEO_LOCATION_BROADCAST = BuildConfig.APPLICATION_ID + ".geoLocationService";
    public static final String TASK_ID = "taskId";
    public static final String SEARCH_STRING = "searchString";
    public static final String LAT_LONG = "latLong";
    public static final String ADDRESS_RESULT = "addressResult";
    public static final int GET_LOCATION = 1;
    public static final int GET_ADDRESS = 2;

    public GeoLocationService() {
        super("GeoLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String searchString = intent.getExtras().getString(SEARCH_STRING);
        LatLng latLng = intent.getExtras().getParcelable(LAT_LONG);
        if (!TextUtils.isEmpty(searchString)) {
            getLocation(searchString);
        } else if (latLng != null) {
            getAddress(latLng);
        }
    }

    private void getLocation (String address) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(GEO_LOCATION_BROADCAST);
        intent.putExtra(TASK_ID, GET_LOCATION);
        if(addresses != null && addresses.size() > 0) {
            intent.putExtra(ADDRESS_RESULT, addresses.get(0));
        }
        sendBroadcast(intent);
    }

    private void getAddress (LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(GEO_LOCATION_BROADCAST);
        intent.putExtra(TASK_ID, GET_ADDRESS);
        if(addresses != null && addresses.size() > 0) {
            intent.putExtra(ADDRESS_RESULT, addresses.get(0));
        }
        sendBroadcast(intent);
    }
}
