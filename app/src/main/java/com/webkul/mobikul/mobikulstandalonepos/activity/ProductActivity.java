package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityProductBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;
import com.webkul.mobikul.mobikulstandalonepos.handlers.ProductHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends BaseActivity {
    public ActivityProductBinding binding;
    private List<Product> products;
    private ProductAdapter productAdapter;
    public final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        setSupportActionBar(binding.toolbar);
        Product product = new Product(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setHandler(new ProductHandler(this));
        products = new ArrayList<>();
        binding.setData(product);
        binding.setVisibility(true);
        setProduct();
    }

    public void setProduct() {
        DataBaseController.getInstanse().getProducts(this, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    if (!(products.toString().equalsIgnoreCase(responseData.toString()))) {
                        if (products.size() > 0)
                            products.clear();
                        products.addAll((List<Product>) responseData);
                        if (productAdapter == null) {
                            productAdapter = new ProductAdapter(ProductActivity.this, products);
                            binding.productRv.setAdapter(productAdapter);
                        } else {
                            productAdapter.notifyDataSetChanged();
                        }
                    }
                    binding.setVisibility(true);
                } else {
                    binding.setVisibility(false);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(ProductActivity.this, errorMsg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri uri = Helper.saveToInternalStorage(ProductActivity.this, photo, binding.getData().getPId() + "");
                Fragment fragment = mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                ((AddProductFragment) fragment).binding.getData().setImage(uri.toString());
                synchronized (binding.getData()) {
                    binding.getData().notifyAll();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        menu.findItem(R.id.menu_item_scan_barcode).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}