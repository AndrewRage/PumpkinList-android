package geekhub.activeshoplistapp.model;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import geekhub.activeshoplistapp.helpers.SqlDbHelper;

public class ShoppingContentProvider extends ContentProvider {
    private static final String TAG = ShoppingContentProvider.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "geekhub.activeshoplistapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_GOODS = "goods";
    public static final String PATH_PURCHASE_LIST = "purchase_list";
    public static final String PATH_PURCHASE_ITEM = "purchase_item";
    public static final String PATH_FRIEND = "friend";
    public static final String PATH_PLACE = "place";

    public static final Uri GOODS_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_GOODS);
    public static final Uri PURCHASE_LIST_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_LIST);
    public static final Uri PURCHASE_ITEM_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PURCHASE_ITEM);
    public static final Uri FRIEND_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_FRIEND);
    public static final Uri PLACE_CONTENT_URI = Uri.parse(BASE_CONTENT_URI + "/" + PATH_PLACE);

    static final int URI_GOODS = 100;
    static final int URI_GOODS_ID = 101;
    static final int URI_PURCHASE_LIST = 200;
    static final int URI_PURCHASE_LIST_ID = 201;
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
        dbHelper = SqlDbHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_GOODS:
                return GOODS_CONTENT_TYPE;
            case URI_GOODS_ID:
                return GOODS_CONTENT_ITEM_TYPE;
            case URI_PURCHASE_LIST:
                return PURCHASE_LIST_CONTENT_TYPE;
            case URI_PURCHASE_LIST_ID:
                return PURCHASE_LIST_CONTENT_ITEM_TYPE;
            case URI_PURCHASE_ITEM:
                return PURCHASE_ITEM_CONTENT_TYPE;
            case URI_PURCHASE_ITEM_ID:
                return PURCHASE_LIST_CONTENT_ITEM_TYPE;
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
        Cursor cursor = null;
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
                        SqlDbHelper.COLUMN_ID,
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
                        SqlDbHelper.COLUMN_ID,
                        args,
                        null,
                        null,
                        sortOrder
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
                        SqlDbHelper.COLUMN_ID,
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
                        SqlDbHelper.COLUMN_ID,
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
                        SqlDbHelper.COLUMN_ID,
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
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static UriMatcher initUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIEND, URI_FRIEND);
        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIEND + "/#", URI_FRIEND_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST, URI_PURCHASE_LIST);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST + "/#", URI_PURCHASE_LIST_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM, URI_PURCHASE_ITEM);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM + "/#", URI_PURCHASE_ITEM_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS, URI_GOODS);
        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS + "/#", URI_GOODS_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACE, URI_PLACE);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACE + "/#", URI_PLACE_ID);

        return matcher;
    }
}
