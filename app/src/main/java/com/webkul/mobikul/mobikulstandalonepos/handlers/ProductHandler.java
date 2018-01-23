package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;

/**
 * Created by aman.gupta on 9/1/18.
 */

public class ProductHandler {

    private final Context context;

    public ProductHandler(Context context) {
        this.context = context;
    }

    public void addProduct(View v, Product product) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        bundle.putBoolean("edit", false);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }


    public void onClickProduct(Product product) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddProductFragment();
        if (!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            bundle.putBoolean("edit", true);
            fragment.setArguments(bundle);
            Log.d("name", fragment.getClass().getSimpleName() + "");
            fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }
}
