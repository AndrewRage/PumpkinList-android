package geekhub.activeshoplistapp.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ShoppingContentProvider extends ContentProvider {
    private static final String TAG = ShoppingContentProvider.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "geekhub.activeshoplistapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_GOODS = "goods";
    public static final String PATH_PURCHASE_LIST = "purchase_list";
    public static final String PATH_PURCHASE_LIST_APPOINTMENT = "purchase_list_appointment";
    public static final String PATH_PURCHASE_ITEM = "purchase_item";
    public static final String PATH_FRIEND = "friend";
    public static final String PATH_PLACE = "place";

    public static final String PATH_COUNT = "count";

    public static final String PATH_WITH_PLACE = "with_place";

    public static final Uri GOODS_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_GOODS);
    public static final Uri PURCHASE_LIST_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_LIST);
    public static final Uri PURCHASE_LIST_CONTENT_APPOINTMENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_LIST_APPOINTMENT);
    public static final Uri PURCHASE_LIST_CONTENT_COUNT_WITH_PLACE_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_LIST + "/" + PATH_COUNT + "/" + PATH_WITH_PLACE);
    public static final Uri PURCHASE_ITEM_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_ITEM);
    public static final Uri FRIEND_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_FRIEND);
    public static final Uri PLACE_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PLACE);

    static final int URI_GOODS = 100;
    static final int URI_GOODS_ID = 101;
    static final int URI_PURCHASE_LIST = 200;
    static final int URI_PURCHASE_LIST_ID = 201;
    static final int URI_PURCHASE_LIST_COUNT = 202;
    static final int URI_PURCHASE_ITEM = 300;
    static final int URI_PURCHASE_ITEM_ID = 301;
    static final int URI_PLACE = 400;
    static final int URI_PLACE_ID = 401;
    static final int URI_FRIEND = 500;
    static final int URI_FRIEND_ID = 501;

    public static final String GOODS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_GOODS;
    public static final String GOODS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_GOODS;
    public static final String PURCHASE_LIST_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_LIST;
    public static final String PURCHASE_LIST_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_LIST;
    public static final String PURCHASE_LIST_CONTENT_COUNT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_LIST;
    public static final String PURCHASE_ITEM_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ITEM;
    public static final String PURCHASE_ITEM_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PURCHASE_ITEM;
    public static final String PLACE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PLACE;
    public static final String PLACE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_PLACE;
    public static final String FRIEND_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_FRIEND;
    public static final String FRIEND_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_FRIEND;

    private static final UriMatcher sUriMatcher = initUriMatcher();
    private SqlDbHelper dbHelper;

    public ShoppingContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        dbHelper = SqlDbHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType: " + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS:
                return GOODS_CONTENT_TYPE;
            case URI_GOODS_ID:
                return GOODS_CONTENT_ITEM_TYPE;
            case URI_PURCHASE_LIST:
                return PURCHASE_LIST_CONTENT_TYPE;
            case URI_PURCHASE_LIST_ID:
                return PURCHASE_LIST_CONTENT_ITEM_TYPE;
            case URI_PURCHASE_LIST_COUNT:
                return PURCHASE_LIST_CONTENT_COUNT_TYPE;
            case URI_PURCHASE_ITEM:
                return PURCHASE_ITEM_CONTENT_TYPE;
            case URI_PURCHASE_ITEM_ID:
                return PURCHASE_ITEM_CONTENT_ITEM_TYPE;
            case URI_PLACE:
                return PLACE_CONTENT_TYPE;
            case URI_PLACE_ID:
                return PLACE_CONTENT_ITEM_TYPE;
            case URI_FRIEND:
                return FRIEND_CONTENT_TYPE;
            case URI_FRIEND_ID:
                return FRIEND_CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + uri.toString());
        Cursor cursor;
        String[] args;
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS:
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_GOODS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_GOODS_ID:
                args = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_GOODS,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PURCHASE_LIST:
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PURCHASE_LIST,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PURCHASE_LIST_ID:
                args = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PURCHASE_LIST,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PURCHASE_LIST_COUNT:
                args = new String[]{"0", "0"};
                String SQL_QUERY = "SELECT COUNT(*) FROM "
                        + SqlDbHelper.TABLE_PURCHASE_LIST + " WHERE "
                        + SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID + "!=? OR "
                        + SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID + "!=?";
                cursor = dbHelper.getReadableDatabase().rawQuery(
                        SQL_QUERY,
                        args
                );
                break;
            case URI_PURCHASE_ITEM:
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PURCHASE_ITEM,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PURCHASE_ITEM_ID:
                args = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PURCHASE_ITEM,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PLACE:
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PLACES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_PLACE_ID:
                args = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_PLACES,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_FRIEND:
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_FRIENDS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case URI_FRIEND_ID:
                args = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        SqlDbHelper.TABLE_FRIENDS,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert: " + uri.toString());
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri resultUri = null;
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS: {
                long rowID = db.insert(SqlDbHelper.TABLE_GOODS, null, values);
                if (rowID > 0) {
                    resultUri = ContentUris.withAppendedId(GOODS_CONTENT_URI, rowID);
                }
                break;
            }
            case URI_PURCHASE_LIST: {
                long rowID = db.insert(SqlDbHelper.TABLE_PURCHASE_LIST, null, values);
                if (rowID > 0) {
                    resultUri = ContentUris.withAppendedId(PURCHASE_LIST_CONTENT_URI, rowID);
                }
                break;
            }
            case URI_PURCHASE_ITEM: {
                long rowID = db.insert(SqlDbHelper.TABLE_PURCHASE_ITEM, null, values);
                if (rowID > 0) {
                    resultUri = ContentUris.withAppendedId(PURCHASE_ITEM_CONTENT_URI, rowID);
                }
                break;
            }
            case URI_PLACE: {
                long rowID = db.insert(SqlDbHelper.TABLE_PLACES, null, values);
                if (rowID > 0) {
                    resultUri = ContentUris.withAppendedId(PLACE_CONTENT_URI, rowID);
                }
                break;
            }
            case URI_FRIEND: {
                long rowID = db.insert(SqlDbHelper.TABLE_FRIENDS, null, values);
                if (rowID > 0) {
                    resultUri = ContentUris.withAppendedId(FRIEND_CONTENT_URI, rowID);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (resultUri == null) {
            throw new android.database.SQLException("Failed to insert row into: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update: " + uri.toString());
        final String table;
        final String sel;
        final String[] args;
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS:
                table = SqlDbHelper.TABLE_GOODS;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_GOODS_ID:
                table = SqlDbHelper.TABLE_GOODS;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PURCHASE_LIST:
                table = SqlDbHelper.TABLE_PURCHASE_LIST;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PURCHASE_LIST_ID:
                table = SqlDbHelper.TABLE_PURCHASE_LIST;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PURCHASE_ITEM:
                table = SqlDbHelper.TABLE_PURCHASE_ITEM;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PURCHASE_ITEM_ID:
                table = SqlDbHelper.TABLE_PURCHASE_ITEM;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PLACE:
                table = SqlDbHelper.TABLE_PLACES;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PLACE_ID:
                table = SqlDbHelper.TABLE_PLACES;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_FRIEND:
                table = SqlDbHelper.TABLE_FRIENDS;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_FRIEND_ID:
                table = SqlDbHelper.TABLE_FRIENDS;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        rowsUpdated = dbHelper.getWritableDatabase().update(
                table,
                values,
                sel,
                args
        );
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: " + uri.toString());
        final String table;
        final String sel;
        final String[] args;
        int rowsDelete;
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS:
                table = SqlDbHelper.TABLE_GOODS;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_GOODS_ID:
                table = SqlDbHelper.TABLE_GOODS;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PURCHASE_LIST:
                table = SqlDbHelper.TABLE_PURCHASE_LIST;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PURCHASE_LIST_ID:
                table = SqlDbHelper.TABLE_PURCHASE_LIST;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PURCHASE_ITEM:
                table = SqlDbHelper.TABLE_PURCHASE_ITEM;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PURCHASE_ITEM_ID:
                table = SqlDbHelper.TABLE_PURCHASE_ITEM;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_PLACE:
                table = SqlDbHelper.TABLE_PLACES;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_PLACE_ID:
                table = SqlDbHelper.TABLE_PLACES;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            case URI_FRIEND:
                table = SqlDbHelper.TABLE_FRIENDS;
                sel = selection;
                args = selectionArgs;
                break;
            case URI_FRIEND_ID:
                table = SqlDbHelper.TABLE_FRIENDS;
                sel = SqlDbHelper.COLUMN_ID + "=?";
                args = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        rowsDelete = dbHelper.getWritableDatabase().delete(
                table,
                sel,
                args
        );
        if (rowsDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDelete;
    }

    static UriMatcher initUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIEND, URI_FRIEND);
        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIEND + "/#", URI_FRIEND_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST, URI_PURCHASE_LIST);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST + "/#", URI_PURCHASE_LIST_ID);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST_APPOINTMENT + "/#", URI_PURCHASE_LIST_ID);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST + "/" + PATH_COUNT + "/" + PATH_WITH_PLACE, URI_PURCHASE_LIST_COUNT);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM, URI_PURCHASE_ITEM);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM + "/#", URI_PURCHASE_ITEM_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS, URI_GOODS);
        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS + "/#", URI_GOODS_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACE, URI_PLACE);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACE + "/#", URI_PLACE_ID);

        return matcher;
    }
}
