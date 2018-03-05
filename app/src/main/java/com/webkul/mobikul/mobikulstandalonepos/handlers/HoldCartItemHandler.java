package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.fromStringToCartModel;

/**
 * Created by aman.gupta on 6/2/18. @Webkul Software Private limited
 */

public class HoldCartItemHandler {
    private Context context;

    public HoldCartItemHandler(Context context) {
        this.context = context;
    }

    public void addToCart(final HoldCart holdCart) {
        DataBaseController.getInstanse().deleteHoldCart(context, holdCart);
        if (fromStringToCartModel(AppSharedPref.getCartData(context)) != null && fromStringToCartModel(AppSharedPref.getCartData(context)).getProducts().size() > 0) {
            CartModel cartData = fromStringToCartModel(AppSharedPref.getCartData(context));
            final HoldCart holdCart1 = new HoldCart();
            holdCart1.setCartData(fromStringToCartModel(AppSharedPref.getCartData(context)));
            holdCart1.setQty(cartData.getTotals().getQty());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
            String date = simpleDateFormat.format(new Date());
            holdCart1.setDate(date);
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String currentTime = simpleDateFormat.format(new Date());
            holdCart1.setTime(currentTime + "");

            DataBaseController.getInstanse().addHoldCart(context, holdCart1, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    AppSharedPref.deleteCartData(context);
                    AppSharedPref.setCartData(context, Helper.fromCartModelToString(holdCart.getCartData()));
                    Intent i = new Intent(context, CartActivity.class);
                    context.startActivity(i);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg + "", Toast.LENGTH_LONG);
                }
            });
        } else {
            AppSharedPref.setCartData(context, Helper.fromCartModelToString(holdCart.getCartData()));
            Intent i = new Intent(context, CartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }
}