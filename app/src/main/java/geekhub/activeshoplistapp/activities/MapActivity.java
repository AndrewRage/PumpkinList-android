package geekhub.activeshoplistapp.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.services.GeoLocationService;

/**
 * Created by rage on 3/3/15.
 *
 * Used for show and choose places on map
 */
public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String ID_ARG = "arg_id";
    private static final String PLACE_STATE = "placeState";
    private static final String IS_EDIT_STATE = "isEditState";
    private int menuItemId = -1;
    private long dbId = -1;
    private PlacesModel placesModel;
    private GoogleMap map;
    private EditText placeNameEdit;
    private boolean isEdit = false;
    private boolean isOnceShowMyLocation = false;
    private Marker marker;
    private GoogleMap.OnMarkerDragListener markerDragListener;
    private boolean needSave = false;
    private EditText editSearch;
    private View progressBar;
    private GeoBroadcast geoBroadcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        geoBroadcast = new GeoBroadcast();
        IntentFilter intentFilter = new IntentFilter(GeoLocationService.GEO_LOCATION_BROADCAST);
        registerReceiver(geoBroadcast, intentFilter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check_white_24dp);

        placeNameEdit = (EditText) findViewById(R.id.title);
        placeNameEdit.clearFocus();

        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocationAction(editSearch.getText().toString());
            }
        });

        editSearch = (EditText) findViewById(R.id.edit_search);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchLocationAction(editSearch.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        Intent args = getIntent();

        if (args != null) {
            menuItemId = args.getExtras().getInt(AppConstants.EXTRA_MENU_ITEM);
            dbId = args.getExtras().getLong(AppConstants.EXTRA_PLACE_ID, -1);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        placesModel = savedInstanceState.getParcelable(PLACE_STATE);
        isEdit = savedInstanceState.getBoolean(IS_EDIT_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (placesModel != null) {
            initScreen();
        } else if (dbId >= 0) {
            Bundle bundle = new Bundle();
            bundle.putLong(ID_ARG, dbId);
            getSupportLoaderManager().initLoader(0, bundle, loaderCallbacks);
        } else {
            placesModel = new PlacesModel();
            if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
                placesModel.setCategory(AppConstants.PLACES_SHOP);
            } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
                placesModel.setCategory(AppConstants.PLACES_USER);
            } else {
                finish();
            }
            initScreen();
        }
        if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
            placeNameEdit.setHint(R.string.shop_edit_name_hint);
        } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
            placeNameEdit.setHint(R.string.place_edit_name_hint);
        }

        markerDragListener = new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d(TAG, "onMarkerDragEnd");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.d(TAG, "onMarkerDragEnd");
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d(TAG, "onMarkerDragEnd");
            }
        };
    }

    private void initScreen() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*map = mapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setOnMarkerDragListener(markerDragListener);*/
    }

    private void readPlaceModel(Cursor cursor) {
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
            int indexServerId = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_PLACES_ID);
            int indexCategory = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_CATEGORY);
            int indexName = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_NAME);
            int indexDescription = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_DESCRIPTION);
            int indexLatitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LATITUDE);
            int indexLongitude = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_LONGITUDE);
            int indexIsDelete = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_IS_DELETE);
            int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PLACES_COLUMN_TIMESTAMP);
            placesModel = new PlacesModel(
                    cursor.getLong(indexId),
                    cursor.getLong(indexServerId),
                    cursor.getLong(indexCategory),
                    cursor.getString(indexName),
                    cursor.getString(indexDescription),
                    cursor.getDouble(indexLatitude),
                    cursor.getDouble(indexLongitude),
                    cursor.getInt(indexIsDelete) > 0,
                    cursor.getLong(indexTimestamp)
            );
            cursor.close();
            isEdit = true;

            initScreen();

            placeNameEdit.setText(placesModel.getShopName());
        } else {
            finish();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        progressBar.setVisibility(View.GONE);
        //fix map crash - need test!
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setOnMarkerDragListener(markerDragListener);

        if (placesModel.getGpsLatitude() > 0 && placesModel.getGpsLongitude() > 0) {
            /*marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            placesModel.getGpsLatitude(),
                            placesModel.getGpsLongitude()
                    ))
                    .draggable(false));*/

            LatLng latLng = new LatLng(placesModel.getGpsLatitude(), placesModel.getGpsLongitude());
            addMarker(latLng);
            showMyLocation(latLng);
            isOnceShowMyLocation = true;
        }

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!isEdit && !isOnceShowMyLocation) {
                    showMyLocation(location);
                    isOnceShowMyLocation = true;
                }
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
                searchAddressAction(latLng);
            }
        });
    }

    private void addMarker(LatLng latLng) {
        if (map != null) {
            if (marker == null) {
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(false)); //TODO draggable
            } else {
                marker.setPosition(latLng);
            }
        }
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
            boolean isNeedDelete = false;
            if (isEdit) {
                isNeedDelete = true;
            } else if (marker != null || !TextUtils.isEmpty(placeNameEdit.getText().toString())) {
                isNeedDelete = true;
            }
            if (isNeedDelete) {
                String placeName = placeNameEdit.getText().toString();
                String message = "";
                if (TextUtils.isEmpty(placeName)) {
                    if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
                        message = String.format(
                                getString(R.string.shop_edit_alert_delete_description),
                                getString(R.string.shop_edit_this_shop_default)
                        );
                    } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
                        message = String.format(
                                getString(R.string.place_edit_alert_delete_description),
                                getString(R.string.place_edit_this_place_default)
                        );
                    }
                } else {
                    if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
                        message = String.format(
                                getString(R.string.shop_edit_alert_delete_description),
                                placeName
                        );
                    } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
                        message = String.format(
                                getString(R.string.place_edit_alert_delete_description),
                                placeName
                        );
                    }
                }
                String title = "";
                if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
                    title = getString(R.string.shop_edit_alert_delete_title);
                } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
                    title = getString(R.string.place_edit_alert_delete_title);
                }
                new AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (isEdit) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ContentHelper.deletePlace(MapActivity.this, placesModel.getDbId());
                                        }
                                    }).start();
                                }
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
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
            if (TextUtils.isEmpty(placeNameEdit.getText().toString())) {
                if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
                    placesModel.setShopName(getString(R.string.shop_edit_new_shop_default));
                } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
                    placesModel.setShopName(getString(R.string.place_edit_new_place_default));
                }
            } else {
                placesModel.setShopName(placeNameEdit.getText().toString());
            }
            placesModel.setGpsLatitude(marker.getPosition().latitude);
            placesModel.setGpsLongitude(marker.getPosition().longitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentHelper.insertPlace(MapActivity.this, placesModel);
                }
            }).start();
        } else if (!isEdit) {
            Toast.makeText(this, R.string.map_save_empty_location, Toast.LENGTH_SHORT).show();
        }
        if (isEdit) {
            savePosition();
        }
        super.onBackPressed();
    }

    private void savePosition() {
        if (isEdit) {
            if (marker != null
                    && (placesModel.getGpsLatitude() != marker.getPosition().latitude
                    || placesModel.getGpsLongitude() != marker.getPosition().longitude)) {
                needSave = true;
                placesModel.setGpsLatitude(marker.getPosition().latitude);
                placesModel.setGpsLongitude(marker.getPosition().longitude);
            }
            if (!TextUtils.equals(placeNameEdit.getText().toString(), placesModel.getShopName())
                    || TextUtils.isEmpty(placeNameEdit.getText().toString())) {
                needSave = true;
                if (TextUtils.isEmpty(placeNameEdit.getText().toString())) {
                    placesModel.setShopName(getString(R.string.shop_edit_new_shop_default));
                } else {
                    placesModel.setShopName(placeNameEdit.getText().toString());
                }
            }
            if (needSave) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ContentHelper.updatePlace(MapActivity.this, placesModel);
                    }
                }).start();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!isEdit) {
            if (placesModel != null) {
                placesModel.setShopName(placeNameEdit.getText().toString());
                if (marker != null) {
                    placesModel.setGpsLatitude(marker.getPosition().latitude);
                    placesModel.setGpsLongitude(marker.getPosition().longitude);
                }
                outState.putParcelable(PLACE_STATE, placesModel);
            }
            outState.putBoolean(IS_EDIT_STATE, isEdit);
        } else {
            savePosition();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(0);
        unregisterReceiver(geoBroadcast);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            long dbId = args.getLong(ID_ARG);
            Uri uri = Uri.parse(ShoppingContentProvider.PLACE_CONTENT_URI + "/" + dbId);
            String[] projection = {
                    SqlDbHelper.COLUMN_ID,
                    SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                    SqlDbHelper.PLACES_COLUMN_CATEGORY,
                    SqlDbHelper.PLACES_COLUMN_NAME,
                    SqlDbHelper.PLACES_COLUMN_DESCRIPTION,
                    SqlDbHelper.PLACES_COLUMN_LATITUDE,
                    SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                    SqlDbHelper.PLACES_COLUMN_IS_DELETE,
                    SqlDbHelper.PLACES_COLUMN_TIMESTAMP,
            };
            return new CursorLoader(
                    MapActivity.this,
                    uri,
                    projection,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            readPlaceModel(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void searchLocationAction(String searchString) {
        if (!TextUtils.isEmpty(searchString)) {
            hideSoftKeyboard();
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, GeoLocationService.class);
            intent.putExtra(GeoLocationService.SEARCH_STRING, searchString);
            startService(intent);
        }
    }

    private void searchAddressAction(LatLng latLng) {
        if (latLng != null) {
            hideSoftKeyboard();
            Intent intent = new Intent(this, GeoLocationService.class);
            intent.putExtra(GeoLocationService.LAT_LONG, latLng);
            startService(intent);
        }
    }

    class GeoBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            int taskId = intent.getExtras().getInt(GeoLocationService.TASK_ID, 0);
            Address address = intent.getExtras().getParcelable(GeoLocationService.ADDRESS_RESULT);
            if (taskId == GeoLocationService.GET_LOCATION) {
                if (address != null) {
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    addMarker(latLng);
                    showMyLocation(latLng);
                } else {
                    Toast.makeText(MapActivity.this, R.string.map_search_fail, Toast.LENGTH_SHORT).show();
                }
            }
            if (placesModel != null && TextUtils.isEmpty(placesModel.getShopName()) && address != null) {
                StringBuilder stringBuilder = new StringBuilder();
                boolean addCity = true;
                int count = address.getMaxAddressLineIndex();
                if (count > 2) {
                    count = 2;
                }
                for (int i = 0; i < count; i++) {
                    if (!TextUtils.equals(address.getAddressLine(i), address.getLocality())) {
                        addCity = false;
                    }
                }
                if (addCity) {
                    stringBuilder.append(address.getLocality());
                    if (address.getMaxAddressLineIndex() > 0) {
                        stringBuilder.append(", ");
                    }
                }
                if (address.getMaxAddressLineIndex() > 0) {
                    if (address.getMaxAddressLineIndex() > 1) {
                        stringBuilder.append(address.getAddressLine(1));
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(address.getAddressLine(0));
                }
                placeNameEdit.setText(stringBuilder.toString());
            }
        }
    }

}
