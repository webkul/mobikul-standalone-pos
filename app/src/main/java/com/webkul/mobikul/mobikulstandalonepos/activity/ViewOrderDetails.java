package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CartProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityViewOrderDetailsBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

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
        }
        setTitle(String.format(getString(R.string.order_id), orderData.getOrderId() + ""));
        CartProductAdapter cartProductAdapter = new CartProductAdapter(this, orderData.getCartData().getProducts());
        binding.productRv.setAdapter(cartProductAdapter);
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
