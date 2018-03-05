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
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentAddCustomerBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.handlers.AddCustomerFragmentHandler;

public class AddCustomerFragment extends Fragment {
    private static final String ARG_PARAM1 = "customer";
    private static final String ARG_PARAM2 = "edit";
    public FragmentAddCustomerBinding binding;
    private Customer customer;
    private boolean isEdit;
    private CustomerActivity customerActivity;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer = (Customer) getArguments().getSerializable(ARG_PARAM1);
            isEdit = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCustomerBinding.inflate(inflater, container, false);
        if (getActivity() != null)
            customerActivity = ((CustomerActivity) getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setData(customer);

        if (isEdit) {
            if (getContext() != null) {
                ((CustomerActivity) getContext())
                        .setTitle(getContext().getString(R.string.new_customer));
            }
            if (customerActivity != null) {
                customerActivity.binding.deleteCustomer.setVisibility(View.VISIBLE);
            }
        } else {
            if (getContext() != null) {
                ((CustomerActivity) getContext())
                        .setTitle(getContext().getString(R.string.new_customer));
            }
            customerActivity.binding.deleteCustomer.setVisibility(View.GONE);
        }
//        binding.setEdit(isEdit);
        customerActivity.binding.setEdit(isEdit);
        customerActivity.binding.setData(customer);
        customerActivity.binding.setHandler2(new AddCustomerFragmentHandler(getActivity()));
        customerActivity.binding.addCustomer.setVisibility(View.GONE);
        customerActivity.binding.saveCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customerActivity.binding.addCustomer.setVisibility(View.VISIBLE);
        customerActivity.binding.saveCustomer.setVisibility(View.GONE);
//        customerActivity.binding.deleteCustomer.setVisibility(View.GONE);
        getActivity().recreate();
        ((CustomerActivity) getContext())
                .setTitle(getContext().getString(R.string.title_activity_category));
    }
}
