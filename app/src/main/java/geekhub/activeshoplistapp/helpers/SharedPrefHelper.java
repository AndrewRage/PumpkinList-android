package geekhub.activeshoplistapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by rage on 09.12.14.
 *
 *
 */
public class SharedPrefHelper {
    private static final String TAG = SharedPrefHelper.class.getSimpleName();

    private static SharedPrefHelper sharedPrefHelper;
    private SharedPreferences preferences;

    private SharedPrefHelper(Context context){
        preferences = context.getSharedPreferences(AppConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPrefHelper getInstance(Context context){
        if (sharedPrefHelper == null) {
            sharedPrefHelper = new SharedPrefHelper(context);
        }
        return sharedPrefHelper;
    }

    public static SharedPrefHelper getInstance(){
        return sharedPrefHelper;
    }

    public void setUserName(String userName){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.APP_PREFERENCES_USERNAME, userName);
        editor.apply();
    }

    public String getUserName(){
        String userName = null;
        if (preferences.contains(AppConstants.APP_PREFERENCES_USERNAME)) {
            userName = preferences.getString(AppConstants.APP_PREFERENCES_USERNAME, null);
        }
        return userName;
    }

    public void setOffLine(boolean offLine){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AppConstants.APP_PREFERENCES_OFFLINE, offLine);
        editor.apply();
    }

    public boolean getOffLine(){
        if (preferences.contains(AppConstants.APP_PREFERENCES_OFFLINE)) {
            return preferences.getBoolean(AppConstants.APP_PREFERENCES_OFFLINE, false);
        } else {
            return false;
        }
    }

    public boolean isLogin(){
        if (!TextUtils.isEmpty(getUserName()) | getOffLine()) {
            return true;
        } else {
            return false;
        }
    }

}
