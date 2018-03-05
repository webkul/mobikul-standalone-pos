package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.app.ActivityOptions;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.OptionsAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityOptionsBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.handlers.OptionHandler;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends BaseActivity {
    public ActivityOptionsBinding binding;
    private List<Options> optionsList;
    private OptionsAdapter optionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_options);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Options options = new Options();
        binding.setData(options);
        binding.setHandler(new OptionHandler(this));
        optionsList = new ArrayList<>();
        setOption();
    }

    public void setOption() {
        DataBaseController.getInstanse().getOptions(this, new DataBaseCallBack() {

            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!responseData.toString().equalsIgnoreCase("[]")) {
                    if (!(optionsList.toString().equalsIgnoreCase(responseData.toString()))) {
                        if (optionsList.size() > 0)
                            optionsList.clear();
                        optionsList.addAll((List<Options>) responseData);
                        if (optionsAdapter == null) {
                            optionsAdapter = new OptionsAdapter(OptionsActivity.this, optionsList);
                            binding.optionRv.setAdapter(optionsAdapter);
                        } else {
                            optionsAdapter.notifyDataSetChanged();
                        }
                    }
                    binding.setVisibility(true);
                } else {
                    binding.setVisibility(false);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(OptionsActivity.this, errorMsg + "", Toast.LENGTH_SHORT).show();
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
}
