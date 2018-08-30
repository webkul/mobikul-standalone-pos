package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.adapter.HoldCartAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.FragmentHoldBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.ArrayList;
import java.util.List;

public class HoldFragment extends Fragment {
    FragmentHoldBinding binding;
    List<HoldCart> holdCartList;

    public static HoldFragment newInstance(/*String param1, String param2*/) {
        HoldFragment fragment = new HoldFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hold, container, false);
        binding.setVisibility(true);
        holdCartList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHoldCartData();
    }

    void setHoldCartData() {
        DataBaseController.getInstanse().getHoldCart(getActivity(), new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                if (((List<HoldCart>) responseData).size() > 0) {
                    if (holdCartList.size() > 0)
                        holdCartList.clear();
                    holdCartList.addAll((List<HoldCart>) responseData);
                    HoldCartAdapter holdCartAdapter = new HoldCartAdapter(getActivity(), holdCartList);
                    binding.holdCartRv.setAdapter(holdCartAdapter);
                    binding.setVisibility(true);
                } else {
                    binding.setVisibility(false);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getContext() != null) {
            ((BaseActivity) getContext())
                    .setActionbarTitle(getContext().getString(R.string.hold_fragment_title));
            setHoldCartData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final MenuItem barcodeItem = menu.findItem(R.id.menu_item_scan_barcode);
        searchItem.setVisible(false);
        barcodeItem.setVisible(false);
    }
}