package geekhub.activeshoplistapp.fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.PlacesActivity;
import geekhub.activeshoplistapp.adapters.PurchaseItemAdapter;
import geekhub.activeshoplistapp.adapters.SettingsSpinnerAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;
import geekhub.activeshoplistapp.utils.ViewUtils;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseEditFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PurchaseEditFragment.class.getSimpleName();
    private static final String ARG_LIST_ID = "PurchaseList_param";
    private static final String STATE_LIST = "PurchaseListState";
    private static final int REQUEST_SHOP = 10101;
    private static final int REQUEST_PLACES = 10102;
    private static final int LOADER_ITEM_ID = 0;
    private static final int LOADER_PLACE_ID = 1;
    private static final int LOADER_SHOP_ID = 2;
    private static final int LOADER_LIST = 3;
    private PurchaseItemAdapter adapter;
    private ListView purchaseListView;
    private View header, progressBar, addItemButton, toolbarBottom, hidePanel, moreButton;
    private EditText listNameEdit;
    private EditText goodsLabelEdit;
    private Spinner shopsSpinner, placeSpinner;
    private SettingsSpinnerAdapter shopSpinnerAdapter, placeSpinnerAdapter;
    private List<PlacesModel> shopsList, placesList;
    private PurchaseListModel purchaseList;
    private Parcelable purchaseListViewState;
    private boolean isShowMore = false;

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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            purchaseListViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
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

        progressBar = view.findViewById(R.id.progress_bar);

        toolbarBottom = view.findViewById(R.id.toolbar_bottom);
        moreButton = view.findViewById(R.id.more_button);
        hidePanel = view.findViewById(R.id.hide_panel);
        shopsSpinner = (Spinner) view.findViewById(R.id.shops_spinner);
        placeSpinner = (Spinner) view.findViewById(R.id.place_spinner);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            long id = getArguments().getLong(ARG_LIST_ID);
            Bundle args = new Bundle();
            args.putLong(ARG_LIST_ID, id);
            getLoaderManager().initLoader(LOADER_LIST, args, this);
        } else {
            purchaseList = new PurchaseListModel();
            purchaseList.setTimeCreate(System.currentTimeMillis());
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            purchaseList.setPurchasesItems(purchaseItems);

            initScreen();
        }

        final int heightHidePanel = getActivity().getResources().getDimensionPixelSize(R.dimen.purchase_item_hide_panel_height);
        //ViewUtils.setY(toolbarBottom, heightHidePanel);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMore = !isShowMore;
                if (isShowMore) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbarBottom.getLayoutParams();
                        params.bottomMargin = 0;
                        toolbarBottom.setLayoutParams(params);
                    } else {
                        ObjectAnimator.ofFloat(toolbarBottom, "translationY", 0, -heightHidePanel).setDuration(1000).start();
                    }
                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbarBottom.getLayoutParams();
                        params.bottomMargin = (-heightHidePanel);
                        toolbarBottom.setLayoutParams(params);
                    } else {
                        ObjectAnimator.ofFloat(toolbarBottom, "translationY", -heightHidePanel, 0).setDuration(1000).start();
                    }
                }
            }
        });
    }

    private void initScreen() {
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

        goodsLabelEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem();
                    handled = true;
                }
                return handled;
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
                R.string.shop_edit_spinner_default_entry
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
                if (!purchaseList.isUserShop()) {
                    if (shop.getServerId() == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                } else {
                    if (shop.getDbId() == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                }
            }
        }
        shopsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean needUpdate = false;
                if (position != 0) {
                    if (shopSpinnerAdapter.getItem(position).getServerId() == 0
                            && (purchaseList.getShopId() != shopSpinnerAdapter.getItem(position).getDbId()
                            || !purchaseList.isUserShop())) {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getDbId());
                        purchaseList.setIsUserShop(true);
                        needUpdate = true;
                    } else if (shopSpinnerAdapter.getItem(position).getServerId() != 0
                            && (purchaseList.getShopId() != shopSpinnerAdapter.getItem(position).getServerId()
                            || purchaseList.isUserShop())) {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getServerId());
                        purchaseList.setIsUserShop(false);
                        needUpdate = true;
                    }
                } else if (purchaseList.getShopId() != 0) {
                    purchaseList.setShopId(0);
                    needUpdate = true;
                }
                if (needUpdate && purchaseList.getDbId() != 0) {
                    purchaseList.setMaxDistance(0);
                    purchaseList.setIsAlarm(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                        }
                    }).start();
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
                R.string.place_edit_spinner_default_entry
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
                if (!purchaseList.isUserPlace()) {
                    if (place.getServerId() == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                } else {
                    if (place.getDbId() == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                }
            }
        }
        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean needUpdate = false;
                if (position != 0) {
                    if (placeSpinnerAdapter.getItem(position).getServerId() == 0
                            && (purchaseList.getPlaceId() != placeSpinnerAdapter.getItem(position).getDbId()
                            || !purchaseList.isUserPlace())) {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getDbId());
                        purchaseList.setIsUserPlace(true);
                        needUpdate = true;
                    } else if (placeSpinnerAdapter.getItem(position).getServerId() != 0
                            && (purchaseList.getPlaceId() != placeSpinnerAdapter.getItem(position).getServerId()
                            || purchaseList.isUserPlace())) {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getServerId());
                        purchaseList.setIsUserPlace(false);
                        needUpdate = true;
                    }
                } else if (purchaseList.getPlaceId() != 0) {
                    purchaseList.setPlaceId(0);
                    purchaseList.setIsUserPlace(false);
                    needUpdate = true;
                }
                if (needUpdate && purchaseList.getDbId() != 0) {
                    purchaseList.setMaxDistance(0);
                    purchaseList.setIsAlarm(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                        }
                    }).start();
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        purchaseListViewState = purchaseListView.onSaveInstanceState();
        outState.putParcelable(STATE_LIST, purchaseListViewState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(LOADER_LIST);
        getLoaderManager().destroyLoader(LOADER_PLACE_ID);
        getLoaderManager().destroyLoader(LOADER_SHOP_ID);
        getLoaderManager().destroyLoader(LOADER_ITEM_ID);
    }

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
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
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
        goodsLabelEdit.requestFocus();
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
                            final Context context = getActivity().getApplicationContext();
                            if (purchaseList.getDbId() != 0) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ContentHelper.deletePurchaseList(context, purchaseList);
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

    private void refreshPurchaseList(Cursor cursor) {
        purchaseList = ContentHelper.getPurchaseList(cursor);
        listNameEdit.setText(purchaseList.getListName());
        initScreen();
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
            case LOADER_LIST: {
                long dbId = args.getLong(ARG_LIST_ID);
                Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + dbId);
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
                };
                return new CursorLoader(
                        getActivity(),
                        uri,
                        projection,
                        null,
                        null,
                        null
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
                if (purchaseListViewState != null) {
                    purchaseListView.onRestoreInstanceState(purchaseListViewState);
                }
                purchaseListViewState = null;
                progressBar.setVisibility(View.GONE);
                break;
            case LOADER_SHOP_ID:
                refreshShopsList(data);
                break;
            case LOADER_PLACE_ID:
                refreshPlacesList(data);
                break;
            case LOADER_LIST:
                refreshPurchaseList(data);
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
