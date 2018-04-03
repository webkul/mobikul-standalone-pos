package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CartProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityViewOrderDetailsBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OrderFragmentHandler;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import static com.webkul.mobikul.mobikulstandalonepos.constants.BundleConstants.BUNDLE_ORDER_DATA;

public class ViewOrderDetails extends BaseActivity {
    ActivityViewOrderDetailsBinding binding;
    OrderEntity orderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_order_details);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras().containsKey(BUNDLE_ORDER_DATA)) {
            orderData = (OrderEntity) getIntent().getExtras().getSerializable(BUNDLE_ORDER_DATA);
            binding.setData(orderData);
            Log.d(TAG, "onCreate: " + orderData.getIsReturn());
            if (!orderData.getIsReturn())
                setTitle(String.format(getString(R.string.order_id), orderData.getOrderId() + ""));
            else
                setTitle(String.format(getString(R.string.return_order_id), orderData.getOrderId() + ""));
            setAdapter(orderData);
        } else if (getIntent().getExtras().containsKey("order_id")) {
            DataBaseController.getInstanse().getOrderById(this, getIntent().getExtras().getString("order_id"), new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    orderData = (OrderEntity) responseData;
                    binding.setData(orderData);
                    setTitle(String.format(getString(R.string.return_order_id), orderData.getOrderId() + ""));
                    setAdapter(orderData);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {

                }
            });
        }

        binding.setHandler(new OrderFragmentHandler(this));
    }

    void setAdapter(OrderEntity orderData) {
        CartProductAdapter cartProductAdapter = new CartProductAdapter(ViewOrderDetails.this, orderData.getCartData().getProducts());
        binding.productRv.setAdapter(cartProductAdapter);
        binding.productRv.setNestedScrollingEnabled(false);
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
