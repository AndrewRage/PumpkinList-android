package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import geekhub.activeshoplistapp.fragments.LoginFragment;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Login screen
 */

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginFragmentListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();

        //This only for offline demo app
        sharedPrefHelper.setOffLine(true);

        if (!sharedPrefHelper.isLogin()) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new LoginFragment())
                        .commit();
            }
        } else {
            openApplication();
        }
    }

    private void openApplication() {
        finish();
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        superOnResume();
    }

    @Override
    public void onLoginFragmentClickListener(int button) {
        openApplication();
    }
}
