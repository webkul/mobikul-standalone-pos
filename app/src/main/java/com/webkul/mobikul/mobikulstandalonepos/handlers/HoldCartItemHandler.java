package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 6/2/18. @Webkul Software Private limited
 */

public class HoldCartItemHandler {
    private Context context;

    public HoldCartItemHandler(Context context) {
        this.context = context;
    }

    public void addToCart(HoldCart holdCart) {
        DataBaseController.getInstanse().deleteHoldCart(context, holdCart);
        AppSharedPref.setCartData(context, Helper.fromCartModelToString(holdCart.getCartData()));
        Intent i = new Intent(context, CartActivity.class);
        context.startActivity(i);

    }
}
