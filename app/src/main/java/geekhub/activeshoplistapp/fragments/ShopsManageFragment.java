package geekhub.activeshoplistapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.ShopMapActivity;
import geekhub.activeshoplistapp.adapters.ShopAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.ShopsModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class ShopsManageFragment extends BaseFragment {
    private ListView shopListView;
    private View plusButton;
    private List<ShopsModel> shopsList;
    private ShopAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_manage, container, false);
        addToolbar(view);
        shopListView = (ListView) view.findViewById(R.id.shop_list);
        plusButton = view.findViewById(R.id.plus_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopsList = ShoppingHelper.getInstance().getShopsList();

        adapter = new ShopAdapter(getActivity(), R.layout.shop_item, shopsList);
        shopListView.setAdapter(adapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShopMapActivity.class)
                        .putExtra(AppConstants.EXTRA_SHOP_ID, position);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopMapActivity.class);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }
}
