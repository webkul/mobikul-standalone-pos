package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.customviews.CustomDialogClass;
import com.webkul.mobikul.mobikulstandalonepos.databinding.CustomOptionsBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HomeFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 17/1/18. @Webkul Software Private limited
 */

public class HomeFragmentHandler {

    private Context context;
    double subTotal;
    int counter;
    String currencySymbol;
    private double grandTotal;
    DecimalFormat df;
    HashMap<String, OptionValues> optionValuesHashMap;

    public HomeFragmentHandler(Context context) {
        this.context = context;
        currencySymbol = context.getResources().getString(R.string.currency_symbol);
        df = new DecimalFormat("####0.00");
        optionValuesHashMap = new HashMap<>();
    }

    public void onClickProduct(Product product) {
        if (!AppSharedPref.isReturnCart(context)) {
            Log.d(TAG, "onClickProduct: " + new Gson().toJson(product.getOptions()));
            CartModel cartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(context));
            if (cartData == null) {
                cartData = new CartModel();
            }
            subTotal = Double.parseDouble(cartData.getTotals().getSubTotal());
            counter = Integer.parseInt(cartData.getTotals().getQty());
            Log.d(TAG, "onClickProduct: " + product.getCartQty());
            if (product.isStock() && (Integer.parseInt(product.getQuantity()) > Integer.parseInt(product.getCartQty()))) {
                if (isOptionsShow(product)) {
                    CustomOptionsDialogClass customOptionsDialogClass = new CustomOptionsDialogClass(context, product, cartData);
                    customOptionsDialogClass.show();
                } else
                    addToCart(product, cartData);
            } else {
                ToastHelper.showToast(context, "The quantity for " + product.getProductName() + " is not available", Toast.LENGTH_LONG);
            }
        } else {
            ToastHelper.showToast(context, "First complete Return Order!", 1000);
        }
    }

    void addToCart(Product product, CartModel cartData) {
        double price;
        double basePrice;
        if (product.getSpecialPrice().isEmpty()) {
            price = Helper.currencyConverter(Double.parseDouble(product.getPrice()), context);
            subTotal = subTotal + Double.parseDouble(product.getPrice());
            basePrice = Double.parseDouble(product.getPrice());

        } else {
            price = Helper.currencyConverter(Double.parseDouble(product.getSpecialPrice()), context);
            subTotal = subTotal + Double.parseDouble(product.getSpecialPrice());
            product.setFormattedSpecialPrice(Helper.currencyFormater(price, context) + "");
            basePrice = Double.parseDouble(product.getSpecialPrice());
        }

        for (int i = 0; i < product.getOptions().size(); i++) {
            if (!product.getOptions().get(i).getType().equalsIgnoreCase("text") && !product.getOptions().get(i).getType().equalsIgnoreCase("textarea"))
                for (OptionValues optionValues : product.getOptions().get(i).getOptionValues()) {
                    if (optionValues.isAddToCart()) {
                        if (!optionValues.getOptionValuePrice().isEmpty()) {
                            subTotal = subTotal + Double.parseDouble(optionValues.getOptionValuePrice());
                            price = price + Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), context);
                            basePrice = basePrice + Double.parseDouble(optionValues.getOptionValuePrice());
                        }
                    }
                }
        }

        product.setFormattedPrice(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getPrice()), context), context) + "");
        counter++;
        boolean isProductAlreadyInCart = false;
        int position = -1;
        if (cartData.getProducts().size() == 0) {
            product.setCartProductSubtotal(basePrice + "");
        }
        if (product.getCartQty().equalsIgnoreCase("0"))
            product.setCartQty("1");
        for (Product product1 : cartData.getProducts()) {
            position++;
            if (product.getPId() == product1.getPId() && new Gson().toJson(product.getOptions()).equalsIgnoreCase(new Gson().toJson(product1.getOptions()))) {
                int cartQty = Integer.parseInt(product1.getCartQty());
                cartQty++;
                product.setCartQty(cartQty + "");
                product.setCartProductSubtotal(Double.parseDouble(product1.getCartProductSubtotal()) + basePrice + "");
                isProductAlreadyInCart = true;
                break;
            } else {
                product.setCartProductSubtotal(basePrice + "");
            }
        }
        product.setFormattedCartProductSubtotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getCartProductSubtotal()), context), context));

        if (!isProductAlreadyInCart)
            cartData.getProducts().add(product);
        else {
            cartData.getProducts().remove(position);
            cartData.getProducts().add(position, product);
        }

        double taxRate = 0;
        if (product.isTaxableGoodsApplied() && product.getProductTax() != null && !product.getProductTax().toString().isEmpty()) {
            if (product.getProductTax().getType().contains("%")) {
                taxRate = (price / 100.0f) * Double.parseDouble(product.getProductTax().getTaxRate());
            } else {
                taxRate = Double.parseDouble(product.getProductTax().getTaxRate());
            }
        }

        cartData.getTotals().setTax(df.format(Double.parseDouble(cartData.getTotals().getTax()) + taxRate) + "");

        grandTotal = subTotal + Double.parseDouble(cartData.getTotals().getTax());
        cartData.getTotals().setSubTotal(subTotal + "");
        cartData.getTotals().setQty(counter + "");
        cartData.getTotals().setGrandTotal(df.format(grandTotal) + "");
        cartData.getTotals().setRoundTotal(Math.ceil(grandTotal) + "");
        // set formated values
        cartData.getTotals().setFormatedSubTotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(df.format(subTotal)), context), context));
        cartData.getTotals().setFormatedTax(Helper.currencyFormater(Double.parseDouble(cartData.getTotals().getTax()), context));
        cartData.getTotals().setFormatedGrandTotal(Helper.currencyFormater(Double.parseDouble(df.format(grandTotal)), context));
        cartData.getTotals().setFormatedRoundTotal(Helper.currencyFormater((Math.ceil(grandTotal)), context));
        AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        ((HomeFragment) fragment).binding.setCartData(cartData);
        ToastHelper.showToast(context, "" + product.getProductName() + " is added to cart.", Toast.LENGTH_SHORT);
    }

    public void goToCart(CartModel cartData) {
        Intent i = new Intent(context, CartActivity.class);
        i.putExtra("cartData", Helper.fromCartModelToString(cartData));
        context.startActivity(i);
    }

    public class CustomOptionsDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Dialog d;
        public Button yes, no;
        private Context context;
        private Product product;
        private CartModel cartData;

        public CustomOptionsDialogClass(Context context, Product product, CartModel cartData) {
            super(context);
            this.context = context;
            this.product = product;
            this.cartData = cartData;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setCanceledOnTouchOutside(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_options);
            for (final Options options : product.getOptions()) {
                if (options.isSelected()) {
                    TextView label = new TextView(context);
                    label.setText(options.getOptionName());
                    ((LinearLayout) findViewById(R.id.options)).addView(label);
                    Log.d("Option - ", options.isSelected() + "");
                    switch (options.getType()) {
                        case "Select":
                        case "Radio":
                            RadioGroup rg = new RadioGroup(context);
                            for (OptionValues optionValues : options.getOptionValues()) {
                                if (optionValues.isSelected()) {
                                    RadioButton optionValuesRadio = new RadioButton(context);

                                    if (!optionValues.getOptionValuePrice().isEmpty())
                                        optionValuesRadio.setText(optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), context), context) + ")");
                                    else
                                        optionValuesRadio.setText(optionValues.getOptionValueName());
                                    optionValuesRadio.setTag(optionValues);
                                    rg.addView(optionValuesRadio);
                                    if (optionValues.isAddToCart()) {
                                        optionValuesRadio.setChecked(true);
                                    }
                                }
                            }
                            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    for (OptionValues optionValues : options.getOptionValues()) {
                                        optionValues.setAddToCart(false);
                                    }
                                    OptionValues optionValues = (OptionValues) findViewById(checkedId).getTag();
                                    if (((RadioButton) findViewById(checkedId)).isChecked())
                                        optionValues.setAddToCart(true);
                                }
                            });
                            ((LinearLayout) findViewById(R.id.options)).addView(rg);
                            break;
                        case "Checkbox":
                            for (OptionValues optionValues : options.getOptionValues()) {
                                if (optionValues.isSelected()) {

                                    CheckBox optionValuesCheckBox = new CheckBox(context);
                                    if (optionValues.isAddToCart()) {
                                        optionValuesCheckBox.setChecked(true);
                                    }
                                    if (!optionValues.getOptionValuePrice().isEmpty())
                                        optionValuesCheckBox.setText(optionValues.getOptionValueName() + "(" + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(optionValues.getOptionValuePrice()), context), context) + ")");
                                    else
                                        optionValuesCheckBox.setText(optionValues.getOptionValueName());
                                    optionValuesCheckBox.setTag(optionValues);
                                    optionValuesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            OptionValues optionValues = (OptionValues) buttonView.getTag();
                                            if (isChecked)
                                                optionValues.setAddToCart(true);
                                            else
                                                optionValues.setAddToCart(false);
                                        }


                                    });
                                    ((LinearLayout) findViewById(R.id.options)).addView(optionValuesCheckBox);
                                }
                            }
                            break;
                        case "Text":
                        case "TextArea":
                            EditText text = new EditText(context);
                            text.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT));
                            text.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    OptionValues optionValues = new OptionValues();
                                    optionValues.setAddToCart(true);
                                    optionValues.setOptionValueName(s + "");
                                    optionValues.setSelected(true);
                                    List<OptionValues> optionValuesList = new ArrayList<>();
                                    optionValuesList.add(optionValues);
                                    options.setOptionValues(optionValuesList);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            ((LinearLayout) findViewById(R.id.options)).addView(text);
                            break;
                    }
                }
            }
            yes = findViewById(R.id.btn_yes);
            no = findViewById(R.id.btn_no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:
                    if (isOptionsValidate(product)) {
                        addToCart(product, cartData);
                        findViewById(R.id.error_text).setVisibility(View.GONE);
                        dismiss();
                    } else {
                        Helper.shake(context, findViewById(R.id.dialog_ll));
                        findViewById(R.id.error_text).setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    dismiss();
                    break;
            }
        }
    }

    boolean isOptionsShow(Product product) {
        for (int i = 0; i < product.getOptions().size(); i++) {
            if (product.getOptions().get(i).isSelected())
                return true;
        }
        return false;
    }

    boolean isOptionsValidate(Product product) {
        int count = 0;
        int enabledOptionCount = 0;
        for (int i = 0; i < product.getOptions().size(); i++) {
            if (product.getOptions().get(i).isSelected())
                for (OptionValues optionValues : product.getOptions().get(i).getOptionValues()) {
                    if (optionValues.isAddToCart()) {
                        count++;
                        break;
                    }
                }
        }

        for (Options options : product.getOptions()) {
            if (options.isSelected())
                enabledOptionCount++;
        }

        if (count == enabledOptionCount)
            return true;
        else
            return false;
    }
}
