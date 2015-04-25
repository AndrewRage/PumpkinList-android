package geekhub.activeshoplistapp;

import android.app.Application;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.services.GpsAppointmentService;
import io.fabric.sdk.android.Fabric;

/**
 * Created by rage on 06.02.15.
 *
 * Application Class
 */
public class AppClass extends Application {

    private static final String TAG = AppClass.class.getSimpleName();
    private Handler handler;
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SharedPrefHelper.getInstance(getApplicationContext());
        SqlDbHelper.getInstance(getApplicationContext());

        readCount();

        handler = new Handler();

        getContentResolver().registerContentObserver(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                true,
                contentObserver
        );

    }

    private ContentObserver contentObserver = new ContentObserver(handler) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, "onChange: " + uri.toString());
            readCount();
        }
    };

    private void readCount() {
        Cursor cursor = getContentResolver().query(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_COUNT_WITH_PLACE_URI,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        Log.d(TAG, "COUNT: " + count);

        if (this.count == 0 && count > 0) {
            startGpsService(true);
        } /*else if (this.count > 0 && count ==0 ) {
            startGpsService(false);
        }*/
        this.count = count;
    }

    private void startGpsService(boolean start) {
        Log.d(TAG, "GpsAppointment - start: " + start);
        Intent intent = new Intent(getApplicationContext(), GpsAppointmentService.class);
        if (start) {
            getApplicationContext().startService(intent);
        } else {
            getApplicationContext().stopService(intent);
        }
    }

}
