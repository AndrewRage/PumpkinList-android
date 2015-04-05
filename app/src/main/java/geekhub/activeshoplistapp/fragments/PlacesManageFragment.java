package geekhub.activeshoplistapp.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.MapActivity;
import geekhub.activeshoplistapp.adapters.PlaceAdapter;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.model.PlacesModel;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesManageFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PlacesManageFragment.class.getSimpleName();
    private static final String ARG_MENU_ID = "argMenuId";
    private static final String STATE_LIST = "PlaceListState";
    private ListView placeListView;
    private View floatPlus;
    private List<PlacesModel> placesList;
    private PlaceAdapter adapter;
    private int menuItemId = -1;
    private Parcelable placeListViewState;
    private boolean showFabPlus;

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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            placeListViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_manage, container, false);
        addToolbar(view);
        placeListView = (ListView) view.findViewById(R.id.shop_list);
        floatPlus = view.findViewById(R.id.plus_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            menuItemId = getArguments().getInt(ARG_MENU_ID);
        }

        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        getLoaderManager().initLoader(menuItemId, null, this);

        adapter = new PlaceAdapter(getActivity(), R.layout.item_shop, placesList);
        placeListView.setAdapter(adapter);
        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MapActivity.class)
                        .putExtra(AppConstants.EXTRA_MENU_ITEM, menuItemId)
                        .putExtra(
                                AppConstants.EXTRA_PLACE_ID,
                                adapter.getItem(position).getDbId()
                        );
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        floatPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class)
                        .putExtra(AppConstants.EXTRA_MENU_ITEM, menuItemId);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        placeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > visibleItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        showFabPlus(false);
                    } else {
                        showFabPlus(true);
                    }
                }
            }
        });
    }

    private void showFabPlus(boolean show) {
        if (show != showFabPlus) {
            showFabPlus = show;

            int time = getResources().getInteger(R.integer.fab_button_hide_time);
            int distance = getResources().getDimensionPixelSize(R.dimen.fab_button_hide_distance);
            int padding = getResources().getDimensionPixelSize(R.dimen.fab_button_right_margin);

            if (!show) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) floatPlus.getLayoutParams();
                    params.bottomMargin = (-distance);
                    floatPlus.setLayoutParams(params);
                } else {
                    ObjectAnimator
                            .ofFloat(floatPlus, "translationX", 0, distance)
                            .setDuration(time).start();
                }
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) floatPlus.getLayoutParams();
                    params.bottomMargin = padding;
                    floatPlus.setLayoutParams(params);
                } else {
                    ObjectAnimator
                            .ofFloat(floatPlus, "translationX", distance, 0)
                            .setDuration(time).start();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().restartLoader(menuItemId, null, this);
    }

    private void refreshShopsList(Cursor cursor) {
        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        if (placesList.size() > 0) {
            placesList.clear();
        }
        placesList.addAll(ContentHelper.getPlaceList(cursor));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (placeListViewState != null) {
            placeListView.onRestoreInstanceState(placeListViewState);
        }
        placeListViewState = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        placeListViewState = placeListView.onSaveInstanceState();
        outState.putParcelable(STATE_LIST, placeListViewState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(menuItemId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case AppConstants.MENU_SHOW_SHOPS: {
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
            case AppConstants.MENU_SHOW_PLACES: {
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
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        refreshShopsList(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
