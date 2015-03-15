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
@Deprecated
public class GpsAppointment {
    private static final String TAG = GpsAppointment.class.getSimpleName();


    public GpsAppointment(Context context) {
        this.context = context;
        appointmentList = new ArrayList<>();
    }

    public void update() {


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




}
