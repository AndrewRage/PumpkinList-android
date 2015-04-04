package geekhub.activeshoplistapp.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.services.GpsAppointmentService;
import geekhub.activeshoplistapp.utils.AlarmUtils;
import geekhub.activeshoplistapp.utils.Coordinate;

/**
 * Created by rage on 3/29/15.
 */
public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startGps = new Intent(context, GpsAppointmentService.class);
        context.startService(startGps);
        startPurchaseListAlarm(context);
    }

    private void startPurchaseListAlarm(Context context) {
        List<PurchaseListModel> purchaseLists = new ArrayList<>();
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
            String selection = SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM + "!=? AND "
                    + SqlDbHelper.PURCHASE_LIST_COLUMN_DONE + "=?";
            Cursor cursor = context.getContentResolver().query(
                    ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                    projection,
                    selection,
                    args,
                    null
            );

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

        AlarmUtils alarmUtils = new AlarmUtils(context);
        for (PurchaseListModel list : purchaseLists) {
            alarmUtils.setListAlarm(list);
        }
    }
}
