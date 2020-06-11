package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ManageCategoryItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageCategoriesFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionValuesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

/**
 * Created by aman.gupta on 17/2/18. @Webkul Software Private limited
 */

public class ManageOptionFragmentHandler {

    private Context context;
    private ManageCategoryItemBinding manageCategoryItemBinding;
    public static HashMap<String, Options> optionHashmap;

    public ManageOptionFragmentHandler(Context context) {
        this.context = context;
        initialize();
    }

    void initialize() {
        if (optionHashmap == null)
            optionHashmap = new HashMap<>();
    }

    public void saveOptionToProduct(Product product, boolean isEdit) {
        List<Options> optionsList = new ArrayList<>();
        optionsList.addAll(product.getOptions());
        for (Options options : optionsList) {
            for (OptionValues optionValues : options.getOptionValues()) {
                if (optionValues.isSelected()) {
                    options.setSelected(optionValues.isSelected());
                    break;
                }
                options.setSelected(optionValues.isSelected());
            }
        }

        product.setOptions(optionsList);
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageOptionValuesFragment.class.getSimpleName());
        FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
        ft.detach(fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
        ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
    }

    public void onOptionsSelect(Options options, Product product) {
        if (!options.getType().equalsIgnoreCase("text") && !options.getType().equalsIgnoreCase("textarea")) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            Fragment fragment;
            fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageOptionValuesFragment.class.getSimpleName());
            if (fragment == null)
                fragment = new ManageOptionValuesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("options", options);
            fragment.setArguments(bundle);
            fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        } else {
            List<Options> optionsList = new ArrayList<>();
            optionsList.addAll(product.getOptions());
            for (Options options1 : optionsList) {
                if (options1.getOptionId() == options.getOptionId())
                    options.setSelected(true);
            }
            product.setOptions(optionsList);
            Log.d("Options", new Gson().toJson(product.getOptions()) + "");

            Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageOptionValuesFragment.class.getSimpleName());
            FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
            ft.detach(fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
        }
    }
}
