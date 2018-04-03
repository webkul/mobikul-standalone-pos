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
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.ProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCashDrawerBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.ArrayList;
import java.util.List;

public class CashDrawerActivity extends BaseActivity {
    public ActivityCashDrawerBinding binding;
    List<CashDrawerModel> cashDrawerModelsList;
    CashDrawerAdapter cashDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cash_drawer);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setVisibility(false);
        cashDrawerModelsList = new ArrayList<>();
        setCashDrawerList();
    }

    public void setCashDrawerList() {
        DataBaseController.getInstanse().getAllCashHistory(this, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    if (!(cashDrawerModelsList.toString().equalsIgnoreCase(responseData.toString()))) {
                        if (cashDrawerModelsList.size() > 0)
                            cashDrawerModelsList.clear();
                        cashDrawerModelsList.addAll((List<CashDrawerModel>) responseData);
                        if (cashDrawerAdapter == null) {
                            cashDrawerAdapter = new CashDrawerAdapter(CashDrawerActivity.this, cashDrawerModelsList);
                            binding.cashDrawerRv.setAdapter(cashDrawerAdapter);
                        } else {
                            cashDrawerAdapter.notifyDataSetChanged();
                        }
                    }
                    binding.setVisibility(true);
                } else {
                    binding.setVisibility(false);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(CashDrawerActivity.this, errorMsg + "", Toast.LENGTH_SHORT).show();
            }
        });
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
