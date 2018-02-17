package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CashDrawerHistoryAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentCashDrawerHistoryBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;


public class CashDrawerHistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "cashDrawerModelData";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private CashDrawerModel cashDrawerModelData;
    private String mParam2;
    FragmentCashDrawerHistoryBinding binding;

    public CashDrawerHistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cashDrawerModelData = (CashDrawerModel) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_drawer_history, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setData(cashDrawerModelData);
        CashDrawerHistoryAdapter cashDrawerHistoryAdapter = new CashDrawerHistoryAdapter(getActivity(), cashDrawerModelData.getCashDrawerItems());
        binding.cashDrawerHistoryRv.setAdapter(cashDrawerHistoryAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
