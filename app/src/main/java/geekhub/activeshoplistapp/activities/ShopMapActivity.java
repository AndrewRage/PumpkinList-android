package geekhub.activeshoplistapp.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private EditText shopNameEdit;
    private boolean isOnceShowMyLocation = false;
    private Marker marker;

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

        shopNameEdit = (EditText) findViewById(R.id.title);

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
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!isOnceShowMyLocation) {
                    showMyLocation(location);
                    isOnceShowMyLocation = true;
                }
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(true));
                } else {
                    marker.setPosition(latLng);
                }
            }
        });
    }

    private void showMyLocation(Location location) {
        map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()),
                        17
                )
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (marker != null) {
            shop.setGpsLatitude(marker.getPosition().latitude);
            shop.setGpsLongitude(marker.getPosition().longitude);
            shop.setShopName(shopNameEdit.getText().toString());
            ShoppingHelper.getInstance().addShop(shop);
        }
    }
}
