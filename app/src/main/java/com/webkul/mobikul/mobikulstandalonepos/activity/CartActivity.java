package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CartProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCartBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CartHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.RecyclerClick_Listener;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.TotalModel;
import com.webkul.mobikul.mobikulstandalonepos.utils.RecyclerTouchListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartActivity extends BaseActivity {

    public ActivityCartBinding binding;
    private ActionMode mActionMode;
    private List<Product> multiselect_list = new ArrayList<>();
    private List<Product> cartProductList;
    private CartProductAdapter adapter;
    private CartModel cartData;
    private DecimalFormat df;
    private double grandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(this));
        df = new DecimalFormat("####0.00");
        if (cartData != null)
            currencyConverter();
        cartProductList = new ArrayList<>();
        setCartProducts(cartData);
        implementRecyclerViewClickListeners();
    }

    private void currencyConverter() {
        List<Product> list = new ArrayList<>();
        for (Product product : cartData.getProducts()) {
            product.setFormattedPrice(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getPrice()), this), this));
            if (!product.getSpecialPrice().isEmpty())
                product.setFormattedSpecialPrice(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getSpecialPrice()), this), this));
            Log.d(TAG, "currencyConverter: " + Helper.currencyConverter(Double.parseDouble(product.getCartProductSubtotal()), this));
            product.setFormattedCartProductSubtotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getCartProductSubtotal()), this), this));

//            for (Options option : product.getOptions()) {
//                for (OptionValues optionValues : option.getOptionValues()) {
//                    if (!optionValues.getOptionValuePrice().isEmpty())
//                        optionValues.setOptionValuePrice();
//                    optionValues.getOptionValuePrice()
//                }
//            }
            list.add(product);
        }
        cartData.setProducts(list);
        TotalModel totalModel = cartData.getTotals();
        totalModel.setFormatedSubTotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getSubTotal()), this), this));
        if (!cartData.getTotals().getDiscount().isEmpty()) {
            totalModel.setFormatedDiscount(Helper.currencyFormater(Double.parseDouble(cartData.getTotals().getDiscount()), this));
        }
        if (!cartData.getTotals().getTax().isEmpty())
            totalModel.setFormatedTax(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getTax()), this), this));
        totalModel.setFormatedGrandTotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getGrandTotal()), this), this));
        totalModel.setFormatedRoundTotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getRoundTotal()), this), this));
        cartData.setTotals(totalModel);
        Log.d(TAG, "currencyConverter: " + new Gson().toJson(cartData));
        AppSharedPref.setCartData(context, new Gson().toJson(cartData));
    }

    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    for (Product product : multiselect_list) {
                        for (int i = 0; i < cartProductList.size(); i++) {
                            if (product.getPId() == cartProductList.get(i).getPId()) {
                                Log.d(TAG, "onActionItemClicked: " + cartProductList.get(i).getProductName());
                                cartProductList.remove(i);
                                break;
                            }
                        }
                    }

                    List<Product> temp = new ArrayList<>();
                    temp.addAll(cartProductList);

                    cartData.setProducts(temp);
                    adapter.notifyDataSetChanged();
                    onDeleteCartItem(cartData);
                    setNullToActionMode();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            setNullToActionMode();
            multiselect_list = new ArrayList<>();
            adapter.removeSelection();
        }
    };

    private void onDeleteCartItem(CartModel cartData) {
        double subTotal = 0.00;
        int totalQty = 0;
        for (int i = 0; i < cartData.getProducts().size(); i++) {
            Product product = cartData.getProducts().get(i);
            double price;
            if (product.getSpecialPrice().isEmpty()) {
                price = Helper.currencyConverter(Double.parseDouble(product.getPrice()), this);
                subTotal = subTotal + price * Integer.parseInt(product.getCartQty());
            } else {
                price = Helper.currencyConverter(Double.parseDouble(product.getSpecialPrice()), this);
                subTotal = subTotal + price * Integer.parseInt(product.getCartQty());
            }
            if (Integer.parseInt(product.getCartQty()) > 1) {
                totalQty = totalQty + Integer.parseInt(product.getCartQty());
            } else
                totalQty++;
            double taxRate = 0;
            if (product.isTaxableGoodsApplied() && product.getProductTax() != null && !product.getProductTax().toString().isEmpty()) {
                if (product.getProductTax().getType().contains("%")) {
                    taxRate = (price / 100.0f) * Double.parseDouble(product.getProductTax().getTaxRate());
                } else {
                    taxRate = Double.parseDouble(product.getProductTax().getTaxRate());
                }
            }
            cartData.getTotals().setTax(df.format(Double.parseDouble(cartData.getTotals().getTax()) + taxRate) + "");
            grandTotal = subTotal + Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getTax()), this);
        }
        cartData.getTotals().setSubTotal(df.format(subTotal) + "");
        cartData.getTotals().setQty(totalQty + "");
        cartData.getTotals().setTax(cartData.getTotals().getTax());
        cartData.getTotals().setGrandTotal(df.format(grandTotal) + "");
        cartData.getTotals().setRoundTotal(Math.ceil(grandTotal) + "");
        // set formated values
        cartData.getTotals().setFormatedSubTotal(Helper.currencyFormater(subTotal, this) + "");
        cartData.getTotals().setFormatedTax(Helper.currencyFormater(Double.parseDouble(cartData.getTotals().getTax()), this));
        cartData.getTotals().setFormatedGrandTotal(Helper.currencyFormater(grandTotal, this) + "");
        cartData.getTotals().setFormatedRoundTotal(Helper.currencyFormater(Math.ceil(grandTotal), this) + "");
        AppSharedPref.setCartData(this, Helper.fromCartModelToString(cartData));
        Log.d(TAG, "onDeleteCartItem: " + cartProductList.size());
        setCartProducts(cartData);
    }

    private void setCartProducts(CartModel cartData) {
        binding.setData(cartData);
        binding.setHasReturn(AppSharedPref.isReturnCart(this));

        binding.setHandler(new CartHandler(this, binding));
        if (cartData != null && cartData.getProducts().size() > 0) {
            if (cartProductList.size() > 0)
                cartProductList.clear();
            cartProductList.addAll(cartData.getProducts());
            adapter = new CartProductAdapter(this, cartProductList, true, cartData);
            binding.cartProductRv.setAdapter(adapter);
            binding.cartProductRv.setNestedScrollingEnabled(false);
            binding.setVisibility(true);
            if (AppSharedPref.isReturnCart(this)) {
                if (!cartData.getTotals().getFormatedSubTotal().contains("-")) {
                    cartData.getTotals().setFormatedSubTotal("-" + cartData.getTotals().getFormatedSubTotal());
                    cartData.getTotals().setFormatedGrandTotal("-" + cartData.getTotals().getFormatedGrandTotal());
                    cartData.getTotals().setFormatedRoundTotal("-" + cartData.getTotals().getFormatedRoundTotal());
                }
            }
        } else {
            binding.setVisibility(false);
            AppSharedPref.setReturnCart(this, false);
        }
        if (cartData != null && cartData.getTotals().getDiscount().isEmpty())
            binding.customerCustomDiscountTnl.setEnabled(true);
        else
            binding.customerCustomDiscountTnl.setEnabled(false);
    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        binding.cartProductRv.addOnItemTouchListener(new RecyclerTouchListener(this, binding.cartProductRv, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                multiselect_list = new ArrayList<>();
                onListItemSelect(position);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not
        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = this.startSupportActionMode(mActionModeCallback);
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " selected");
            if (!multiselect_list.contains(cartProductList.get(position)))
                multiselect_list.add(cartProductList.get(position));
            else
                multiselect_list.remove(cartProductList.get(position));
        }
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartProducts(Helper.fromStringToCartModel(AppSharedPref.getCartData(this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        menu.findItem(R.id.menu_item_scan_barcode).setVisible(false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            Customer customer = (Customer) data.getExtras().getSerializable("customer");
            binding.getData().setCustomer(customer);
            AppSharedPref.setCartData(this, Helper.fromCartModelToString(binding.getData()));
        }
    }
}
