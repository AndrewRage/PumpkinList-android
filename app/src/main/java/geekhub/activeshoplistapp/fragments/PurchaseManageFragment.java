package geekhub.activeshoplistapp.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.adapters.PurchaseListAdapter;
import geekhub.activeshoplistapp.helpers.ActivityHelper;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ContentHelper;
import geekhub.activeshoplistapp.helpers.SqlDbHelper;
import geekhub.activeshoplistapp.helpers.ShoppingContentProvider;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseManageFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PurchaseManageFragment.class.getSimpleName();
    //private static final String ARG_MENU_ID = "argMenuId";
    private static final String STATE_LIST = "PurchaseListState";

    private GridView purchaseView;
    private View progressBar, floatPlus;
    private OnPurchaseListMainFragmentListener purchaseListMainFragmentListener;
    private PurchaseListAdapter adapter;
    private Parcelable purchaseViewState;
    private boolean showFabPlus;

    /*public static PurchaseManageFragment newInstance(int menuItemId) {
        PurchaseManageFragment fragment = new PurchaseManageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_ID, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            purchaseViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_manage, container, false);
        addToolbar(view);
        purchaseView = (GridView) view.findViewById(R.id.purchase_view);
        progressBar = view.findViewById(R.id.progress_bar);
        floatPlus = view.findViewById(R.id.plus_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            adapter = new PurchaseListAdapter(getActivity(), null, R.layout.item_purchase_view);
        } else {
            adapter = new PurchaseListAdapter(getActivity(), null, 0, R.layout.item_purchase_view);
        }
        purchaseView.setAdapter(adapter);
        purchaseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                purchaseListMainFragmentListener.onPurchaseListMainFragmentClickListener(
                        ContentHelper.getDbId((Cursor) purchaseView.getItemAtPosition(position))
                );
            }
        });

        getLoaderManager().initLoader(0, null, this);

        //Show edit fragment
        floatPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseListMainFragmentListener.onPurchaseListMainFragmentClickListener();
            }
        });

        purchaseView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            purchaseListMainFragmentListener = (OnPurchaseListMainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (purchaseView != null) {
            purchaseViewState = purchaseView.onSaveInstanceState();
            outState.putParcelable(STATE_LIST, purchaseViewState);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                SqlDbHelper.COLUMN_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
        };
        String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
        String[] selectionArgs;
        if (ActivityHelper.getInstance().getPurchaseMenuId() == AppConstants.MENU_SHOW_PURCHASE_LIST) {
            selectionArgs = new String[]{"0"};
        } else {
            selectionArgs = new String[]{"1"};
        }
        String selection = SqlDbHelper.PURCHASE_LIST_COLUMN_DONE + "=?";
        return new CursorLoader(
                getActivity(),
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                orderBy
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
        if (purchaseViewState != null) {
            purchaseView.onRestoreInstanceState(purchaseViewState);
        }
        purchaseViewState = null;

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    public interface OnPurchaseListMainFragmentListener {
        void onPurchaseListMainFragmentClickListener();
        void onPurchaseListMainFragmentClickListener(long id);
    }
}
