package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.constants.OptionConstants;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ManageCategoryItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageCategoriesFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionValuesFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionsFragment;

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

    public void saveOptionToProduct(Product product, Options option, boolean isEdit) {
        List<Options> optionsList = new ArrayList<>(product.getOptions());
        // We must replace the new option if it is already found in product
        // If not then we will add the option to the product
        boolean add = true;
        for (int i = 0; i < optionsList.size(); i++) {
            Options options1 = optionsList.get(i);
            if (options1.getOptionId() == option.getOptionId()) {
                add = false;
                optionsList.set(i, option);
                break;
            }
        }
        if (add) {
            product.getOptions().add(option);
        }
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

    public void onOptionsSelect(final Options options, final Product product) {
        if (options.getType().equals(OptionConstants.RADIO) || options.getType().equals(OptionConstants.CHECKBOX)) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageOptionsFragment.class.getSimpleName());
            View mview = fragment.getLayoutInflater().inflate(R.layout.input_price_dialog, null);
            builder.setView(mview);
            final Dialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            Button cancel = mview.findViewById(R.id.price_cancel);
            Button addPrice = mview.findViewById(R.id.btn_add_price);
            final EditText price = mview.findViewById(R.id.enter_price);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            addPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float mPrice;
                    try {
                        mPrice = Float.parseFloat(price.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        price.setError(context.getString(R.string.enter_correct_value));
                        return;
                    }
                    List<Options> optionsList = new ArrayList<>(product.getOptions());
                    Options newOptions = options;
                    boolean add = true;
                    // We have to check for option existance in the product and add or replace accordingly
                    for (Options options1 : optionsList) {
                        if (options1.getOptionId() == options.getOptionId()) {
                            newOptions = options1;
                            add = false;
                            break;
                        }
                    }
                    newOptions.setSelected(true);
                    newOptions.getOptionValues().clear();
                    OptionValues optionValues = new OptionValues();
                    optionValues.setOptionValuePrice(Float.toString(mPrice));
                    optionValues.setSelected(true);
                    newOptions.getOptionValues().add(optionValues);
                    if (add) {
                        optionsList.add(newOptions);
                    }
                    product.setOptions(optionsList);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
