package geekhub.activeshoplistapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.PurchaseItemAdapter;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseListEditFragment extends BaseFragment {
    private static final String TAG = "PurchaseListEditFragment";
    private static final String ARG_LIST_ID = "PurchaseList_param";

    private ListView purchaseListView;
    private View header;
    private View footer;
    private PurchaseListModel purchaseList;
    private EditText listNameEdit;
    private View addNewListButton;
    private View deleteListButton;
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
        deleteListButton = footer.findViewById(R.id.button_delete_list);
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
            isEdit = false;
        }

        PurchaseItemAdapter adapter = new PurchaseItemAdapter(getActivity(), R.layout.purchase_edit_item, purchaseList.getPurchasesItem());;
        purchaseListView.addHeaderView(header);
        purchaseListView.addFooterView(footer);
        purchaseListView.setAdapter(adapter);

        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (TextUtils.isEmpty(listNameEdit.getText())) {
                    listNameEdit.setText(R.string.purchase_edit_new_list_default);
                }
                purchaseList.setListName(listNameEdit.getText().toString());
                ShoppingHelper.getInstance().getPurchaseLists().add(purchaseList);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    ShoppingHelper.getInstance().getPurchaseLists().remove(purchaseList);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
