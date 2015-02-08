package geekhub.activeshoplistapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geekhub.activeshoplistapp.R;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class UserFriendAddFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_friend_add, container, false);
        return view;
    }

}
