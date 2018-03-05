package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityPlaceOrderBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OrderPlacedHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlaceOrderActivity extends BaseActivity {
    ActivityPlaceOrderBinding binding;
    private CashModel cashData;
    private CartModel cartData;

    List<CashDrawerItems> cashDrawerItemsList;
    CashDrawerItems cashDrawerItems;

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
        cashDrawerItemsList = new ArrayList<>();
        cashDrawerItems = new CashDrawerItems();
        for (Product product : cartData.getProducts()) {
            DataBaseController.getInstanse().updateProductQty(PlaceOrderActivity.this, product);
        }
        final OrderEntity order = new OrderEntity();
        order.setCartData(Helper.fromStringToCartModel(AppSharedPref.getCartData(this)));
        order.setCashData(cashData);
        order.setQty(cartData.getTotals().getQty());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
        final String date = simpleDateFormat.format(new Date());
        order.setDate(date);
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = simpleDateFormat.format(new Date());
        order.setTime(currentTime + "");
        binding.setOrderData(order);
        binding.setHandler(new OrderPlacedHandler(this));
        final CashDrawerModel cashDrawerModel = new CashDrawerModel();


        DataBaseController.getInstanse().generateOrder(this, order, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                final long orderId = (long) responseData + 10000;
                setTitle("Order #" + orderId);
                ToastHelper.showToast(PlaceOrderActivity.this, successMsg, Toast.LENGTH_SHORT);
                AppSharedPref.deleteCartData(PlaceOrderActivity.this);

                DataBaseController.getInstanse().getCashHistoryByDate(PlaceOrderActivity.this, date, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {

                        CashDrawerModel cashDrawerModel1 = (CashDrawerModel) responseData;
                        DecimalFormat df = new DecimalFormat("####0.00");
                        Log.d(TAG, "onSuccess: " + new Gson().toJson(cashDrawerModel1));
                        cashDrawerModel.setInAmount(df.format(Double.parseDouble(cashData.getCollectedCash()) + Double.parseDouble(cashDrawerModel1.getInAmount())) + "");
                        cashDrawerModel.setOutAmount(df.format(Double.parseDouble(cashData.getChangeDue()) + Double.parseDouble(cashDrawerModel1.getOutAmount())) + "");
                        cashDrawerModel.setNetRevenue(df.format(Double.parseDouble(cashDrawerModel.getInAmount()) - Double.parseDouble(cashDrawerModel.getOutAmount())) + "");
                        cashDrawerModel.setClosingBalance(df.format(Double.parseDouble(cashDrawerModel1.getClosingBalance()) + Double.parseDouble(cashData.getCollectedCash()) - Double.parseDouble(cashData.getChangeDue())) + "");
                        cashDrawerModel.setDate(cashDrawerModel1.getDate());

                        cashDrawerItemsList = cashDrawerModel1.getCashDrawerItems();
                        cashDrawerItems.setChangeDue(cashData.getChangeDue());
                        cashDrawerItems.setCollectedCash(cashData.getCollectedCash());
                        cashDrawerItems.setTotal(cashData.getTotal());
                        cashDrawerItems.setOrderId(orderId + "");
                        cashDrawerItemsList.add(cashDrawerItems);

                        cashDrawerModel.setCashDrawerItems(cashDrawerItemsList);

                        DataBaseController.getInstanse().updateCashDrawer(PlaceOrderActivity.this, cashDrawerModel, new DataBaseCallBack() {
                            @Override
                            public void onSuccess(Object responseData, String successMsg) {
                            }

                            @Override
                            public void onFailure(int errorCode, String errorMsg) {
                                Toast.makeText(PlaceOrderActivity.this, errorMsg + "", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(PlaceOrderActivity.this, errorMsg, Toast.LENGTH_SHORT);
                    }
                });

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