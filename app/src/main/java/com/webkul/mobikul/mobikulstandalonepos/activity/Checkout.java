package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCheckoutBinding;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CheckoutHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

public class Checkout extends BaseActivity {
    public ActivityCheckoutBinding checkoutBinding;
    CartModel cartData;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
        setSupportActionBar(checkoutBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras().containsKey("cartData")) {
            cartData = (CartModel) getIntent().getExtras().getSerializable("cartData");
            checkoutBinding.setData(cartData);
        }
        checkoutBinding.setVisibility(AppSharedPref.isCashEnabled(this, true));
        checkoutBinding.setHandler(new CheckoutHandler(this));
        checkoutBinding.setHasReturn(AppSharedPref.isReturnCart(this));
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
