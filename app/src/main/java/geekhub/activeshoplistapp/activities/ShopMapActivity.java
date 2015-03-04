package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.ShopsModel;

/**
 * Created by rage on 3/3/15.
 */
public class ShopMapActivity extends BaseActivity {
    private ShopsModel shop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);

        Intent args = getIntent();
        int id = 0;
        if (args != null) {
            id = args.getIntExtra(AppConstants.EXTRA_SHOP_ID, 0);
        }
        if (id > 0) {
            shop = ShoppingHelper.getInstance().getShopsList().get(id);
        } else {
            shop = new ShopsModel();
        }
    }

}
