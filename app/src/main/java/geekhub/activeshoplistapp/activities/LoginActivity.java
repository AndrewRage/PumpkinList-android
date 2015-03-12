package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.LoginFragment;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Login screen
 */

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginFragmentListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (TextUtils.isEmpty(sharedPrefHelper.getUserName())) {
            //setContentView(R.layout.activity_with_fragment);
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
        Intent intent = new Intent(this, PurchaseListActivity.class);
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
