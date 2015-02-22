package geekhub.activeshoplistapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.PurchaseListAdapter;
import geekhub.activeshoplistapp.model.PurchaseItemModel;
import geekhub.activeshoplistapp.model.PurchaseListModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseListMainFragment extends BaseFragment {
    private static final String TAG = "PurchaseListMainFragment";

    private GridView purchaseView;
    private List<PurchaseListModel> purchaseLists;
    private OnPurchaseListMainFragmentListener purchaseListMainFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list_main, container, false);
        purchaseView = (GridView) view.findViewById(R.id.purchase_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Fake data
        purchaseLists = new ArrayList<>();
        List<PurchaseItemModel> purchasesItem = new ArrayList<>();
        purchaseLists.add(new PurchaseListModel(0,"List1",1,1,0,0,0,purchasesItem));
        purchaseLists.add(new PurchaseListModel(0,"List2",1,1,0,0,0,purchasesItem));
        purchaseLists.add(new PurchaseListModel(0,"List3",1,1,0,0,0,purchasesItem));
        purchaseLists.add(new PurchaseListModel(0,"List4",1,1,0,0,0,purchasesItem));
        purchaseLists.add(new PurchaseListModel(0,"List5",1,1,0,0,0,purchasesItem));

        PurchaseListAdapter adapter = new PurchaseListAdapter(getActivity(), R.layout.purchase_view_item, purchaseLists);
        purchaseView.setAdapter(adapter);

        //Show edit fragment
        view.findViewById(R.id.plus_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseListMainFragmentListener.onPurchaseListMainFragmentClickListener();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            purchaseListMainFragmentListener = (OnPurchaseListMainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    public interface OnPurchaseListMainFragmentListener {
        public void onPurchaseListMainFragmentClickListener();
    }
}
