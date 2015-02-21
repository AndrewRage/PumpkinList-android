package geekhub.activeshoplistapp.activities;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import java.io.IOException;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.LoginFragment;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Login screen
 */

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginFragmentListener {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (TextUtils.isEmpty(sharedPrefHelper.getUserName())) {
            setContentView(R.layout.activity_with_fragment);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        } else {
            openApplication();
        }
        VKSdk.initialize(vkSdkListener, getString(R.string.vkontakte_app_id)/*, VKAccessToken.tokenFromSharedPreferences(this, sTokenKey)*/);
    }

    private void openApplication() {
        finish();
        Intent intent = new Intent(this, PurchaseListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);

        if (requestCode == AppConstants.REQUEST_CODE_GOOGLE_PLUS_AUTH && resultCode == RESULT_OK) {
            final String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            AsyncTask<Void, Void, String> getToken = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;
                    try {
                        token = GoogleAuthUtil.getToken(LoginActivity.this, accountName,
                                AppConstants.G_PLUS_SCOPES);
                    } catch (UserRecoverableAuthException userAuthEx) {
                        Log.d(TAG, "UserRecoverableAuthException");
                        startActivityForResult(userAuthEx.getIntent(), AppConstants.REQUEST_CODE_GOOGLE_PLUS_AUTH);
                    }  catch (IOException ioEx) {
                        Log.d(TAG, "IOException");
                    }  catch (GoogleAuthException fatalAuthEx)  {
                        Log.d(TAG, "Fatal Authorization Exception" + fatalAuthEx.getLocalizedMessage());
                    }
                    return token;
                }

                @Override
                protected void onPostExecute(String token) {
                    Log.d(TAG, "Google+ token: " + token);
                }

            };
            getToken.execute();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    private VKSdkListener vkSdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
            Log.d(TAG, "onCaptchaError");
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(AppConstants.VKONTAKTE_SCOPES);
            Log.d(TAG, "onTokenExpired");
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(authorizationError.errorMessage)
                    .show();
            Log.d(TAG, "onAccessDenied");
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(LoginActivity.this, AppConstants.VKONTAKTE_PREFERENCES);
            Log.d(TAG, "onReceiveNewToken: " + newToken.accessToken);
            /*Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);*/
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Log.d(TAG, "onAcceptUserToken: " + token.accessToken);
            /*Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);*/
        }
    };

    @Override
    public void onLoginFragmentClickListener(int button) {
        switch (button) {
            case AppConstants.LOGIN_BUTTON:
                openApplication();
                break;
            case AppConstants.LOGIN_G_PLUS_BUTTON:
                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                        false, null, null, null, null);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_GOOGLE_PLUS_AUTH);
                break;
            case AppConstants.LOGIN_VKONTAKTE_BUTTON:
                VKSdk.authorize(AppConstants.VKONTAKTE_SCOPES, true, false);
                break;
        }
    }
}
