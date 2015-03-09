package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginFragmentClickListener(int button) {
        openApplication();
    }
}
