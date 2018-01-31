package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.ViewOrderDetails;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;

import static com.webkul.mobikul.mobikulstandalonepos.constants.BundleConstants.BUNDLE_ORDER_DATA;

/**
 * Created by aman.gupta on 30/1/18. @Webkul Software Private limited
 */

public class OrderFragmentHandler {

    private Context context;

    public OrderFragmentHandler(Context context) {
        this.context = context;
    }

    public void onClickOrderItem(OrderEntity orderData) {

        Intent i = new Intent(context, ViewOrderDetails.class);
        i.putExtra(BUNDLE_ORDER_DATA, orderData);
        context.startActivity(i);
    }
}
