package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.TaxActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddTaxFragment;

/**
 * Created by aman.gupta on 26/2/18. @Webkul Software Private limited
 */

public class TaxActivityHandler {

    private Context context;

    public TaxActivityHandler(Context context) {
        this.context = context;
    }

    public void addTax() {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddTaxFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddTaxFragment();
        if (!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tax", new Tax());
            bundle.putBoolean("edit", false);
            fragment.setArguments(bundle);
            fragmentTransaction.add(((TaxActivity) context).binding.taxFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    public void editTaxRate(Tax tax) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddTaxFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddTaxFragment();
        if (!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tax", tax);
            bundle.putBoolean("edit", true);
            fragment.setArguments(bundle);
            fragmentTransaction.add(((TaxActivity) context).binding.taxFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }
}
