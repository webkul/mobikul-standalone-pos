package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityPlaceOrderBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OrderPlacedHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlaceOrderActivity extends BaseActivity {
    ActivityPlaceOrderBinding binding;
    private CashModel cashData;
    private CartModel cartData;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_order);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras().containsKey("cashData")) {
            cashData = (CashModel) getIntent().getExtras().getSerializable("cashData");
        }
        cartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(this));

        final OrderEntity order = new OrderEntity();
        order.setCartData(Helper.fromStringToCartModel(AppSharedPref.getCartData(this)));
        order.setCashData(cashData);
        order.setQty(cartData.getTotals().getQty());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
        String date = simpleDateFormat.format(new Date());
        order.setDate(date);
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = simpleDateFormat.format(new Date());
        order.setTime(currentTime + "");
        binding.setOrderData(order);
        binding.setHandler(new OrderPlacedHandler(this));
        DataBaseController.getInstanse().generateOrder(this, order, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                long orderId = (long) responseData + 10000;
                setTitle("Order #" + orderId);
                ToastHelper.showToast(PlaceOrderActivity.this, successMsg, Toast.LENGTH_SHORT);
                AppSharedPref.deleteCartData(PlaceOrderActivity.this);
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                ToastHelper.showToast(PlaceOrderActivity.this, errorMsg, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        menu.findItem(R.id.menu_item_scan_barcode).setVisible(false);
        return true;
    }
}