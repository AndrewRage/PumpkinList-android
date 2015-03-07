package geekhub.activeshoplistapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.ShopsModel;

/**
 * Created by rage on 3/3/15.
 */
public class ShopMapActivity extends BaseActivity implements OnMapReadyCallback {
    private ShopsModel shop;
    private GoogleMap map;
    private EditText shopNameEdit;
    private boolean isEdit = false;
    private boolean isOnceShowMyLocation = false;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map = mapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        shopNameEdit = (EditText) findViewById(R.id.title);
        shopNameEdit.clearFocus();

        Intent args = getIntent();
        int id = -1;
        if (args != null) {
            id = args.getIntExtra(AppConstants.EXTRA_SHOP_ID, -1);
        }
        if (id >= 0) {
            shop = ShoppingHelper.getInstance().getShopsList().get(id);
            isEdit = true;
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            shop.getGpsLatitude(),
                            shop.getGpsLongitude()
                    ))
                    .draggable(true));
            shopNameEdit.setText(shop.getShopName());
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
                    if (!isEdit) {
                        showMyLocation(location);
                    } else {
                        LatLng latLng = new LatLng(shop.getGpsLatitude(), shop.getGpsLongitude());
                        showMyLocation(latLng);
                    }
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        showMyLocation(latLng);
    }

    private void showMyLocation(LatLng latLng) {
        map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 17)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_shop) {
            boolean isNeedDelet = false;
            if (isEdit) {
                isNeedDelet = true;
            } else if (marker != null || !TextUtils.isEmpty(shopNameEdit.getText().toString())) {
                isNeedDelet = true;
            }
            if (isNeedDelet) {
                String shopName = shopNameEdit.getText().toString();
                String message;
                if (TextUtils.isEmpty(shopName)) {
                    message = String.format(
                            getString(R.string.shop_edit_alert_delete_description),
                            getString(R.string.shop_edit_this_shop_default)
                    );
                } else {
                    message = String.format(
                            getString(R.string.shop_edit_alert_delete_description),
                            shopName
                    );
                }
                new AlertDialog.Builder(this)
                        .setTitle(R.string.shop_edit_alert_delete_title)
                        .setMessage(message)
                        .setPositiveButton(R.string.shop_edit_alert_delete_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (isEdit) {
                                    ShoppingHelper.getInstance().deleteShop(shop);
                                }
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.shop_edit_alert_delete_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            } else {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!isEdit && marker != null) {
            if (TextUtils.isEmpty(shopNameEdit.getText().toString())) {
                shop.setShopName(getString(R.string.shop_edit_new_shop_default));
            } else {
                shop.setShopName(shopNameEdit.getText().toString());
            }
            shop.setGpsLatitude(marker.getPosition().latitude);
            shop.setGpsLongitude(marker.getPosition().longitude);
            ShoppingHelper.getInstance().addShop(shop);
        }
        if (isEdit) {
            boolean edit = false;
            if (shop.getGpsLatitude() != marker.getPosition().latitude
                    && shop.getGpsLongitude() != marker.getPosition().longitude) {
                edit = true;
                shop.setGpsLatitude(marker.getPosition().latitude);
                shop.setGpsLongitude(marker.getPosition().longitude);
            }
            if (!TextUtils.equals(shopNameEdit.getText().toString(), shop.getShopName())
                    || TextUtils.isEmpty(shopNameEdit.getText().toString())) {
                edit = true;
                if (TextUtils.isEmpty(shopNameEdit.getText().toString())) {
                    shop.setShopName(getString(R.string.shop_edit_new_shop_default));
                } else {
                    shop.setShopName(shopNameEdit.getText().toString());
                }
            }
            if (edit) {
                ShoppingHelper.getInstance().updateShop(shop);
            }
        }
        super.onBackPressed();
    }
}
