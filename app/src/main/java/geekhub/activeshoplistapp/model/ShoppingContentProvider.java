package geekhub.activeshoplistapp.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ShoppingContentProvider extends ContentProvider {
    private static final String TAG = ShoppingContentProvider.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "geekhub.activeshoplistapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_GOODS = "goods";
    public static final String PATH_PURCHASE_LIST = "purchase_list";
    public static final String PATH_PURCHASE_ITEM = "purchase_item";
    public static final String PATH_FRIENDS = "friends";
    public static final String PATH_PLACES = "friends";

    static final int URI_GOODS = 100;
    static final int URI_GOODS_ID = 101;
    static final int URI_PURCHASE_LIST = 200;
    static final int URI_PURCHASE_LIST_ID = 201;
    static final int URI_PURCHASE_ITEM = 300;
    static final int URI_PURCHASE_ITEM_ID = 301;
    static final int URI_PLACES = 400;
    static final int URI_PLACES_ID = 401;
    static final int URI_FRIENDS = 500;
    static final int URI_FRIENDS_ID = 501;

    private static final UriMatcher sUriMatcher = initUriMatcher();

    public ShoppingContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static UriMatcher initUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIENDS, URI_FRIENDS);
        matcher.addURI(CONTENT_AUTHORITY, PATH_FRIENDS + "/#", URI_FRIENDS_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST, URI_PURCHASE_LIST);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_LIST + "/#", URI_PURCHASE_LIST_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM, URI_PURCHASE_ITEM);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PURCHASE_ITEM + "/#", URI_PURCHASE_ITEM_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS, URI_GOODS);
        matcher.addURI(CONTENT_AUTHORITY, PATH_GOODS + "/#", URI_GOODS_ID);

        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACES, URI_PLACES);
        matcher.addURI(CONTENT_AUTHORITY, PATH_PLACES + "/#", URI_PLACES_ID);

        return matcher;
    }
}
