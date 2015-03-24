package geekhub.activeshoplistapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rage on 2/26/15.
 */
public class SqlDbHelper extends SQLiteOpenHelper {
    private static final String TAG = SqlDbHelper.class.getSimpleName();

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "aslistapp.db";

    private static final String INT_PRIMARY_KAY = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String ADD_COLUMN = " ADD COLUMN ";

    public final static String COLUMN_ID = "_id";

    public final static String TABLE_FRIENDS = "friends";
    public final static String FRIENDS_COLUMN_NAME = "user_name";
    public final static String SQL_CREATE_FRIENDS =
            "CREATE TABLE " + TABLE_FRIENDS + " (" +
            COLUMN_ID + INT_PRIMARY_KAY + COMMA_SEP +
            FRIENDS_COLUMN_NAME + TEXT_TYPE +
            " );";
    public final static String SQL_DELETE_FRIENDS =
            "DROP TABLE IF EXISTS " + TABLE_FRIENDS;

    public final static String TABLE_GOODS = "goods";
    public final static String GOODS_COLUMN_GOODS = "goods_id";
    public final static String GOODS_COLUMN_LABEL = "good_label";
    public final static String GOODS_COLUMN_CATEGORY_ID = "good_category_id";
    public final static String GOODS_COLUMN_MEASURE_ID = "good_measure_id";
    public final static String GOODS_COLUMN_DESCRIPTION = "goods_description";
    public final static String SQL_CREATE_GOODS =
            "CREATE TABLE " + TABLE_GOODS + " (" +
            COLUMN_ID + INT_PRIMARY_KAY + COMMA_SEP +
            GOODS_COLUMN_GOODS + INTEGER_TYPE + COMMA_SEP +
            GOODS_COLUMN_LABEL + INTEGER_TYPE + COMMA_SEP +
            GOODS_COLUMN_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
            GOODS_COLUMN_MEASURE_ID + INTEGER_TYPE + COMMA_SEP +
            GOODS_COLUMN_DESCRIPTION + TEXT_TYPE +
            " );";
    public final static String SQL_DELETE_GOODS =
            "DROP TABLE IF EXISTS " + TABLE_GOODS;

    public final static String TABLE_PURCHASE_ITEM = "purchase_items";
    public final static String PURCHASE_ITEM_COLUMN_ITEM_ID = "item_id";
    public final static String PURCHASE_ITEM_COLUMN_LIST_ID = "list_id";
    public final static String PURCHASE_ITEM_COLUMN_IS_BOUGHT = "is_bought";
    public final static String PURCHASE_ITEM_COLUMN_IS_CANCEL = "is_cancel";
    public final static String PURCHASE_ITEM_COLUMN_GOODS_ID = "goods_id";
    public final static String PURCHASE_ITEM_COLUMN_GOODS_LABEL = "goods_label";
    public final static String PURCHASE_ITEM_COLUMN_GOODS_QUANTITY = "goods_quantity";
    public final static String PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION = "goods_description";
    public final static String PURCHASE_ITEM_COLUMN_TIMESTAMP = "timestamp";
    public final static String SQL_CREATE_PURCHASE_ITEM =
            "CREATE TABLE " + TABLE_PURCHASE_ITEM + " (" +
            COLUMN_ID + INT_PRIMARY_KAY + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_ITEM_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_LIST_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_IS_BOUGHT + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_IS_CANCEL + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_GOODS_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_GOODS_LABEL + TEXT_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_GOODS_QUANTITY + REAL_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            PURCHASE_ITEM_COLUMN_TIMESTAMP + INTEGER_TYPE +
            " );";
    public final static String SQL_DELETE_PURCHASE_ITEM =
            "DROP TABLE IF EXISTS " + TABLE_PURCHASE_ITEM;

    public final static String TABLE_PURCHASE_LIST = "purchase_lists";
    public final static String PURCHASE_LIST_COLUMN_LIST_ID = "list_id";
    public final static String PURCHASE_LIST_COLUMN_LIST_NAME = "list_name";
    public final static String PURCHASE_LIST_COLUMN_USER_ID = "user_id";
    public final static String PURCHASE_LIST_COLUMN_SHOP_ID = "shop_id";
    public final static String PURCHASE_LIST_COLUMN_IS_USER_SHOP = "is_user_shop";
    public final static String PURCHASE_LIST_COLUMN_PLACE_ID = "place_id";
    public final static String PURCHASE_LIST_COLUMN_IS_USER_PLACE = "is_user_place";
    public final static String PURCHASE_LIST_COLUMN_DONE = "is_done";
    public final static String PURCHASE_LIST_COLUMN_MAX_DISTANCE = "max_distance";
    public final static String PURCHASE_LIST_COLUMN_IS_ALARM = "is_alarm";
    public final static String PURCHASE_LIST_COLUMN_TIME_ALARM = "time_alarm";
    public final static String PURCHASE_LIST_COLUMN_TIME_CREATE = "time_create";
    public final static String PURCHASE_LIST_COLUMN_TIMESTAMP = "timestamp";
    public final static String SQL_CREATE_PURCHASE_LIST =
            "CREATE TABLE " + TABLE_PURCHASE_LIST + " (" +
            COLUMN_ID + INT_PRIMARY_KAY + COMMA_SEP +
            PURCHASE_LIST_COLUMN_LIST_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_LIST_NAME + TEXT_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_USER_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_SHOP_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_IS_USER_SHOP + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_PLACE_ID + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_IS_USER_PLACE + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_DONE + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_MAX_DISTANCE + REAL_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_IS_ALARM + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_TIME_ALARM + TEXT_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_TIME_CREATE + INTEGER_TYPE + COMMA_SEP +
            PURCHASE_LIST_COLUMN_TIMESTAMP + INTEGER_TYPE +
            " );";
    public final static String SQL_DELETE_PURCHASE_LIST =
            "DROP TABLE IF EXISTS " + TABLE_PURCHASE_LIST;
    /*public final static String SQL_UPDATE_TO_V3_PURCHASE_LIST_PLACE =
            "ALTER TABLE " + TABLE_PURCHASE_LIST + ADD_COLUMN + PURCHASE_LIST_COLUMN_PLACE_ID + INTEGER_TYPE + ";";
    public final static String SQL_UPDATE_TO_V3_PURCHASE_LIST_DONE =
            "ALTER TABLE " + TABLE_PURCHASE_LIST + ADD_COLUMN + PURCHASE_LIST_COLUMN_DONE + INTEGER_TYPE + ";";*/

    public final static String TABLE_PLACES = "places";
    public final static String PLACES_COLUMN_PLACES_ID = "place_id";
    public final static String PLACES_COLUMN_CATEGORY = "place_category";
    public final static String PLACES_COLUMN_NAME = "place_name";
    public final static String PLACES_COLUMN_DESCRIPTION = "place_description";
    public final static String PLACES_COLUMN_LATITUDE = "latitude";
    public final static String PLACES_COLUMN_LONGITUDE = "longitude";
    public final static String PLACES_COLUMN_IS_DELETE = "is_delete";
    public final static String PLACES_COLUMN_TIMESTAMP = "timestamp";
    public final static String SQL_CREATE_PLACES =
            "CREATE TABLE " + TABLE_PLACES + " (" +
            COLUMN_ID + INT_PRIMARY_KAY + COMMA_SEP +
            PLACES_COLUMN_PLACES_ID + INTEGER_TYPE + COMMA_SEP +
            PLACES_COLUMN_CATEGORY + INTEGER_TYPE + COMMA_SEP +
            PLACES_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            PLACES_COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            PLACES_COLUMN_LATITUDE + REAL_TYPE + COMMA_SEP + PLACES_COLUMN_LONGITUDE + REAL_TYPE + COMMA_SEP +
            PLACES_COLUMN_IS_DELETE + INTEGER_TYPE + COMMA_SEP +
            PLACES_COLUMN_TIMESTAMP + INTEGER_TYPE +
            " );";
    public final static String SQL_DELETE_PLACES =
            "DROP TABLE IF EXISTS " + TABLE_PLACES;

    private static SqlDbHelper sqlDbHelper;

    private SqlDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SqlDbHelper getInstance(Context context) {
        if (sqlDbHelper == null) {
            sqlDbHelper = new SqlDbHelper(context);
        }
        return sqlDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FRIENDS);
        db.execSQL(SQL_CREATE_GOODS);
        db.execSQL(SQL_CREATE_PURCHASE_ITEM);
        db.execSQL(SQL_CREATE_PURCHASE_LIST);
        db.execSQL(SQL_CREATE_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
