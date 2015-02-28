package geekhub.activeshoplistapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 2/27/15.
 */
public class DataBaseHelper {
    private static final String LOG = "DataBaseHelper";

    private SqlDbHelper dbHelper;
    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        dbHelper = SqlDbHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int universalUpdateColumn(String table, String column, String val, long id) {
        ContentValues values = new ContentValues();
        values.put(column, val);
        int update = database.update(
                table,
                values,
                SqlDbHelper.COLUMN_ID + " = " + id,
                null
        );
        return update;
    }

    public long addPurchaseList(PurchaseListModel list) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID, list.getServerId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME, list.getListName());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID, list.getUserId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID, list.getShopId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM, list.getTimeAlarm());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE, list.getTimeCreate());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP, list.getTimeStamp());
        long rawId = database.insert(
                SqlDbHelper.TABLE_PURCHASE_LIST,
                null,
                values
        );
        return rawId;
    }

    public int updatePurchaseList(PurchaseListModel list) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID, list.getServerId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME, list.getListName());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID, list.getUserId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID, list.getShopId());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM, list.getTimeAlarm());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE, list.getTimeCreate());
        values.put(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP, list.getTimeStamp());
        int update = database.update(
                SqlDbHelper.TABLE_PURCHASE_LIST,
                values,
                SqlDbHelper.PURCHASE_LIST_COLUMN_ID + " = " + list.getDbId(),
                null
        );
        return update;
    }

    public int deletePurchaseList(long dbId) {
        int count = database.delete(
                SqlDbHelper.TABLE_PURCHASE_LIST,
                SqlDbHelper.PURCHASE_LIST_COLUMN_ID + " = " + dbId,
                null
        );
        return count;
    }

    public List<PurchaseListModel> getPurchaseLists(){
        List<PurchaseListModel> list = new ArrayList<>();
        String[] projection = {
                SqlDbHelper.PURCHASE_LIST_COLUMN_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        String orderBy = SqlDbHelper.PURCHASE_LIST_COLUMN_ID + " DESC";
        Cursor cursor = database.query(
                SqlDbHelper.TABLE_PURCHASE_LIST,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int indexId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID);
            int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
            int indexUser = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID);
            int indexShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID);
            int indexAlarm = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM);
            int indexCreate = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP);
            List<PurchaseItemModel> purchasesItem = new ArrayList<>();
            PurchaseListModel listModel = new PurchaseListModel(
                    cursor.getInt(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getString(indexName),
                    cursor.getInt(indexUser),
                    cursor.getInt(indexShop),
                    cursor.getLong(indexAlarm),
                    cursor.getLong(indexCreate),
                    cursor.getLong(indexTimestamp),
                    purchasesItem
            );
            list.add(listModel);
            cursor.moveToNext();
        }
        return list;
    }
}
