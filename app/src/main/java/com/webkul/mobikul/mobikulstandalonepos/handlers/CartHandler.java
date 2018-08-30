package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.Checkout;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CustomerActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.customviews.CustomDialogClass;
import com.webkul.mobikul.mobikulstandalonepos.customviews.CustomDialogClassForDiscount;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCartBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 18/1/18. @Webkul Software Private limited
 */

public class CartHandler {

    private final String currencySymbol;
    private Context context;
    private ActivityCartBinding binding;
    private double tax;

    public CartHandler(Context context, ActivityCartBinding binding) {
        this.context = context;
        this.binding = binding;
        currencySymbol = context.getResources().getString(R.string.currency_symbol);
    }

    public CartHandler(Context context) {
        this.context = context;
        currencySymbol = context.getResources().getString(R.string.currency_symbol);
    }

    public void holdCart(CartModel cartData) {
        if (!AppSharedPref.isReturnCart(context)) {
            final HoldCart holdCart = new HoldCart();
            holdCart.setCartData(Helper.fromStringToCartModel(AppSharedPref.getCartData(context)));
            holdCart.setQty(cartData.getTotals().getQty());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
            String date = simpleDateFormat.format(new Date());
            holdCart.setDate(date);
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String currentTime = simpleDateFormat.format(new Date());
            holdCart.setTime(currentTime + "");

            DataBaseController.getInstanse().addHoldCart(context, holdCart, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    AppSharedPref.deleteCartData(context);
                    binding.setData(Helper.fromStringToCartModel(AppSharedPref.getCartData(context)));
                    binding.setVisibility(false);
                    binding.delete.setVisibility(View.GONE);
                    SweetAlertBox.getInstance().showSuccessPopUp(context, context.getString(R.string.success), successMsg);
                    ToastHelper.showToast(context, successMsg + "", Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg + "", Toast.LENGTH_LONG);
                }
            });
        } else {
            ToastHelper.showToast(context, "First complete Return Order!", 1000);
        }
    }

    public void deleteAll() {
        SweetAlertDialog sweetAlert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlert.setTitleText(context.getString(R.string.warning))
                .setContentText("Do you want to empty cart?" /*+ " Do you want to see?"*/)
                .setConfirmText(context.getResources().getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        AppSharedPref.deleteCartData(context);
                        ((CartActivity) context).recreate();
                    }
                })
                .setCancelText(context.getResources().getString(R.string.no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void proceedToCheckout(CartModel cartData) {
        if (!cartData.getCustomer().getEmail().isEmpty()) {
            Intent i = new Intent(context, Checkout.class);
            i.putExtra("cartData", cartData);
            context.startActivity(i);
        } else {
            ToastHelper.showToast(context, "Select Customer first!", Toast.LENGTH_LONG);
            Helper.shake(context, binding.customerView);
        }
    }

    public void increaseQuantity(Product product) {
        int qty = Integer.parseInt(product.getCartQty());
        if (qty < Integer.parseInt(product.getQuantity())) {
            qty++;
            product.setCartQty("" + qty);
            updateCart(product, true);
        } else
            ToastHelper.showToast(context, "Can't be increase quantity! You already reach at max level.", Toast.LENGTH_LONG);

    }

    public void decreaseQuantity(Product product) {
        int qty = Integer.parseInt(product.getCartQty());
        if (qty != 1) {
            qty--;
            product.setCartQty("" + qty);
            updateCart(product, false);
        } else {
            ToastHelper.showToast(context, "Can't be decrease quantity! You already reach at min level.", Toast.LENGTH_LONG);
        }
    }

    public void applyDiscount(Product product, CartModel cartData) {
        CustomDialogClassForDiscount customDialogClass = new CustomDialogClassForDiscount(context, product, cartData);
        customDialogClass.show();
    }

    private void updateCart(Product product, boolean isIncrease) {
        int qty = Integer.parseInt(product.getCartQty());
        double price;
        double basePrice;
        if (product.getSpecialPrice().isEmpty()) {
            price = Helper.currencyConverter(Double.parseDouble(product.getPrice()), context);
            basePrice = Double.parseDouble(product.getPrice());
        } else {
            price = Helper.currencyConverter(Double.parseDouble(product.getSpecialPrice()), context);
            basePrice = Double.parseDouble(product.getSpecialPrice());
        }

        for (int i = 0; i < product.getOptions().size(); i++) {
            if (!product.getOptions().get(i).getType().equalsIgnoreCase("text") && !product.getOptions().get(i).getType().equalsIgnoreCase("textarea"))
                for (OptionValues optionValues : product.getOptions().get(i).getOptionValues()) {
                    if (optionValues.isAddToCart()) {
                        if (!optionValues.getOptionValuePrice().isEmpty()) {
                            price = price + Integer.parseInt(optionValues.getOptionValuePrice());
                            basePrice = basePrice + Integer.parseInt(optionValues.getOptionValuePrice());
                        }
                    }
                }
        }
        product.setCartProductSubtotal(qty * basePrice + "");
        product.setFormattedCartProductSubtotal(Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(product.getCartProductSubtotal()), context), context));
        Log.d(TAG, "updateCart: " + product.getCartQty());
        Log.d(TAG, "updateCart: " + product.getFormattedCartProductSubtotal());


        CartModel cartData = ((CartActivity) context).binding.getData();
        int position = 0;
        for (Product product1 : cartData.getProducts()) {
            if (product.getPId() == product1.getPId()) {
                break;
            }
            position++;
        }
        cartData.getProducts().remove(position);
        cartData.getProducts().add(position, product);
        int totalQty = Integer.parseInt(cartData.getTotals().getQty());
        if (isIncrease) {
            cartData.getTotals().setSubTotal(Double.parseDouble(cartData.getTotals().getSubTotal()) + basePrice + "");
            cartData.getTotals().setQty(++totalQty + "");
        } else {
            cartData.getTotals().setSubTotal(Double.parseDouble(cartData.getTotals().getSubTotal()) - basePrice + "");
            cartData.getTotals().setQty(--totalQty + "");
        }
        DecimalFormat df;
        df = new DecimalFormat("####0.00");
        cartData.getTotals().setTax(calculateTax(product, Double.parseDouble(product.getCartProductSubtotal())));
        double grandTotal = Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getSubTotal()), context) + Double.parseDouble(cartData.getTotals().getTax());
        cartData.getTotals().setGrandTotal(df.format(grandTotal) + "");
        cartData.getTotals().setRoundTotal(Math.ceil(grandTotal) + "");
        // set formated values
        String returnSymbol = "";
        if (AppSharedPref.isReturnCart(context)) {
            returnSymbol = "-";
        }
        cartData.getTotals().setFormatedSubTotal(returnSymbol + Helper.currencyFormater(Helper.currencyConverter(Double.parseDouble(cartData.getTotals().getSubTotal()), context), context));
        cartData.getTotals().setFormatedTax(Helper.currencyFormater(Double.parseDouble(cartData.getTotals().getTax()), context));
        cartData.getTotals().setFormatedGrandTotal(returnSymbol + Helper.currencyFormater(Double.parseDouble(df.format(grandTotal)), context));
        cartData.getTotals().setFormatedRoundTotal(returnSymbol + Helper.currencyFormater((Math.ceil(grandTotal)), context));
        AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
    }

    public void proceedToCheckout(CartModel cartData, boolean hasReturn) {
        if (!cartData.getCustomer().getEmail().isEmpty()) {
            Intent i = new Intent(context, Checkout.class);
            i.putExtra("cartData", cartData);
            i.putExtra("return", hasReturn);
            context.startActivity(i);
        } else {
            ToastHelper.showToast(context, "Select Customer first!", Toast.LENGTH_LONG);
            Helper.shake(context, binding.customerView);
        }
    }

    public void selectCustomer(Customer customer) {
        Intent i = new Intent(context, CustomerActivity.class);
        i.putExtra("choose_customer", "");
        i.putExtra("customer", customer);
        ((CartActivity) context).startActivityForResult(i, 2);
    }

    public void customDiscount(CartModel cartData, String customDiscount) {
        if (!customDiscount.isEmpty()) {
            if (Double.parseDouble(customDiscount) <= Double.parseDouble(cartData.getTotals().getSubTotal())) {
                DecimalFormat df = new DecimalFormat("####0.00");
                double newGrandTotal = Double.parseDouble(cartData.getTotals().getGrandTotal()) - Double.parseDouble(customDiscount);
                double totalDiscount = Double.parseDouble(customDiscount) + cartData.getTotals().getTotalDiscountByProduct();
                cartData.getTotals().setFormatedDiscount("-" + Helper.currencyFormater(Double.parseDouble(df.format(totalDiscount)), context) + "");
                cartData.getTotals().setGrandTotal(df.format(newGrandTotal) + "");
                cartData.getTotals().setRoundTotal(Math.ceil(newGrandTotal) + "");
                cartData.getTotals().setFormatedGrandTotal(Helper.currencyFormater(Double.parseDouble(df.format(newGrandTotal)), context) + "");
                cartData.getTotals().setFormatedRoundTotal(Helper.currencyFormater(Double.parseDouble(df.format(Math.ceil(newGrandTotal))), context) + "");
                AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
                ((CartActivity) context).recreate();
            } else {
                ToastHelper.showToast(context, "Custom discount cannot be more then subtotal!", Toast.LENGTH_LONG);
                binding.customDiscount.requestFocus();
                binding.customDiscount.setText("");
                Helper.shake(context, binding.customerCustomDiscountTnl);
            }
        } else {
            ToastHelper.showToast(context, "Custom discount is empty!", Toast.LENGTH_LONG);
            binding.customDiscount.requestFocus();
            Helper.shake(context, binding.customerCustomDiscountTnl);
        }
    }

    public void removeCustomDiscount(CartModel cartData) {
        if (!cartData.getTotals().getDiscount().isEmpty()) {
            DecimalFormat df = new DecimalFormat("####0.00");
            double newGrandTotal = Double.parseDouble(cartData.getTotals().getGrandTotal()) + Double.parseDouble(cartData.getTotals().getDiscount());
            cartData.getTotals().setFormatedDiscount(Helper.currencyFormater(cartData.getTotals().getTotalDiscountByProduct(), context));
            cartData.getTotals().setDiscount("");
            cartData.getTotals().setGrandTotal(df.format(newGrandTotal) + "");
            cartData.getTotals().setRoundTotal(Math.ceil(newGrandTotal) + "");
            cartData.getTotals().setFormatedGrandTotal(Helper.currencyFormater(Double.parseDouble(df.format(newGrandTotal)), context) + "");
            cartData.getTotals().setFormatedRoundTotal(Helper.currencyFormater(Double.parseDouble(df.format(Math.ceil(newGrandTotal))), context) + "");
            AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
            binding.customDiscount.setText("");
            ((CartActivity) context).recreate();
        }
    }

    public String calculateTax(Product product, double price) {
        double taxRate = 0.00;
        if (product.isTaxableGoodsApplied() && product.getProductTax() != null && !product.getProductTax().toString().isEmpty()) {
            if (product.getProductTax().getType().contains("%")) {
                taxRate = (price / 100.0f) * Double.parseDouble(product.getProductTax().getTaxRate());
            } else {
                taxRate = Helper.currencyConverter(Double.parseDouble(product.getProductTax().getTaxRate()), context);
            }
        }
        return taxRate + "";
    }
}