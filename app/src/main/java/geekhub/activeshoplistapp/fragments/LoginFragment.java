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
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.internal.ImageDownloader;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.internal.oauth.AppSession;
import com.twitter.sdk.android.core.models.User;

import geekhub.activeshoplistapp.R;
import geekhub.activeshoplistapp.helpers.AppConstants;
import geekhub.activeshoplistapp.helpers.SharedPrefHelper;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class LoginFragment extends BaseFragment{
    private static final String TAG = "LoginFragment";
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private View loginButton;
    private View continueButton;
    private OnLoginFragmentListener onLoginFragmentListener;
    private TwitterLoginButton twitterLoginButton;

    /*Facebook*/
    //private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailEditText = (EditText) view.findViewById(R.id.edit_email);
        passwordEditText = (EditText) view.findViewById(R.id.edit_password);
        rememberCheckBox = (CheckBox) view.findViewById(R.id.checkbox_remember);
        loginButton = view.findViewById(R.id.button_login);
        continueButton = view.findViewById(R.id.button_continue);
        twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(getActivity(), null, callback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(getActivity());
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(callback));
            }
        }

        facebookLogout();

        view.findViewById(R.id.button_facebook_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });

        view.findViewById(R.id.button_google_plus_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginFragmentListener.onLoginFragmentClickListener(AppConstants.LOGIN_G_PLUS_BUTTON);
            }
        });

        view.findViewById(R.id.button_vkontakte_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginFragmentListener.onLoginFragmentClickListener(AppConstants.LOGIN_VKONTAKTE_BUTTON);
            }
        });

        view.findViewById(R.id.button_twitter_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginButton.performClick();
            }
        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "success");
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                TwitterSession guestAppSession = result.data;
                Log.d(TAG, "guestAppSession: " + guestAppSession.getUserName());

                //TODO twitter login
                
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d(TAG, "failure");
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void facebookLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(callback));
        } else {
            Session.openActiveSession(getActivity(), this, true, callback);
        }
    }

    private void facebookLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
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
            Toast.makeText(getActivity(), "LogIn", Toast.LENGTH_SHORT).show();
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            Toast.makeText(getActivity(), "LogOut", Toast.LENGTH_SHORT).show();
        }
    }
}
