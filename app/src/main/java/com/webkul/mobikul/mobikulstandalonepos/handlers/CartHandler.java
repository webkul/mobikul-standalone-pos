package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.Checkout;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CustomerActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCartBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by aman.gupta on 18/1/18. @Webkul Software Private limited
 */

public class CartHandler {

    private final String currencySymbol;
    private Context context;
    private ActivityCartBinding binding;

    public CartHandler(Context context, ActivityCartBinding binding) {
        this.context = context;
        this.binding = binding;
        currencySymbol = context.getResources().getString(R.string.currency_symbol);
    }

    public void holdCart(CartModel cartData) {
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
    }

    public void deleteAll() {
        SweetAlertDialog sweetAlert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlert.setTitleText(context.getString(R.string.warning))
                .setContentText("Are you sure?" /*+ " Do you want to see?"*/)
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

    public void selectCustomer() {
        Intent i = new Intent(context, CustomerActivity.class);
        i.putExtra("choose_customer", "");
        ((CartActivity) context).startActivityForResult(i, 2);
    }

    public void customDiscount(CartModel cartData, String customDiscount) {
        if (!customDiscount.isEmpty()) {
            if (Double.parseDouble(customDiscount) <= Double.parseDouble(cartData.getTotals().getSubTotal())) {
                DecimalFormat df = new DecimalFormat("####0.00");
                double newGrandTotal = Double.parseDouble(cartData.getTotals().getGrandTotal()) - Double.parseDouble(customDiscount);
                cartData.getTotals().setFormatedDiscount("-" + currencySymbol + df.format(Double.parseDouble(customDiscount)) + "");
                cartData.getTotals().setGrandTotal(df.format(newGrandTotal) + "");
                cartData.getTotals().setRoundTotal(Math.ceil(newGrandTotal) + "");
                cartData.getTotals().setFormatedGrandTotal(currencySymbol + df.format(newGrandTotal) + "");
                cartData.getTotals().setFormatedRoundTotal(currencySymbol + Math.ceil(newGrandTotal) + "");
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
            cartData.getTotals().setFormatedDiscount("0.00");
            cartData.getTotals().setDiscount("");
            cartData.getTotals().setGrandTotal(df.format(newGrandTotal) + "");
            cartData.getTotals().setRoundTotal(Math.ceil(newGrandTotal) + "");
            cartData.getTotals().setFormatedGrandTotal(currencySymbol + df.format(newGrandTotal) + "");
            cartData.getTotals().setFormatedRoundTotal(currencySymbol + Math.ceil(newGrandTotal) + "");
            AppSharedPref.setCartData(context, Helper.fromCartModelToString(cartData));
            binding.customDiscount.setText("");
            ((CartActivity) context).recreate();
        }
    }
}
