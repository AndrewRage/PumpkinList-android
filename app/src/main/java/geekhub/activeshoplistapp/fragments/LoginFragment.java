package geekhub.activeshoplistapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import geekhub.activeshoplistapp.R;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class LoginFragment extends BaseFragment{
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private View loginButton;
    private View continueButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
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


    }
}
