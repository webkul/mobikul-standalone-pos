package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.ActivityNotFoundException;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityPaymentMethodBinding;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;

public class PaymentMethodActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaymentMethodBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_method);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.enableCash.setChecked(AppSharedPref.isCashEnabled(PaymentMethodActivity.this, true));
        binding.enableCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppSharedPref.setCashEnabled(PaymentMethodActivity.this, isChecked);
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
