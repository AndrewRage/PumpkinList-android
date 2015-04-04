package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.PurchaseEditFragment;
import geekhub.activeshoplistapp.fragments.PurchaseManageFragment;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 08.02.15.
 */
public class PurchaseActivity extends BaseActivity implements PurchaseManageFragment.OnPurchaseListMainFragmentListener, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = PurchaseActivity.class.getSimpleName();
    private PurchaseEditFragment purchaseListEditFragment;
    private PurchaseManageFragment purchaseManageFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(this);
            showPurchaseLists();
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            long listDbId = intent.getExtras().getLong(AppConstants.NOTIFICATION_LIST_ARGS, -1);
            if (listDbId >= 0) {
                showList(listDbId);
            }
        }
    }

    @Override
    protected void onResume() {
        superOnResume();
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.isLogin()) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //getDrawerToggle().syncState();
        syncDrawerToggle();

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount <= 0){
            showPurchaseLists();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //getDrawerToggle().syncState();
        syncDrawerToggle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void onPurchaseListMainFragmentClickListener() {
        purchaseListEditFragment = new PurchaseEditFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, purchaseListEditFragment)
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onPurchaseListMainFragmentClickListener(long id) {
        showList(id);
    }

    private void showPurchaseLists(){
        purchaseManageFragment = null;
        purchaseManageFragment = PurchaseManageFragment.newInstance(purchaseActivityHelper.getMenuId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, purchaseManageFragment)
                .commit();
    }

    private void showList(long id) {
        if (purchaseListEditFragment != null) {
            purchaseListEditFragment.onBackPressed();
            getSupportFragmentManager().popBackStack();
            purchaseListEditFragment = null;
        }
        purchaseListEditFragment = PurchaseEditFragment.newInstance(id);
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

    private void syncDrawerToggle(){
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount <= 0){
            getDrawerToggle().syncState();
        }
    }

    @Override
    public void menuOnClick(int menuId) {
        super.menuOnClick(menuId);
        if (menuId != AppConstants.MENU_LOGOUT && purchaseListEditFragment != null) {
            purchaseListEditFragment.onBackPressed();
            purchaseListEditFragment = null;
        }
    }

    @Override
    public void menuShowPurchaseLists(int menuId) {
        purchaseActivityHelper.setMenuId(menuId);
        showPurchaseLists();
    }

    @Override
    public void menuLogout() {
        super.menuLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
