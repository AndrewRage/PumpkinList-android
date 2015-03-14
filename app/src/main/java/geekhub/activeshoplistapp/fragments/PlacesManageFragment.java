package geekhub.activeshoplistapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.MapActivity;
import geekhub.activeshoplistapp.adapters.PlaceAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ShoppingHelper;
import geekhub.activeshoplistapp.model.PlacesModel;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesManageFragment extends BaseFragment {
    private static final String TAG = PlacesManageFragment.class.getSimpleName();
    private static final String ARG_MENU_ID = "argMenuId";
    private ListView shopListView;
    private View plusButton;
    private List<PlacesModel> shopsList;
    private PlaceAdapter adapter;
    private int menuItemId = -1;

    public PlacesManageFragment() {
    }

    public static PlacesManageFragment newInstance(int menuItemId) {
        PlacesManageFragment fragment = new PlacesManageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_ID, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_manage, container, false);
        addToolbar(view);
        shopListView = (ListView) view.findViewById(R.id.shop_list);
        plusButton = view.findViewById(R.id.plus_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopsList = new ArrayList<>();

        if (getArguments() != null) {
            menuItemId = getArguments().getInt(ARG_MENU_ID);
        }

        getPlaces();

        adapter = new PlaceAdapter(getActivity(), R.layout.item_shop, shopsList);
        shopListView.setAdapter(adapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MapActivity.class)
                        .putExtra(AppConstants.EXTRA_SHOP_ID, position);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shopsList.clear();
        getPlaces();
        adapter.notifyDataSetChanged();
    }

    private void getPlaces() {
        if (menuItemId == AppConstants.MENU_SHOW_SHOPS) {
            shopsList.addAll(getShopsList());
        } else if (menuItemId == AppConstants.MENU_SHOW_PLACES) {
            shopsList.addAll(getUserPlacesList());
        }
    }

    private List<PlacesModel> getShopsList() {
        List<PlacesModel> list = new ArrayList<>();
        for (PlacesModel placesModel : ShoppingHelper.getInstance().gePlacesList()) {
            if (placesModel.getCategory() == AppConstants.PLACES_SHOP) {
                list.add(placesModel);
            }
        }
        return list;
    }

    private List<PlacesModel> getUserPlacesList() {
        List<PlacesModel> list = new ArrayList<>();
        for (PlacesModel placesModel : ShoppingHelper.getInstance().gePlacesList()) {
            if (placesModel.getCategory() == AppConstants.PLACES_USER) {
                list.add(placesModel);
            }
        }
        return list;
    }
}
