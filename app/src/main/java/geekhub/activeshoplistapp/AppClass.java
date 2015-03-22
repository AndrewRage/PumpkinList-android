package geekhub.activeshoplistapp;

import android.app.Application;
import android.util.Log;

import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
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
}
