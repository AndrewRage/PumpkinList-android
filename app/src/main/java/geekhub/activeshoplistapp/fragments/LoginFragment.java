package geekhub.activeshoplistapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class LoginFragment extends BaseFragment{
    private static final String TAG = LoginFragment.class.getSimpleName();
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private View loginButton;
    private View continueButton;
    private OnLoginFragmentListener onLoginFragmentListener;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        addToolbar(view, false);
        emailEditText = (EditText) view.findViewById(R.id.edit_email);
        passwordEditText = (EditText) view.findViewById(R.id.edit_password);
        rememberCheckBox = (CheckBox) view.findViewById(R.id.checkbox_remember);
        loginButton = view.findViewById(R.id.button_login);
        continueButton = view.findViewById(R.id.button_continue);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefHelper = SharedPrefHelper.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEditText.getText())) {
                    if (TextUtils.equals(passwordEditText.getText(), "123")) {
                        if (rememberCheckBox.isChecked()) {
                            sharedPrefHelper.setUserName(emailEditText.getText().toString());
                        }
                        onLoginFragmentListener.onLoginFragmentClickListener(AppConstants.LOGIN_BUTTON);
                    }
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.setOffLine(true);
                onLoginFragmentListener.onLoginFragmentClickListener(AppConstants.LOGIN_BUTTON_CONTINUE);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onLoginFragmentListener = (OnLoginFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    public interface OnLoginFragmentListener {
        void onLoginFragmentClickListener(int button);
    }
}
