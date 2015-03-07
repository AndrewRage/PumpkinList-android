package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.PurchaseListEditFragment;
import geekhub.activeshoplistapp.fragments.PurchaseListMainFragment;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.DataBaseHelper;
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
                    .replace(R.id.container, new PurchaseListMainFragment())
                    .commit();
        }
    }

    @Override
    public void onPurchaseListMainFragmentClickListener() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PurchaseListEditFragment())
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onPurchaseListMainFragmentClickListener(int id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PurchaseListEditFragment.newInstance(id))
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }
}
