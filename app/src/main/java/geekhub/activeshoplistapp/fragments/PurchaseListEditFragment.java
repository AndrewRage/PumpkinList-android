package geekhub.activeshoplistapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import geekhub.activeshoplistapp.R;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseListEditFragment extends BaseFragment {
    private static final String TAG = "PurchaseListEditFragment";
    private ListView purchaseListView;
    private ViewGroup header;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list_edit, container, false);
        purchaseListView = (ListView) view.findViewById(R.id.purchase_item_list);
        header = (ViewGroup) inflater.inflate(R.layout.purchase_edit_header, purchaseListView, false);
        purchaseListView.addHeaderView(header);
        return view;
    }

}
