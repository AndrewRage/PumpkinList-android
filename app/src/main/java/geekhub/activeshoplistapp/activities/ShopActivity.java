package geekhub.activeshoplistapp.activities;

import android.os.Bundle;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.fragments.ShopsManageFragment;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class ShopActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ShopsManageFragment())
                    .commit();
        }
    }

}
