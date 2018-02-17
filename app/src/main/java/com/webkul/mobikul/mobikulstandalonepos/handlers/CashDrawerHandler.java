package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CashDrawerActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.fragment.CashDrawerHistoryFragment;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */

public class CashDrawerHandler {
    private Context context;

    public CashDrawerHandler(Context context) {
        this.context = context;
    }

    public void onClickCashData(CashDrawerModel cashDrawerModelData) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(CashDrawerHistoryFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new CashDrawerHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cashDrawerModelData", cashDrawerModelData);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((CashDrawerActivity) context).binding.cashDrawerFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }
}
