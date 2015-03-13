package geekhub.activeshoplistapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.activities.BaseActivity;

/**
 * Created by rage on 06.02.15.
 *
 * Base Fragment
 */
public abstract class BaseFragment extends Fragment implements BaseActivity.OnBackPressedListener {
    private static final String TAG = BaseFragment.class.getSimpleName();

    public void addToolbar(View view) {
        addToolbar(view, true);
    }

    public void addToolbar(View view, boolean displayHome) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(displayHome);
    }

    public void hideSoftKeyboard() {
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).setOnBackPressedListener(null);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
