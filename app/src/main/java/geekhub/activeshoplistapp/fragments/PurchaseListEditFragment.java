package geekhub.activeshoplistapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.PurchaseItemAdapter;
import geekhub.activeshoplistapp.adapters.PurchaseListAdapter;
import geekhub.activeshoplistapp.model.PurchaseItemModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseListEditFragment extends BaseFragment {
    private static final String TAG = "PurchaseListEditFragment";
    private ListView purchaseListView;
    private View header;
    private View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list_edit, container, false);
        purchaseListView = (ListView) view.findViewById(R.id.purchase_item_list);
        header = inflater.inflate(R.layout.purchase_edit_header, purchaseListView, false);
        footer = inflater.inflate(R.layout.purchase_edit_footer, purchaseListView, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Fake data
        List<PurchaseItemModel> purchaseItemLists = new ArrayList<>();
        purchaseItemLists.add(new PurchaseItemModel(false,false,false,1,1,"Desc_str",123));

        PurchaseItemAdapter adapter = new PurchaseItemAdapter(getActivity(), R.layout.purchase_edit_item, purchaseItemLists);;
        purchaseListView.addHeaderView(header);
        purchaseListView.addFooterView(footer);
        purchaseListView.setAdapter(adapter);
    }
}
