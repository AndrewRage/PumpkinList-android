package geekhub.activeshoplistapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.ShoppingContentProvider;

/**
 * Created by rage on 3/21/15.
 */
public class ContentHelper {

    public static Cursor getPurchaseLists(Context context) {
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
        return context.getContentResolver().query(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                projection,
                null,
                null,
                orderBy
        );
    }

    public static PurchaseListModel getPurchaseList(Context context, long dbId) {
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + dbId);
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_MAX_DISTANCE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID);
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
        int indexUser = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID);
        int indexShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID);
        int indexPlace = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID);
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
                cursor.getInt(indexPlace),
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
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID, list.getPlaceId());
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
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID, list.getPlaceId());
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
        Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + list.getDbId());
        return context.getContentResolver().delete(
                uri,
                null,
                null
        );
    }

    public static long getDbId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(SqlDbHelper.COLUMN_ID));
    }
}
