package geekhub.activeshoplistapp.utils;

import android.location.Location;

import geekhub.activeshoplistapp.helpers.AppConstants;

/**
 * Created by rage on 3/15/15.
 */
public class Coordinate {

    public static Location middlePoint(Location location1, Location location2){
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();
        return middlePoint(lat1, lon1, lat2, lon2);
    }

    public static Location middlePoint(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        Location location = new Location(AppConstants.LOCATION);
        location.setLatitude(Math.toDegrees(lat3));
        location.setLongitude(Math.toDegrees(lon3));
        return location;
    }
}
