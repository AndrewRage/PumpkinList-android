package geekhub.activeshoplistapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

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
    public static final int GET_LOCATION = 1;
    public static final int GET_ADDRESS = 2;

    public GeoLocationService() {
        super("GeoLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int taskId = intent.getIntExtra(TASK_ID, -1);
        String searchString = intent.getExtras().getString(SEARCH_STRING);
        if (taskId > 0 && !TextUtils.isEmpty(searchString)) {
            if (taskId == GET_LOCATION) {
                getLocation(searchString);
            } else if (taskId == GET_ADDRESS) {

            }
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
        if(addresses != null && addresses.size() > 0) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
        }
    }
}
