package geekhub.activeshoplistapp;

import android.app.Application;
import android.util.Log;

import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Application Class
 */
public class AppClass extends Application {

    private static final String TAG = "AppLog AppClass";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefHelper.getInstance(getApplicationContext());
    }
}
