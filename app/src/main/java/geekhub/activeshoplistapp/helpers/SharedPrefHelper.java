package geekhub.activeshoplistapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by rage on 09.12.14.
 *
 *
 */
public class SharedPrefHelper {

    private static final String TAG = "AppLog SharedPrefHelper";

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
}
