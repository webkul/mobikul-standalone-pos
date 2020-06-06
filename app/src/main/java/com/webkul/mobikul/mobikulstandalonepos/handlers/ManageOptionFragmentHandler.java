package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ManageCategoryItemBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageOptionValuesFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (options.getType().equalsIgnoreCase("radio") || options.getType().equalsIgnoreCase("checkbox")) {
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
        } else if (options.getType().equalsIgnoreCase("text") || options.getType().equalsIgnoreCase("textarea")) {
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
        } else {
            List<Options> optionsList = new ArrayList<>();
            optionsList.addAll(product.getOptions());
            for (Options options1 : optionsList) {
                if (options1.getOptionId() == options.getOptionId())
                    options.setSelected(true);
            }
            product.setOptions(optionsList);
            CustomOptionsDialogClass customDialogClass = new CustomOptionsDialogClass(context, options, product);
            customDialogClass.show();
        }
    }

    public class CustomOptionsDialogClass extends Dialog implements android.view.View.OnClickListener {
        public Dialog dialog;
        Button yes;
        TextView txt_dia, error_text;
        EditText opening_balance;
        private Context context;
        private Product product;
        private Options options;

        public CustomOptionsDialogClass(Context context, Options options, Product product) {
            super(context);
            this.context = context;
            this.product = product;
            this.options = options;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setCanceledOnTouchOutside(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);
            this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            error_text = findViewById(R.id.error_text);
            error_text.setText("Please enter price first!.");
            opening_balance = findViewById(R.id.opening_balance);
            opening_balance.setText("");
            opening_balance.setHint("Price");
            txt_dia = findViewById(R.id.txt_dia);
            txt_dia.setText("Set Price:");
            txt_dia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.START;
            txt_dia.setLayoutParams(params);
            yes = findViewById(R.id.btn_yes);
            yes.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:
                    String price = opening_balance.getText().toString();
                    if (!price.isEmpty()) {
                        List<Options> optionsList = new ArrayList<>(product.getOptions());
                        for (Options options1 : optionsList) {
                            if (options1.getOptionId() == options.getOptionId()) {
                                options.setSelected(true);
                                List<OptionValues> optionValues = new ArrayList<>();
                                OptionValues value = new OptionValues();
                                value.setOptionValuePrice(price);
                                optionValues.add(value);
                                options.setOptionValues(optionValues);
                            }
                        }
                        product.setOptions(optionsList);
                        error_text.setVisibility(View.GONE);
                        dismiss();
                    } else {
                        Helper.shake(context, findViewById(R.id.dialog_ll));
                        error_text.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    dismiss();
                    break;
            }
        }
    }
}
