package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityMyAccountInfoBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.handlers.MyAccountInfoHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

public class MyAccountInfo extends BaseActivity {
    public ActivityMyAccountInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account_info);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataBaseController.getInstanse().getAdminData(this, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                binding.setData((Administrator) responseData);
                binding.setFirstName(((Administrator) responseData).getFirstName());
                binding.setLastName(((Administrator) responseData).getLastName());
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(MyAccountInfo.this, errorMsg + "", Toast.LENGTH_SHORT).show();
            }
        });
        binding.setHandler(new MyAccountInfoHandler(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        final MenuItem barcodeItem = menu.findItem(R.id.menu_item_scan_barcode);
        barcodeItem.setVisible(false);
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
