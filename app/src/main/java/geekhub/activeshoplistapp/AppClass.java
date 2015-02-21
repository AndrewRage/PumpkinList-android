package geekhub.activeshoplistapp;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import io.fabric.sdk.android.Fabric;

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

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new Twitter(authConfig));
    }
}
