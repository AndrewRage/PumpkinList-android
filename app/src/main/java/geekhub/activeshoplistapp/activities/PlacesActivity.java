package geekhub.activeshoplistapp.activities;

import android.os.Bundle;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.ShopsManageFragment;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesActivity extends BaseActivity {
    private static final String TAG = PlacesActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ShopsManageFragment())
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
