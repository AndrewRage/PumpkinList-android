package geekhub.activeshoplistapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.BaseActivity;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.ActivityHelper;

/**
 * Created by rage on 06.02.15.
 *
 * Base Fragment
 */
public abstract class BaseFragment extends Fragment implements BaseActivity.InteractionListener {
    private static final String TAG = BaseFragment.class.getSimpleName();

    public void addToolbar(View view) {
        addToolbar(view, true);
    }

    public void addToolbar(View view, boolean displayHome) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(displayHome);
        activity.getSupportActionBar().setHomeButtonEnabled(displayHome);
        changeActionBarTitle();
        changeHomeUpToHamburger();
    }

    public void setActionBarTitle(int resId) {
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.getSupportActionBar().setTitle(resId);
    }

    public void setActionBarTitle(String string) {
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.getSupportActionBar().setTitle(string);
    }

    public void hideSoftKeyboard() {
        if(getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).setInteractionListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).setInteractionListener(null);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public void changeHomeUpToHamburger() {
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        ActivityHelper helper = ActivityHelper.getInstance();
        if (helper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_LIST) {
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    private void changeActionBarTitle() {
        ActivityHelper helper = ActivityHelper.getInstance();
        int menuId = helper.getGlobalId();
        switch (menuId) {
            case AppConstants.MENU_SHOW_PURCHASE_LIST:
                setActionBarTitle(R.string.menu_show_purchase_list);
                break;
            case AppConstants.MENU_SHOW_PURCHASE_ARCHIVE:
                setActionBarTitle(R.string.menu_show_purchase_archive);
                break;
            case AppConstants.MENU_SHOW_SHOPS:
                setActionBarTitle(R.string.menu_show_shops);
                break;
            case AppConstants.MENU_SHOW_PLACES:
                setActionBarTitle(R.string.menu_show_places);
                break;
            default:
                break;
        }
    }

}
