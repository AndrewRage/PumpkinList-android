package geekhub.activeshoplistapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.LoginActivity;
import geekhub.activeshoplistapp.activities.ShopActivity;
import geekhub.activeshoplistapp.adapters.PurchaseListAdapter;
import geekhub.activeshoplistapp.helpers.DataBaseHelper;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
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
        addToolbar(view);
        purchaseView = (GridView) view.findViewById(R.id.purchase_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        purchaseLists = ShoppingHelper.getInstance().getPurchaseLists();

        final PurchaseListAdapter adapter = new PurchaseListAdapter(getActivity(), R.layout.purchase_view_item, purchaseLists);
        purchaseView.setAdapter(adapter);
        purchaseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                purchaseListMainFragmentListener.onPurchaseListMainFragmentClickListener(position);
            }
        });

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
        public void onPurchaseListMainFragmentClickListener(int id);
    }
}
