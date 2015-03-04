package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.ShopsModel;

/**
 * Created by rage on 3/3/15.
 */
public class ShopMapActivity extends BaseActivity implements OnMapReadyCallback {
    private ShopsModel shop;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map = mapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
