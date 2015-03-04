package geekhub.activeshoplistapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.ShopsModel;

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
                SqlDbHelper.COLUMN_ID + " = " + list.getDbId(),
                null
        );
        return update;
    }

    public int deletePurchaseList(long dbId) {
        int count = database.delete(
                SqlDbHelper.TABLE_PURCHASE_LIST,
                SqlDbHelper.COLUMN_ID + " = " + dbId,
                null
        );
        return count;
    }

    public List<PurchaseListModel> getPurchaseLists() {
        List<PurchaseListModel> list = new ArrayList<>();
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
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
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID);
            int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
            int indexUser = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID);
            int indexShop = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID);
            int indexAlarm = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM);
            int indexCreate = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP);
            PurchaseListModel listModel = new PurchaseListModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getString(indexName),
                    cursor.getInt(indexUser),
                    cursor.getInt(indexShop),
                    cursor.getLong(indexAlarm),
                    cursor.getLong(indexCreate),
                    cursor.getLong(indexTimestamp),
                    null
            );
            list.add(listModel);
            cursor.moveToNext();
        }
        return list;
    }

    public long addPurchaseItem(PurchaseItemModel item, long listId) {
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
        long rawId = database.insert(
                SqlDbHelper.TABLE_PURCHASE_ITEM,
                null,
                values
        );
        return rawId;
    }

    public long updatePurchaseItem(PurchaseItemModel item) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID, item.getServerId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT, item.isBought() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL, item.isCancel() ? 1 : 0);
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID, item.getGoodsId());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL, item.getGoodsLabel());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY, item.getGoodsQuantity());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION, item.getGoodsDescription());
        values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP, item.getTimeStamp());
        long rawId = database.update(
                SqlDbHelper.TABLE_PURCHASE_ITEM,
                values,
                SqlDbHelper.COLUMN_ID + " = " + item.getDbId(),
                null
        );
        return rawId;
    }

    public int deletePurchaseItem(long listDbId) {
        int count = database.delete(
                SqlDbHelper.TABLE_PURCHASE_ITEM,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID + " = " + listDbId,
                null
        );
        return count;
    }

    public List<PurchaseItemModel> getPurchaseItems(long listDbId) {
        List<PurchaseItemModel> items = new ArrayList<>();
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP,
        };
        /*String[] selectionArgs = {
                String.valueOf(listDbId)
        };*/
        String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
        Cursor cursor = database.query(
                SqlDbHelper.TABLE_PURCHASE_ITEM,
                projection,
                SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID + " = " + listDbId,
                null,
                null,
                null,
                orderBy
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID);
            int indexIsBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);
            int indexIsCancel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL);
            int indexGoodsId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID);
            int indexLabel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
            int indexQuantity = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY);
            int indexDescription = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP);
            PurchaseItemModel itemModel = new PurchaseItemModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    listDbId,
                    cursor.getInt(indexIsBought)>0,
                    cursor.getInt(indexIsCancel)>0,
                    cursor.getInt(indexGoodsId),
                    cursor.getString(indexLabel),
                    cursor.getFloat(indexQuantity),
                    cursor.getString(indexDescription),
                    cursor.getLong(indexTimestamp)
            );
            items.add(itemModel);
            cursor.moveToNext();
        }
        return items;
    }

    public long addShop(ShopsModel shop) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.SHOPS_COLUMN_SHOP_ID, shop.getServerId());
        values.put(SqlDbHelper.SHOPS_COLUMN_NAME, shop.getShopName());
        values.put(SqlDbHelper.SHOPS_COLUMN_DESCRIPTION, shop.getShopDescription());
        values.put(SqlDbHelper.SHOPS_COLUMN_LATITUDE, shop.getGpsLatitude());
        values.put(SqlDbHelper.SHOPS_COLUMN_LONGITUDE, shop.getGpsLongitude());
        values.put(SqlDbHelper.SHOPS_COLUMN_IS_DELETE, shop.isDelete() ? 1 : 0);
        values.put(SqlDbHelper.SHOPS_COLUMN_TIMESTAMP, shop.getGpsLongitude());
        long rawId = database.insert(
                SqlDbHelper.TABLE_SHOPS,
                null,
                values
        );
        return rawId;
    }

    public long updateShop(ShopsModel shop) {
        ContentValues values = new ContentValues();
        values.put(SqlDbHelper.SHOPS_COLUMN_SHOP_ID, shop.getServerId());
        values.put(SqlDbHelper.SHOPS_COLUMN_NAME, shop.getShopName());
        values.put(SqlDbHelper.SHOPS_COLUMN_DESCRIPTION, shop.getShopDescription());
        values.put(SqlDbHelper.SHOPS_COLUMN_LATITUDE, shop.getGpsLatitude());
        values.put(SqlDbHelper.SHOPS_COLUMN_LONGITUDE, shop.getGpsLongitude());
        values.put(SqlDbHelper.SHOPS_COLUMN_IS_DELETE, shop.isDelete() ? 1 : 0);
        values.put(SqlDbHelper.SHOPS_COLUMN_TIMESTAMP, shop.getGpsLongitude());
        long rawId = database.update(
                SqlDbHelper.TABLE_SHOPS,
                values,
                SqlDbHelper.COLUMN_ID + " = " + shop.getDbId(),
                null
        );
        return rawId;
    }

    public int deleteShop(long listDbId) {
        int count = database.delete(
                SqlDbHelper.TABLE_SHOPS,
                SqlDbHelper.COLUMN_ID + " = " + listDbId,
                null
        );
        return count;
    }

    public List<ShopsModel> getShopsList() {
        List<ShopsModel> shops = new ArrayList<>();
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.SHOPS_COLUMN_SHOP_ID,
                SqlDbHelper.SHOPS_COLUMN_NAME,
                SqlDbHelper.SHOPS_COLUMN_DESCRIPTION,
                SqlDbHelper.SHOPS_COLUMN_LATITUDE,
                SqlDbHelper.SHOPS_COLUMN_LONGITUDE,
                SqlDbHelper.SHOPS_COLUMN_IS_DELETE,
                SqlDbHelper.SHOPS_COLUMN_TIMESTAMP,
        };
        String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
        Cursor cursor = database.query(
                SqlDbHelper.TABLE_SHOPS,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_SHOP_ID);
            int indexName = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_NAME);
            int indexDescription = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_DESCRIPTION);
            int indexLatitude = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_LATITUDE);
            int indexLongitude = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_LONGITUDE);
            int indexIsDelete = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_IS_DELETE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.SHOPS_COLUMN_TIMESTAMP);
            ShopsModel shopItem = new ShopsModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getString(indexName),
                    cursor.getString(indexDescription),
                    cursor.getDouble(indexLatitude),
                    cursor.getDouble(indexLongitude),
                    cursor.getInt(indexIsDelete)>0,
                    cursor.getLong(indexTimestamp)
            );
            shops.add(shopItem);
            cursor.moveToNext();
        }
        return shops;
    }
}
