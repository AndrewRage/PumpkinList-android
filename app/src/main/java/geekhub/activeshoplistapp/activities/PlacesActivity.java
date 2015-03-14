package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.PlacesManageFragment;
import geekhub.activeshoplistapp.helpers.AppConstants;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesActivity extends BaseActivity {
    private static final String TAG = PlacesActivity.class.getSimpleName();
    private int menuItemId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent args = getIntent();
        if (args != null) {
            menuItemId = args.getExtras().getInt(AppConstants.EXTRA_MENU_ITEM);
        }
        initDrawer();
        if (savedInstanceState == null && menuItemId >= 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PlacesManageFragment.newInstance(menuItemId))
                    .commit();
        }
    }

    @Override
    public void menuShowPurchaseLists() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void menuManageShop() {
    }
}
