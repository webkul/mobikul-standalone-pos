package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ProductHandler;

public class ProductActivity extends AppCompatActivity {
    public ActivityProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        setSupportActionBar(binding.toolbar);
        binding.setHandler(new ProductHandler(this));
    }
}
