package geekhub.activeshoplistapp;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Application Class
 */
public class AppClass extends Application {

    private static final String TAG = AppClass.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefHelper.getInstance(getApplicationContext());
        SqlDbHelper.getInstance(getApplicationContext());
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
            return new CursorLoader(
                    getApplicationContext(),
                    ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                    projection,
                    null,
                    null,
                    orderBy
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
}
