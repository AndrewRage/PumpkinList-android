package geekhub.activeshoplistapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 3/21/15.
 */
public class ContentHelper {

    public static PurchaseListModel getPurchaseList(Cursor cursor) {
        cursor.moveToFirst();
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID);
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
        int indexUser = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID);
        int indexShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID);
        int indexIsUserShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP);
        int indexPlace = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID);
        int indexIsUserPlace = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE);
        int indexDone = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE);
        int indexMaxDistance = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE);
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
                cursor.getInt(indexIsUserShop)>0,
                cursor.getInt(indexPlace),
                cursor.getInt(indexIsUserPlace)>0,
                cursor.getInt(indexDone)>0,
                cursor.getFloat(indexMaxDistance),
                cursor.getInt(indexIsAlarm)>0,
                cursor.getLong(indexAlarm),
                cursor.getLong(indexCreate),
                cursor.getLong(indexTimestamp),
                null
        );
        cursor.close();
        return listModel;
    }

    public static Uri insertPurchaseList(Context context, PurchaseListModel list) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID, list.getServerId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME, list.getListName());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID, list.getUserId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID, list.getShopId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP, list.isUserShop() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID, list.getPlaceId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE, list.isUserPlace() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE, list.isDone() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE, list.getMaxDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM, list.isAlarm() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM, list.getTimeAlarm());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE, list.getTimeCreate());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP, list.getTimeStamp());
        return context.getContentResolver().insert(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                values
        );
    }

    public static int updatePurchaseList(Context context, PurchaseListModel list) {
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + list.getDbId());
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID, list.getServerId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME, list.getListName());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID, list.getUserId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID, list.getShopId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP, list.isUserShop() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID, list.getPlaceId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE, list.isUserPlace() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE, list.isDone() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE, list.getMaxDistance());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM, list.isAlarm() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM, list.getTimeAlarm());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE, list.getTimeCreate());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP, list.getTimeStamp());
        return context.getContentResolver().update(
                uri,
                values,
                null,
                null
        );
    }

    public static int deletePurchaseList(Context context, PurchaseListModel list) {
        deletePurchaseItems(context, list.getDbId());
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + list.getDbId());
        return context.getContentResolver().delete(
                uri,
                null,
                null
        );
    }

    public static Uri insertPurchaseItem(Context context, PurchaseItemModel item, long listId) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID, item.getServerId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID, listId);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT, item.isBought() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL, item.isCancel() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID, item.getGoodsId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL, item.getGoodsLabel());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY, item.getGoodsQuantity());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION, item.getGoodsDescription());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP, item.getTimeStamp());
        return context.getContentResolver().insert(
                ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                values
        );
    }

    public static int updatePurchaseItem(Context context, PurchaseItemModel item) {
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI + "/" + item.getDbId());
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID, item.getServerId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID, item.getListDbId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT, item.isBought() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL, item.isCancel() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID, item.getGoodsId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL, item.getGoodsLabel());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY, item.getGoodsQuantity());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION, item.getGoodsDescription());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP, item.getTimeStamp());
        return context.getContentResolver().update(
                uri,
                values,
                null,
                null
        );
    }

    public static int deletePurchaseItems(Context context, long listId) {
        return context.getContentResolver().delete(
                ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID + "=?",
                new String[]{Long.toString(listId)}
        );
    }

    public static int deletePurchaseItem(Context context, long dbId) {
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI + "/" + dbId);
        return context.getContentResolver().delete(
                uri,
                null,
                null
        );
    }

    public static long getDbId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(SqlDbHelper.COLUMN_ID));
    }

    public static List<PlacesModel> getPlaceList(Cursor cursor) {
        List<PlacesModel> places = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
            int indexCategory = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_CATEGORY);
            int indexName = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_NAME);
            int indexDescription = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_DESCRIPTION);
            int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
            int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
            int indexIsDelete = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_IS_DELETE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_TIMESTAMP);
            PlacesModel placesModel = new PlacesModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getLong(indexCategory),
                    cursor.getString(indexName),
                    cursor.getString(indexDescription),
                    cursor.getDouble(indexLatitude),
                    cursor.getDouble(indexLongitude),
                    cursor.getInt(indexIsDelete)>0,
                    cursor.getLong(indexTimestamp)
            );
            places.add(placesModel);
            cursor.moveToNext();
        }
        cursor.close();
        return places;
    }

    public static PlacesModel getPlace(Cursor cursor) {
        cursor.moveToFirst();
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
        int indexCategory = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_CATEGORY);
        int indexName = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_NAME);
        int indexDescription = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_DESCRIPTION);
        int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
        int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
        int indexIsDelete = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_IS_DELETE);
        int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_TIMESTAMP);
        PlacesModel placesModel = new PlacesModel(
                cursor.getLong(indexId),
                cursor.getLong(indexServerId),
                cursor.getLong(indexCategory),
                cursor.getString(indexName),
                cursor.getString(indexDescription),
                cursor.getDouble(indexLatitude),
                cursor.getDouble(indexLongitude),
                cursor.getInt(indexIsDelete)>0,
                cursor.getLong(indexTimestamp)
        );
        cursor.close();
        return placesModel;
    }

    public static Uri insertPlace(Context context, PlacesModel placesModel) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PLACES_COLUMN_PLACES_ID, placesModel.getServerId());
        values.put(SqlDbHelper.PLACES_COLUMN_CATEGORY, placesModel.getCategory());
        values.put(SqlDbHelper.PLACES_COLUMN_NAME, placesModel.getShopName());
        values.put(SqlDbHelper.PLACES_COLUMN_DESCRIPTION, placesModel.getShopDescription());
        values.put(SqlDbHelper.PLACES_COLUMN_LATITUDE, placesModel.getGpsLatitude());
        values.put(SqlDbHelper.PLACES_COLUMN_LONGITUDE, placesModel.getGpsLongitude());
        values.put(SqlDbHelper.PLACES_COLUMN_IS_DELETE, placesModel.isDelete() ? 1 : 0);
        values.put(SqlDbHelper.PLACES_COLUMN_TIMESTAMP, placesModel.getGpsLongitude());
        return context.getContentResolver().insert(
                ShoppingContentProvider.PLACE_CONTENT_URI,
                values
        );
    }

    public static int updatePlace(Context context, PlacesModel placesModel) {
        Uri uri = Uri.parse(ShoppingContentProvider.PLACE_CONTENT_URI + "/" + placesModel.getDbId());
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PLACES_COLUMN_PLACES_ID, placesModel.getServerId());
        values.put(SqlDbHelper.PLACES_COLUMN_CATEGORY, placesModel.getCategory());
        values.put(SqlDbHelper.PLACES_COLUMN_NAME, placesModel.getShopName());
        values.put(SqlDbHelper.PLACES_COLUMN_DESCRIPTION, placesModel.getShopDescription());
        values.put(SqlDbHelper.PLACES_COLUMN_LATITUDE, placesModel.getGpsLatitude());
        values.put(SqlDbHelper.PLACES_COLUMN_LONGITUDE, placesModel.getGpsLongitude());
        values.put(SqlDbHelper.PLACES_COLUMN_IS_DELETE, placesModel.isDelete() ? 1 : 0);
        values.put(SqlDbHelper.PLACES_COLUMN_TIMESTAMP, placesModel.getGpsLongitude());
        return context.getContentResolver().update(
                uri,
                values,
                null,
                null
        );
    }

    public static int deletePlace(Context context, long dbId) {
        Uri uri = Uri.parse(ShoppingContentProvider.PLACE_CONTENT_URI + "/" + dbId);
        return context.getContentResolver().delete(
                uri,
                null,
                null
        );
    }
}
