package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CustomerActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCustomerFragment;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 23/1/18. @Webkul Software Private limited
 */

public class CustomerHandler {

    private Context context;

    public CustomerHandler(Context context) {
        this.context = context;
    }

    public void openAddCustomerFragment(Customer customer) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(AddCustomerFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddCustomerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("customer", customer);
//        bundle.putBoolean("edit", false);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((CustomerActivity) context).binding.customerFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    public void selectCustomer(Customer customer, boolean isChooseCustomer) {
        if (isChooseCustomer) {
            Intent i = ((AppCompatActivity) context).getIntent();
            i.putExtra("customer", customer);
            ((AppCompatActivity) context).setResult(Activity.RESULT_OK, i);
            ((AppCompatActivity) context).finish();
        } else {
//            edit customer stuff
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            Fragment fragment;
            fragment = fragmentManager.findFragmentByTag(AddCustomerFragment.class.getSimpleName());
            if (fragment == null)
                fragment = new AddCustomerFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("customer", customer);
            bundle.putBoolean("edit", true);
            fragment.setArguments(bundle);
            fragmentTransaction.add(((CustomerActivity) context).binding.customerFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }
}
