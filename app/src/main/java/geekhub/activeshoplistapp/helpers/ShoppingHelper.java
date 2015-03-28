package geekhub.activeshoplistapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.services.GpsAppointmentService;
import geekhub.activeshoplistapp.utils.Coordinate;

/**
 * Created by rage on 2/24/15.
 */
@Deprecated
public class ShoppingHelper {
    private static final String TAG = ShoppingHelper.class.getSimpleName();
    private static ShoppingHelper shoppingHelper;
    private Context context;
    private List<PurchaseListModel> purchaseLists;
    private List<Long> appointmentLists;
    private List<PlacesModel> placesList;
    private DataBaseHelper dataBaseHelper;
    private boolean isServiceStart = false;

    private ShoppingHelper(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        this.context = context;
    }
    public static ShoppingHelper newInstance(Context context) {
        if (shoppingHelper == null) {
            shoppingHelper = new ShoppingHelper(context);
        }
        return shoppingHelper;
    }

    public static ShoppingHelper getInstance() {
        return shoppingHelper;
    }

    public List<Long> getAppointmentLists() {
        if (appointmentLists == null) {
            for (PurchaseListModel list : getPurchaseLists()) {
                addAppointment(list);
            }
        }
        return appointmentLists;
    }

    private void addAppointment(PurchaseListModel purchaseList) {
        if (appointmentLists == null) {
            appointmentLists = new ArrayList<>();
        }
        int startService = 0;
        if (purchaseList.getPlaceId() != 0 || purchaseList.getShopId() != 0) {
            PlacesModel place = getPlaceById(purchaseList.getPlaceId());
            PlacesModel shop = getPlaceById(purchaseList.getPlaceId());
            Location point = null;
            float radius = 0;
            if (place != null) {
                if (shop != null) {
                    point = Coordinate.middlePoint(
                            place.getGpsLatitude(),
                            place.getGpsLongitude(),
                            shop.getGpsLatitude(),
                            shop.getGpsLongitude()
                    );
                    Location l1 = new Location(AppConstants.LOCATION);
                    l1.setLatitude(place.getGpsLatitude());
                    l1.setLongitude(place.getGpsLongitude());
                    Location l2 = new Location(AppConstants.LOCATION);
                    l2.setLatitude(shop.getGpsLatitude());
                    l2.setLongitude(shop.getGpsLongitude());
                    radius = l1.distanceTo(l2);
                } else {
                    point = new Location(AppConstants.LOCATION);
                    point.setLatitude(place.getGpsLatitude());
                    point.setLongitude(place.getGpsLongitude());
                }

            } else if (shop != null) {
                point = new Location(AppConstants.LOCATION);
                point.setLatitude(shop.getGpsLatitude());
                point.setLongitude(shop.getGpsLongitude());
            }
            if (point != null) {
                /*purchaseList.setPoint(point);
                purchaseList.setRadius(radius);*/
                if (appointmentLists.indexOf(purchaseList.getDbId()) == -1) {
                    appointmentLists.add(purchaseList.getDbId());
                    Log.d(TAG, "Add appointment. size " + appointmentLists.size());
                } else {
                    Log.d(TAG, "Update appointment. size " + appointmentLists.size());
                }
                startService++;
            }
        } else if (getPurchaseLists().indexOf(purchaseList) > -1) {
            appointmentLists.remove(purchaseList.getDbId());
            Log.d(TAG, "Remove appointment. size " + appointmentLists.size());
        }

        if (startService > 0) {
            startGpsService();
        }
    }

    public List<PurchaseListModel> getPurchaseLists() {
        if (purchaseLists == null) {
            dataBaseHelper.open();
            //purchaseLists = dataBaseHelper.getPurchaseLists();
            for (PurchaseListModel list: purchaseLists) {
                list.setPurchasesItems(dataBaseHelper.getPurchaseItems(list.getDbId()));
            }
            dataBaseHelper.close();
            for (PurchaseListModel list: purchaseLists) {
                addAppointment(list);
            }
        }
        return purchaseLists;
    }

    public PurchaseListModel getPurchaseListByDbId (long id) {
        for (PurchaseListModel list : getPurchaseLists()) {
            if (list.getDbId() == id) {
                return list;
            }
        }
        return null;
    }

    public PurchaseListModel getPurchaseListByServerId (long id) {
        for (PurchaseListModel list : getPurchaseLists()) {
            if (list.getServerId() == id) {
                return list;
            }
        }
        return null;
    }

    public long addPurchaseList(PurchaseListModel purchaseList) {
        purchaseList.setTimeCreate(System.currentTimeMillis());
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPurchaseList(purchaseList);
        dataBaseHelper.close();
        for (PurchaseItemModel item: purchaseList.getPurchasesItems()) {
            addPurchaseItem(item, rawId);
        }
        purchaseList.setDbId(rawId);
        purchaseLists.add(0, purchaseList);
        addAppointment(purchaseList);
        return rawId;
    }

    public void deletePurchaseList(PurchaseListModel purchaseList) {
        dataBaseHelper.open();
        dataBaseHelper.deletePurchaseList(purchaseList.getDbId());
        dataBaseHelper.deletePurchaseItem(purchaseList.getDbId());
        dataBaseHelper.close();
        getAppointmentLists().remove(purchaseList);
        purchaseLists.remove(purchaseList);
    }

    public void updatePurchaseList(PurchaseListModel purchaseList) {
        purchaseList.setTimeStamp(0);
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseList(purchaseList);
        dataBaseHelper.close();
        addAppointment(purchaseList);
    }

    public void updatePurchaseListMaxDistance(long dbId, float maxDistance) {
        /*getPurchaseListByDbId(dbId).setMaxDistance(maxDistance);*/
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseListMaxDistamce(dbId, maxDistance);
        dataBaseHelper.close();
    }

    public void updatePurchaseListIsAlarm(long dbId, boolean isAlarm) {
        getPurchaseListByDbId(dbId).setIsAlarm(isAlarm);
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseListIsAlarm(dbId, isAlarm);
        dataBaseHelper.close();
    }

    public long addPurchaseItem(PurchaseItemModel purchaseItem, long listId) {
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPurchaseItem(purchaseItem, listId);
        dataBaseHelper.close();
        purchaseItem.setDbId(rawId);
        return rawId;
    }

    public void updatePurchaseItem(PurchaseItemModel item) {
        item.setTimeStamp(0);
        dataBaseHelper.open();
        dataBaseHelper.updatePurchaseItem(item);
        dataBaseHelper.close();
    }

    public List<PlacesModel> getPlacesList() {
        if (placesList == null) {
            dataBaseHelper.open();
            placesList = dataBaseHelper.getPlacesList();
            dataBaseHelper.close();
        }
        return placesList;
    }

    public long addPlace(PlacesModel placesModel) {
        dataBaseHelper.open();
        long rawId = dataBaseHelper.addPlace(placesModel);
        dataBaseHelper.close();
        placesModel.setDbId(rawId);
        placesList.add(0, placesModel);
        return rawId;
    }

    public void updatePlace(PlacesModel placesModel) {
        placesModel.setTimeStamp(0);
        dataBaseHelper.open();
        dataBaseHelper.updatePlace(placesModel);
        dataBaseHelper.close();
    }

    public void deletePlace(PlacesModel placesModel) {
        dataBaseHelper.open();
        dataBaseHelper.deletePlace(placesModel.getDbId());
        dataBaseHelper.close();
        placesList.remove(placesModel);
    }

    public PlacesModel getPlaceById(long id) {
        for (PlacesModel item : getPlacesList()) {
            if (id > 0 && id == item.getServerId()) {
                return item;
            }
            if (id < 0 && id == (item.getDbId() * (-1))) {
                return item;
            }
        }
        return null;
    }

    private void startGpsService() {
        Log.d(TAG, "GpsAppointment: start");
        Intent intent = new Intent(context, GpsAppointmentService.class);
        context.startService(intent);
        isServiceStart = true;
    }
}
