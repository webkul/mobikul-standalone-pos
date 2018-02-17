package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentSignUpBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.handlers.SignUpSignInHandler;

public class SignUpFragment extends Fragment {
    public FragmentSignUpBinding binding;

    public SignUpFragment() {
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sign_up, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Administrator administrator = new Administrator();
        administrator.setEmail("admin");
        administrator.setFirstName("admin");
        administrator.setLastName("admin");
        administrator.setPassword("admin");
        binding.setData(administrator);
        binding.setHandler(new SignUpSignInHandler(getActivity(), administrator));
    }
}

