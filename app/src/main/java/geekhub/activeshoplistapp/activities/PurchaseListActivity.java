package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.LoginFragment;
import geekhub.activeshoplistapp.fragments.PurchaseListEditFragment;
import geekhub.activeshoplistapp.fragments.PurchaseListMainFragment;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 08.02.15.
 */
public class PurchaseListActivity extends BaseActivity implements PurchaseListMainFragment.OnPurchaseListMainFragmentListener{
    private static final String TAG = "PurchaseListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PurchaseListMainFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_purchase_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
            sharedPrefHelper.setUserName(null);
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPurchaseListMainFragmentClickListener() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PurchaseListEditFragment())
                .commit();
    }
}
