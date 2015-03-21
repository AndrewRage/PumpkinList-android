package geekhub.activeshoplistapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;
import geekhub.activeshoplistapp.model.PlacesModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseEditFragment extends BaseFragment {
    private static final String TAG = PurchaseEditFragment.class.getSimpleName();
    private static final String ARG_LIST_ID = "PurchaseList_param";
    private static final int REQUEST_SHOP = 10101;
    private static final int REQUEST_PLACES = 10102;
    private PurchaseItemAdapter adapter;
    private ListView purchaseListView;
    private View header;
    private PurchaseListModel purchaseList;
    private EditText listNameEdit;
    private EditText goodsLabelEdit;
    private View addItemButton;
    private boolean isEdit;
    private Spinner shopsSpinner;
    private Spinner placeSpinner;
    private SettingsSpinnerAdapter shopSpinnerAdapter;
    private SettingsSpinnerAdapter placeSpinnerAdapter;
    private List<PlacesModel> shopsList;
    private List<PlacesModel> placesList;

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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            long id = getArguments().getLong(ARG_LIST_ID);
            purchaseList = ContentHelper.getPurchaseList(getActivity(), id);
            listNameEdit.setText(purchaseList.getListName());
            isEdit = true;
        } else {
            purchaseList = new PurchaseListModel();
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            purchaseList.setPurchasesItems(purchaseItems);
            isEdit = false;
        }

        adapter = new PurchaseItemAdapter(getActivity(), R.layout.item_purchase_edit, purchaseList.getPurchasesItems());;
        purchaseListView.addHeaderView(header);
        purchaseListView.setAdapter(adapter);
        purchaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateItem(position - 1);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        refreshShopsList();
        initShopSpinner();

        refreshPlacesList();
        initPlaceSpinner();
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
            for (PlacesModel shop : ShoppingHelper.getInstance().getPlacesList()) {
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
                if (isEdit) {
                    ShoppingHelper.getInstance().updatePurchaseList(purchaseList);
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
                if (isEdit) {
                    ShoppingHelper.getInstance().updatePurchaseList(purchaseList);
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
        if (isEdit) {
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
            PurchaseItemModel item = new PurchaseItemModel(
                    0,
                    0,
                    0,
                    false,
                    false,
                    0,
                    goodsLabelEdit.getText().toString(),
                    0,
                    "",
                    0
            );
            purchaseList.getPurchasesItems().add(0, item);

            if (isEdit) {
                ShoppingHelper.getInstance().addPurchaseItem(item, purchaseList.getDbId());
            }
            goodsLabelEdit.setText(null);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateList() {
        if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);
        }
        purchaseList.setListName(listNameEdit.getText().toString());
        ContentHelper.updatePurchaseList(getActivity(), purchaseList);
    }

    private void deleteList() {
        boolean isNeedDelete = false;
        if (isEdit) {
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
                    .setPositiveButton(R.string.purchase_edit_alert_delete_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (isEdit) {
                                ContentHelper.deletePurchaseList(getActivity(), purchaseList);
                            }
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton(R.string.purchase_edit_alert_delete_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addNewList() {
        if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);
        }
        purchaseList.setListName(listNameEdit.getText().toString());
        ContentHelper.insertPurchaseList(getActivity(), purchaseList);
    }

    private void updateItem(int position) {
        purchaseList.getPurchasesItems().get(position).setBought(
                !(purchaseList.getPurchasesItems().get(position).isBought())
        );
        if (isEdit) {
            ShoppingHelper.getInstance().updatePurchaseItem(
                    purchaseList.getPurchasesItems().get(position)
            );
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshShopsList() {
        if (shopsList == null) {
            shopsList = new ArrayList<>();
        }
        if (shopsList.size() > 0) {
            shopsList.clear();
        }
        for (PlacesModel placesModel : ShoppingHelper.getInstance().getPlacesList()) {
            if (placesModel.getCategory() == AppConstants.PLACES_SHOP) {
                shopsList.add(placesModel);
            }
        }
    }

    private void refreshPlacesList() {
        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        if (placesList.size() > 0) {
            placesList.clear();
        }
        for (PlacesModel placesModel : ShoppingHelper.getInstance().getPlacesList()) {
            if (placesModel.getCategory() == AppConstants.PLACES_USER) {
                placesList.add(placesModel);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SHOP) {
            refreshShopsList();
            shopSpinnerAdapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_PLACES) {
            refreshPlacesList();
            placeSpinnerAdapter.notifyDataSetChanged();
        }
    }
}
