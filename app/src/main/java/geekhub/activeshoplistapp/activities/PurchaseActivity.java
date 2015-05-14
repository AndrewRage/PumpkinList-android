package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.asyncs.AddNewListTask;
import geekhub.activeshoplistapp.asyncs.UpdateListTask;
import geekhub.activeshoplistapp.fragments.PurchaseEditFragment;
import geekhub.activeshoplistapp.fragments.PurchaseManageFragment;
import geekhub.activeshoplistapp.helpers.ActivityHelper;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 08.02.15.
 */
public class PurchaseActivity extends BaseActivity implements PurchaseManageFragment.OnPurchaseListMainFragmentListener, PurchaseEditFragment.OnPurchaseEditFragmentListener, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = PurchaseActivity.class.getSimpleName();
    private PurchaseEditFragment purchaseListEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.getInstance().setIsMainActivity(true);
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
        ActivityHelper.getInstance().setIsMainActivity(true);
        superOnResume();
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.isLogin()) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (activityHelper.getGlobalId() != activityHelper.getPurchaseMenuId()) {
            if (activityHelper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_LIST
                    || activityHelper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_ARCHIVE) {
                menuShowPurchaseLists(activityHelper.getGlobalId());
            } else {
                activityHelper.setGlobalId(activityHelper.getPurchaseMenuId());
            }
        }
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
        activityHelper.setGlobalId(activityHelper.getPurchaseMenuId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PurchaseManageFragment())
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
            purchaseListEditFragment = null;
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
        activityHelper.setPurchaseMenuId(menuId);
        showPurchaseLists();
    }

    @Override
    public void menuLogout() {
        super.menuLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void addNewList(PurchaseListModel purchaseList) {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
            new AddNewListTask(this, purchaseList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AddNewListTask(this, purchaseList).execute();
        }
    }

    @Override
    public void updateList(PurchaseListModel purchaseList) {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
            new UpdateListTask(purchaseList, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new UpdateListTask(purchaseList, this).execute();
        }
    }
}
