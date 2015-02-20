package geekhub.activeshoplistapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class LoginFragment extends BaseFragment{
    private static final String TAG = "Login Fragment";
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private View loginButton;
    private View continueButton;
    private OnLoginFragmentListener onLoginFragmentListener;

    /*Facebook*/
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailEditText = (EditText) view.findViewById(R.id.edit_email);
        passwordEditText = (EditText) view.findViewById(R.id.edit_password);
        rememberCheckBox = (CheckBox) view.findViewById(R.id.checkbox_remember);
        loginButton = view.findViewById(R.id.button_login);
        continueButton = view.findViewById(R.id.button_continue);

        LoginButton facebookAuthButton = (LoginButton) view.findViewById(R.id.button_facebook_auth);
        facebookAuthButton.setFragment(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEditText.getText())) {
                    if (TextUtils.equals(passwordEditText.getText(), "123")) {
                        if (rememberCheckBox.isChecked()) {
                            SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
                            sharedPrefHelper.setUserName(emailEditText.getText().toString());
                        }
                        onLoginFragmentListener.onLoginFragmentClickListener(AppConstants.LOGIN_BUTTON);
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
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
        public void onLoginFragmentClickListener(int button);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }
}
