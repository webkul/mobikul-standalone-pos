package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

/**
 * Created by aman.gupta on 25/1/18. @Webkul Software Private limited
 */

public class OrderPlacedHandler {
    private Context context;

    public OrderPlacedHandler(Context context) {
        this.context = context;
    }

    public void printInvoice(OrderEntity orderData) {

    }

    public void mailInvoice(OrderEntity orderData) {

    }

    public void goToHome() {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }
}
