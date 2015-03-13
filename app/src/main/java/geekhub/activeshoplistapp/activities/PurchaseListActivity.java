package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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
public class PurchaseListActivity extends BaseActivity implements PurchaseListMainFragment.OnPurchaseListMainFragmentListener, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "PurchaseListActivity";
    private PurchaseListEditFragment purchaseListEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(this);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new PurchaseListMainFragment())
                    .commit();
        }

        getDrawerToggle().syncState();
    }

    @Override
    protected void onResume() {
        superOnResume();
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (TextUtils.isEmpty(sharedPrefHelper.getUserName())) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void onPurchaseListMainFragmentClickListener() {
        purchaseListEditFragment = new PurchaseListEditFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, purchaseListEditFragment)
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onPurchaseListMainFragmentClickListener(int id) {
        purchaseListEditFragment = PurchaseListEditFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, purchaseListEditFragment)
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount <= 0){
            getDrawerToggle().syncState();
            purchaseListEditFragment = null;
        }
    }

    @Override
    public void menuShowPurchaseLists() {
        if (purchaseListEditFragment != null) {
            purchaseListEditFragment.onBackPressed();
            purchaseListEditFragment = null;
        }
    }

    @Override
    public void menuLogout() {
        super.menuLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
