package geekhub.activeshoplistapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.PurchaseItemAdapter;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseListEditFragment extends BaseFragment {
    private static final String TAG = "PurchaseListEditFragment";
    private static final String ARG_LIST_ID = "PurchaseList_param";
    private PurchaseItemAdapter adapter;
    private ListView purchaseListView;
    private View header;
    private View footer;
    private PurchaseListModel purchaseList;
    private EditText listNameEdit;
    private View addNewListButton;
    private View updateListButton;
    private View deleteListButton;
    private EditText goodsLabelEdit;
    private View addItemButton;
    private boolean isEdit;

    public static PurchaseListEditFragment newInstance(int purchaseListId) {
        PurchaseListEditFragment fragment = new PurchaseListEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, purchaseListId);
        fragment.setArguments(args);
        return fragment;
    }

    public PurchaseListEditFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list_edit, container, false);
        purchaseListView = (ListView) view.findViewById(R.id.purchase_item_list);
        header = inflater.inflate(R.layout.purchase_edit_header, purchaseListView, false);
        footer = inflater.inflate(R.layout.purchase_edit_footer, purchaseListView, false);
        listNameEdit = (EditText) footer.findViewById(R.id.edit_list_name);
        addNewListButton = footer.findViewById(R.id.button_new_list);
        updateListButton = footer.findViewById(R.id.button_update_list);
        deleteListButton = footer.findViewById(R.id.button_delete_list);
        goodsLabelEdit = (EditText) header.findViewById(R.id.edit_goods_label);
        addItemButton = header.findViewById(R.id.button_goods_add);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int id = getArguments().getInt(ARG_LIST_ID);
            purchaseList = ShoppingHelper.getInstance().getPurchaseLists().get(id);
            listNameEdit.setText(purchaseList.getListName());
            addNewListButton.setVisibility(View.GONE);
            isEdit = true;
        } else {
            purchaseList = new PurchaseListModel();
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            purchaseList.setPurchasesItems(purchaseItems);
            updateListButton.setVisibility(View.GONE);
            isEdit = false;
        }

        adapter = new PurchaseItemAdapter(getActivity(), R.layout.purchase_edit_item, purchaseList.getPurchasesItems());;
        purchaseListView.addHeaderView(header);
        purchaseListView.addFooterView(footer);
        purchaseListView.setAdapter(adapter);
        purchaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    ShoppingHelper.getInstance().updatePurchaseItem(
                            purchaseList.getPurchasesItems().get(position - 1)
                    );
                } else {
                    purchaseList.getPurchasesItems().get(position - 1).setBought(
                            !(purchaseList.getPurchasesItems().get(position - 1).isBought())
                    );
                }
                adapter.notifyDataSetChanged();
            }
        });

        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (TextUtils.isEmpty(listNameEdit.getText())) {
                    listNameEdit.setText(R.string.purchase_edit_new_list_default);
                }
                purchaseList.setListName(listNameEdit.getText().toString());
                purchaseList.setTimeCreate(System.currentTimeMillis());
                ShoppingHelper.getInstance().addPurchaseList(purchaseList);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteList();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        updateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                updateList();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
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
                    System.currentTimeMillis());
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
        purchaseList.setTimeStamp(0);
        ShoppingHelper.getInstance().updatePurchaseList(purchaseList);
    }

    private void deleteList() {
        if (isEdit) {
            ShoppingHelper.getInstance().deletePurchaseList(purchaseList);
        }
    }
}
