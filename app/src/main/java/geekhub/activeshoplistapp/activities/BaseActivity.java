package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.DrawerMenuAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ActivityHelper;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 * 
 * Base activity
 */
public abstract class BaseActivity extends ActionBarActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private View drawerHeader;
    private boolean drawerHamburgerScreen = false;
    public ActivityHelper activityHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHelper = ActivityHelper.getInstance();
    }

    public void initDrawer() {
        List<Integer> menus = new ArrayList<>();
        menus.add(AppConstants.MENU_SHOW_PURCHASE_LIST);
        menus.add(AppConstants.MENU_SHOW_PURCHASE_ARCHIVE);
        menus.add(AppConstants.MENU_SHOW_SHOPS);
        menus.add(AppConstants.MENU_SHOW_PLACES);

        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.getOffLine()) {
            menus.add(AppConstants.MENU_LOGOUT);
        }

        final DrawerMenuAdapter drawerMenuAdapter = new DrawerMenuAdapter(this, R.layout.item_drawer_menu, menus);
        setContentView(R.layout.activity_with_fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        drawerHeader = inflater.inflate(R.layout.item_drawer_header, drawerListView, false);
        drawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing
            }
        });
        drawerListView.addHeaderView(drawerHeader);
        drawerListView.setAdapter(drawerMenuAdapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    drawerLayout.closeDrawer(drawerListView);
                    activityHelper.setGlobalId(drawerMenuAdapter.getItem(position - 1));
                    menuOnClick(drawerMenuAdapter.getItem(position - 1));
                    int menuId = drawerMenuAdapter.getItem(position - 1);
                    switch (menuId) {
                        case AppConstants.MENU_LOGOUT:
                            menuLogout();
                            break;
                        case AppConstants.MENU_SHOW_PURCHASE_LIST:
                            menuShowPurchaseLists(AppConstants.MENU_SHOW_PURCHASE_LIST);
                            break;
                        case AppConstants.MENU_SHOW_PURCHASE_ARCHIVE:
                            menuShowPurchaseLists(AppConstants.MENU_SHOW_PURCHASE_ARCHIVE);
                            break;
                        case AppConstants.MENU_SHOW_SHOPS:
                            menuManageShop();
                            break;
                        case AppConstants.MENU_SHOW_PLACES:
                            menuManagePlaces();
                            break;
                    }
                }
            }
        });
    }

    public void setHamburgerScreen(boolean hamburger) {
        drawerHamburgerScreen = hamburger;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (activityHelper.getPurchaseMenuId() == AppConstants.MENU_SHOW_PURCHASE_LIST) {
            setHamburgerScreen(true);
        } else {
            setHamburgerScreen(false);
        }

        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.isLogin()) {
            finish();
        }
    }

    protected void superOnResume() {
        super.onResume();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                if(drawerHamburgerScreen && drawerLayout != null && backStackEntryCount <= 0){
                    if (drawerLayout.isDrawerOpen(drawerListView)) {
                        drawerLayout.closeDrawer(drawerListView);
                    } else {
                        drawerLayout.openDrawer(drawerListView);
                    }
                } else {
                    onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (drawerLayout != null && drawerLayout.isDrawerOpen(drawerListView)) {
            drawerLayout.closeDrawer(drawerListView);
        } else if (activityHelper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_ARCHIVE
                && backStackEntryCount <= 0) {
            activityHelper.setGlobalId(AppConstants.MENU_SHOW_PURCHASE_LIST);
            menuShowPurchaseLists(AppConstants.MENU_SHOW_PURCHASE_LIST);
        } else if (interactionListener != null) {
            if (interactionListener.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    protected InteractionListener interactionListener;

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public interface InteractionListener {
        boolean onBackPressed();
    }

    public void menuOnClick(int menuId) {
    }

    public void menuLogout() {
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        sharedPrefHelper.setUserName(null);
        finish();
    }

    public void menuManageShop() {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_SHOPS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void menuManagePlaces() {
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_PLACES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void menuShowPurchaseLists(int menuId) {
        activityHelper.setPurchaseMenuId(menuId);
        finish();
        overridePendingTransition(0, 0);
    }
}
