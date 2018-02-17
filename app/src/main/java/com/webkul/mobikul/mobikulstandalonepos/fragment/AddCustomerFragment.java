package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CustomerActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddCustomerBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.handlers.AddCustomerFragmentHandler;

public class AddCustomerFragment extends Fragment {
    private static final String ARG_PARAM1 = "customer";
    private static final String ARG_PARAM2 = "param2";
    public FragmentAddCustomerBinding binding;
    private Customer customer;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer = (Customer) getArguments().getSerializable(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCustomerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((CustomerActivity) getContext())
                .setTitle(getContext().getString(R.string.new_customer));
        binding.setData(customer);
        ((CustomerActivity) getActivity()).binding.setData(customer);
        ((CustomerActivity) getActivity()).binding.setHandler2(new AddCustomerFragmentHandler(getActivity()));
        ((CustomerActivity) getActivity()).binding.addCustomer.setVisibility(View.GONE);
        ((CustomerActivity) getActivity()).binding.saveCustomer.setVisibility(View.VISIBLE);
//        ((CustomerActivity) getActivity()).binding.deleteCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((CustomerActivity) getActivity()).binding.addCustomer.setVisibility(View.VISIBLE);
        ((CustomerActivity) getActivity()).binding.saveCustomer.setVisibility(View.GONE);
//        ((CustomerActivity) getActivity()).binding.deleteCustomer.setVisibility(View.GONE);
        getActivity().recreate();
        ((CustomerActivity) getContext())
                .setTitle(getContext().getString(R.string.title_activity_category));
    }
}
