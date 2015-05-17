package geekhub.activeshoplistapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;

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
    private View doneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeNameEdit = (EditText) findViewById(R.id.title);
        placeNameEdit.clearFocus();

        doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker == null) {
                    new AlertDialog.Builder(MapActivity.this)
                            .setTitle(getString(R.string.create_alert_title))
                            .setMessage(getString(R.string.create_alert_massage))
                            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //nothing
                                }
                            })
                            .show();
                } else {
                    onBackPressed();
                }
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

        //fix map crash - need test!
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setOnMarkerDragListener(markerDragListener);

        if (placesModel.getGpsLatitude() > 0 && placesModel.getGpsLongitude() > 0) {
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            placesModel.getGpsLatitude(),
                            placesModel.getGpsLongitude()
                    ))
                    .draggable(false)); //TODO draggable

            LatLng latLng = new LatLng(placesModel.getGpsLatitude(), placesModel.getGpsLongitude());
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
                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .draggable(false)); //TODO draggable
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

}
