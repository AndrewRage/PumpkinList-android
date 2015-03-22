package geekhub.activeshoplistapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.PlacesActivity;
import geekhub.activeshoplistapp.adapters.PurchaseItemAdapter;
import geekhub.activeshoplistapp.adapters.SettingsSpinnerAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.model.ShoppingContentProvider;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseEditFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PurchaseEditFragment.class.getSimpleName();
    private static final String ARG_LIST_ID = "PurchaseList_param";
    private static final int REQUEST_SHOP = 10101;
    private static final int REQUEST_PLACES = 10102;
    private static final int LOADER_ITEM_ID = 0;
    private static final int LOADER_PLACE_ID = 1;
    private static final int LOADER_SHOP_ID = 2;
    private PurchaseItemAdapter adapter;
    private ListView purchaseListView;
    private View header;
    private PurchaseListModel purchaseList;
    private EditText listNameEdit;
    private EditText goodsLabelEdit;
    private View addItemButton;
    //private boolean isEdit;
    private Spinner shopsSpinner;
    private Spinner placeSpinner;
    private SettingsSpinnerAdapter shopSpinnerAdapter;
    private SettingsSpinnerAdapter placeSpinnerAdapter;
    private List<PlacesModel> shopsList;
    private List<PlacesModel> placesList;
    private View progressBar;

    public static PurchaseEditFragment newInstance(long purchaseListId) {
        PurchaseEditFragment fragment = new PurchaseEditFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_LIST_ID, purchaseListId);
        fragment.setArguments(args);
        return fragment;
    }

    public PurchaseEditFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_edit, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        purchaseListView = (ListView) view.findViewById(R.id.purchase_item_list);
        header = inflater.inflate(R.layout.purchase_edit_header, purchaseListView, false);
        goodsLabelEdit = (EditText) header.findViewById(R.id.edit_goods_label);
        addItemButton = header.findViewById(R.id.button_goods_add);

        listNameEdit = (EditText) view.findViewById(R.id.edit_list_name);
        shopsSpinner = (Spinner) view.findViewById(R.id.shops_spinner);
        placeSpinner = (Spinner) view.findViewById(R.id.place_spinner);

        progressBar = view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            long id = getArguments().getLong(ARG_LIST_ID);
            purchaseList = ContentHelper.getPurchaseList(getActivity(), id);
            listNameEdit.setText(purchaseList.getListName());
            //isEdit = true;
        } else {
            purchaseList = new PurchaseListModel();
            purchaseList.setTimeCreate(System.currentTimeMillis());
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            purchaseList.setPurchasesItems(purchaseItems);
            //isEdit = false;
        }

        //Cursor cursor = ContentHelper.getPurchaseItems(getActivity(), purchaseList.getDbId());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            adapter = new PurchaseItemAdapter(getActivity(), null, R.layout.item_purchase_edit);
        } else {
            adapter = new PurchaseItemAdapter(getActivity(), null, 0, R.layout.item_purchase_edit);
        }
        adapter.setOnItemInteractionListener(new PurchaseItemAdapter.OnItemInteractionListener() {
            @Override
            public void onCheckBoxClick(long dbId, boolean checked) {
                changeBought(dbId, checked);
            }
        });
        purchaseListView.addHeaderView(header);
        purchaseListView.setAdapter(adapter);
        purchaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog((Cursor) purchaseListView.getItemAtPosition(position));
            }
        });

        if (purchaseList.getDbId() != 0) {
            getLoaderManager().initLoader(LOADER_ITEM_ID, null, this);
            progressBar.setVisibility(View.VISIBLE);
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        //shopsList = new ArrayList<>();
        //initShopSpinner();
        getLoaderManager().initLoader(LOADER_SHOP_ID, null, this);

        //placesList = new ArrayList<>();
        //initPlaceSpinner();
        getLoaderManager().initLoader(LOADER_PLACE_ID, null, this);
    }

    private void showDialog(Cursor cursor) {
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID);
        int indexIsBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);
        int indexIsCancel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL);
        int indexGoodsId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID);
        int indexLabel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
        int indexQuantity = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY);
        int indexDescription = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION);
        int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP);
        final PurchaseItemModel item = new PurchaseItemModel(
                cursor.getLong(indexId),
                cursor.getLong(indexServerId),
                purchaseList.getDbId(),
                cursor.getInt(indexIsBought)>0,
                cursor.getInt(indexIsCancel)>0,
                cursor.getInt(indexGoodsId),
                cursor.getString(indexLabel),
                cursor.getFloat(indexQuantity),
                cursor.getString(indexDescription),
                cursor.getLong(indexTimestamp)
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.purchase_edit_item_title);
        View view = inflater.inflate(R.layout.dialog_purchase_item, null);
        final EditText name = (EditText) view.findViewById(R.id.edit_item_name);
        final CheckBox bought = (CheckBox) view.findViewById(R.id.check_bought);
        builder.setView(view);
        if (!item.isCancel()) {
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!TextUtils.isEmpty(name.getText().toString())) {
                        item.setGoodsLabel(name.getText().toString());
                    }
                    item.setBought(bought.isChecked());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseItem(getActivity(), item);
                        }
                    }).start();
                }
            });
        }
        builder.setNegativeButton(R.string.button_cancel, null);
        String deleteTitle;
        if (item.isCancel()) {
            deleteTitle = getString(R.string.purchase_edit_item_button_restore);
        } else {
            deleteTitle = getString(R.string.purchase_edit_item_button_delete);
        }
        builder.setNeutralButton(deleteTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (item.getServerId() == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.deletePurchaseItem(getActivity(), item.getDbId());
                        }
                    }).start();
                } else {
                    if (!TextUtils.isEmpty(name.getText().toString())) {
                        item.setGoodsLabel(name.getText().toString());
                    }
                    item.setBought(bought.isChecked());

                    item.setCancel(!item.isCancel());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseItem(getActivity(), item);
                        }
                    }).start();
                }
            }
        });
        Dialog dialog = builder.create();

        name.setText(item.getGoodsLabel());
        name.setEnabled(!item.isCancel());
        bought.setChecked(item.isBought());
        bought.setEnabled(!item.isCancel());

        dialog.show();
    }

    private void initShopSpinner() {
        shopSpinnerAdapter = new SettingsSpinnerAdapter(
                getActivity(),
                R.layout.item_settings_spinner,
                shopsList,
                R.string.shop_edit_shop_spinner_default_entry
        );
        shopSpinnerAdapter.setSettingsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopsSpinner.setEnabled(false);
                Intent intent = new Intent(getActivity(), PlacesActivity.class);
                intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_SHOPS);
                startActivityForResult(intent, REQUEST_SHOP);
            }
        });
        shopsSpinner.setAdapter(shopSpinnerAdapter);
        if (purchaseList.getShopId() != 0) {
            for (PlacesModel shop : shopsList) {
                if (purchaseList.getShopId() > 0) {
                    if (shop.getServerId() == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                } else if (purchaseList.getShopId() < 0) {
                    if ((shop.getDbId() * (-1) ) == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                }
            }
        }
        shopsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (shopSpinnerAdapter.getItem(position).getServerId() == 0) {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getDbId() * (-1));
                    } else {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getServerId());
                    }
                } else if (position == 0) {
                    purchaseList.setShopId(0);
                }
                if (purchaseList.getDbId() != 0) {
                    ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void initPlaceSpinner() {
        placeSpinnerAdapter = new SettingsSpinnerAdapter(
                getActivity(),
                R.layout.item_settings_spinner,
                placesList,
                R.string.shop_edit_place_spinner_default_entry
        );
        placeSpinnerAdapter.setSettingsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeSpinner.setEnabled(false);
                Intent intent = new Intent(getActivity(), PlacesActivity.class);
                intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_PLACES);
                startActivityForResult(intent, REQUEST_PLACES);
            }
        });
        placeSpinner.setAdapter(placeSpinnerAdapter);
        if (purchaseList.getPlaceId() != 0) {
            for (PlacesModel place : placesList) {
                if (purchaseList.getPlaceId() > 0) {
                    if (place.getServerId() == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                } else if (purchaseList.getPlaceId() < 0) {
                    if ((place.getDbId() * (-1) ) == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                }
            }
        }
        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (placeSpinnerAdapter.getItem(position).getServerId() == 0) {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getDbId() * (-1));
                    } else {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getServerId());
                    }
                } else if (position == 0) {
                    purchaseList.setPlaceId(0);
                }
                if (purchaseList.getDbId() != 0) {
                    ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsLabelEdit.requestFocus();
        shopsSpinner.setEnabled(true);
        placeSpinner.setEnabled(true);

        getActivity().getContentResolver().registerContentObserver(
                ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                true,
                contentObserver
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(contentObserver);
    }

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_purchase_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_list) {
            hideSoftKeyboard();
            deleteList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        if (purchaseList.getDbId() != 0) {
            if (!TextUtils.equals(listNameEdit.getText().toString(), purchaseList.getListName())) {
                updateList();
            }
        } else {
            if (purchaseList.getPurchasesItems().size() > 0
                    || !TextUtils.isEmpty(listNameEdit.getText().toString())) {
                addNewList();
            }
        }
        hideSoftKeyboard();
        getActivity().getSupportFragmentManager().popBackStack();
        return false;
    }

    private void addItem() {
        if (!TextUtils.isEmpty(goodsLabelEdit.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(purchaseList.getListName()) && TextUtils.isEmpty(listNameEdit.getText())) {
                purchaseList.setListName(getString(R.string.purchase_edit_new_list_default));
            }
            final String name = goodsLabelEdit.getText().toString();
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (purchaseList.getDbId() == 0) {
                        Uri uri = ContentHelper.insertPurchaseList(getActivity(), purchaseList);
                        purchaseList.setDbId(Long.parseLong(uri.getLastPathSegment()));
                    }
                    PurchaseItemModel item = new PurchaseItemModel(
                            0,
                            0,
                            0,
                            false,
                            false,
                            0,
                            name,
                            0,
                            "",
                            0
                    );
                    ContentHelper.insertPurchaseItem(getActivity(), item, purchaseList.getDbId());

                    if (adapter.getCursor() == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getLoaderManager().initLoader(LOADER_ITEM_ID, null, PurchaseEditFragment.this);
                            }
                        });
                    }
                }
            }).start();

            goodsLabelEdit.setText(null);
        }
    }

    private void updateList() {
        if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);
        }
        purchaseList.setListName(listNameEdit.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentHelper.updatePurchaseList(getActivity(), purchaseList);
            }
        }).start();
    }

    private void deleteList() {
        boolean isNeedDelete = false;
        if (purchaseList.getDbId() != 0) {
            isNeedDelete = true;
        } else {
            if (purchaseList.getPurchasesItems().size() > 0
                    || !TextUtils.isEmpty(listNameEdit.getText().toString())) {
                isNeedDelete = true;
            }
        }
        if (isNeedDelete) {
            String listName = listNameEdit.getText().toString();
            String message;
            if (TextUtils.isEmpty(listName)) {
                message = String.format(
                        getString(R.string.purchase_edit_alert_delete_description),
                        getString(R.string.purchase_edit_this_list_default)
                );
            } else {
                message = String.format(
                        getString(R.string.purchase_edit_alert_delete_description),
                        listName
                );
            }
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.purchase_edit_alert_delete_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (purchaseList.getDbId() != 0) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ContentHelper.deletePurchaseList(getActivity(), purchaseList);
                                    }
                                }).start();
                            }
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private Uri addNewList() {
        if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);
        }
        purchaseList.setListName(listNameEdit.getText().toString());
        return ContentHelper.insertPurchaseList(getActivity(), purchaseList);
    }

    /*private void updateItem(Cursor cursor) {
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID);
        int indexIsBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);
        int indexIsCancel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL);
        int indexGoodsId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID);
        int indexLabel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
        int indexQuantity = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY);
        int indexDescription = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION);
        int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP);
        PurchaseItemModel item = new PurchaseItemModel(
                cursor.getLong(indexId),
                cursor.getLong(indexServerId),
                purchaseList.getDbId(),
                cursor.getInt(indexIsBought)>0,
                cursor.getInt(indexIsCancel)>0,
                cursor.getInt(indexGoodsId),
                cursor.getString(indexLabel),
                cursor.getFloat(indexQuantity),
                cursor.getString(indexDescription),
                cursor.getLong(indexTimestamp)
        );
        int raw = ContentHelper.updatePurchaseItem(getActivity(), item);
        Log.d(TAG, "update: " + raw);
    }*/

    private void changeBought(final long dbId, final boolean checked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT, checked ? 1 : 0);
                getActivity().getContentResolver().update(
                        ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                        values,
                        SqlDbHelper.COLUMN_ID + "=?",
                        new String[]{Long.toString(dbId)}
                );
            }
        }).start();
    }

    private void refreshShopsList(Cursor cursor) {
        if (shopsList == null) {
            shopsList = new ArrayList<>();
        }
        if (shopsList.size() > 0) {
            shopsList.clear();
        }
        shopsList.addAll(ContentHelper.getPlaceList(cursor));
        if (shopSpinnerAdapter == null) {
            initShopSpinner();
        } else {
            shopSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void refreshPlacesList(Cursor cursor) {
        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        if (placesList.size() > 0) {
            placesList.clear();
        }
        placesList.addAll(ContentHelper.getPlaceList(cursor));
        if (placeSpinnerAdapter == null) {
            initPlaceSpinner();
        } else {
            placeSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SHOP) {
            getLoaderManager().restartLoader(LOADER_SHOP_ID, null, this);
        }
        if (requestCode == REQUEST_PLACES) {
            getLoaderManager().restartLoader(LOADER_PLACE_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ITEM_ID: {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP,
                };
                String orderBy = SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL + " ASC, "
                        + SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getActivity(),
                        ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                        projection,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID + "=?",
                        new String[]{Long.toString(purchaseList.getDbId())},
                        orderBy
                );
            }
            case LOADER_SHOP_ID: {
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
                String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getActivity(),
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        SqlDbHelper.PLACES_COLUMN_CATEGORY + "=?",
                        new String[]{Long.toString(AppConstants.PLACES_SHOP)},
                        orderBy
                );
            }
            case LOADER_PLACE_ID: {
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
                String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getActivity(),
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        SqlDbHelper.PLACES_COLUMN_CATEGORY + "=?",
                        new String[]{Long.toString(AppConstants.PLACES_USER)},
                        orderBy
                );
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ITEM_ID:
                adapter.changeCursor(data);
                progressBar.setVisibility(View.GONE);
                break;
            case LOADER_SHOP_ID:
                refreshShopsList(data);
                break;
            case LOADER_PLACE_ID:
                refreshPlacesList(data);
                break;
        }
        //adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //adapter.swapCursor(null);
        adapter.changeCursor(null);
        progressBar.setVisibility(View.VISIBLE);
    }
}
