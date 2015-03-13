package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.DrawerMenuAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 06.02.15.
 * 
 * Base activity
 */
public abstract class BaseActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private boolean drawerHamburgerScreen = false;

    public void initDrawer() {
        List<Integer> menus = new ArrayList<>();
        menus.add(AppConstants.MENU_SHOW_PURCHASE_LIST);
        menus.add(AppConstants.MENU_SHOW_SHOPS);
        menus.add(AppConstants.MENU_LOGOUT);
        final DrawerMenuAdapter drawerMenuAdapter = new DrawerMenuAdapter(this, R.layout.drawer_menu_item, menus);
        setContentView(R.layout.activity_with_fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

        };
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(drawerMenuAdapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(drawerListView);
                int menuId = drawerMenuAdapter.getItem(position);
                switch (menuId) {
                    case AppConstants.MENU_LOGOUT:
                        menuLogout();
                        break;
                    case AppConstants.MENU_SHOW_PURCHASE_LIST:
                        menuShowPurchaseLists();
                        break;
                    case AppConstants.MENU_SHOW_SHOPS:
                        menuManageShop();
                        break;
                }
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        drawerHamburgerScreen = true;
        return drawerToggle;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (TextUtils.isEmpty(sharedPrefHelper.getUserName())) {
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
        if (drawerLayout != null && drawerLayout.isDrawerOpen(drawerListView)) {
            drawerLayout.closeDrawer(drawerListView);
        } else if (onBackPressedListener != null) {
            if (onBackPressedListener.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public interface OnBackPressedListener {
        public boolean onBackPressed();
    }

    public void menuLogout() {
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        sharedPrefHelper.setUserName(null);
        finish();
    }

    public void menuManageShop() {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void menuShowPurchaseLists() {
    }
}
