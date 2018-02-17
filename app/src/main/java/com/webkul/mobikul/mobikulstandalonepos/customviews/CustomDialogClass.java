package com.webkul.mobikul.mobikulstandalonepos.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Dialog d;
    public Button yes, no;
    private Context context;

    public CustomDialogClass(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = findViewById(R.id.btn_yes);
        yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                CashDrawerModel cashDrawerModel = new CashDrawerModel();
                cashDrawerModel.setOpeningBalance(((EditText) findViewById(R.id.opening_balance)).getText().toString());
                cashDrawerModel.setClosingBalance(((EditText) findViewById(R.id.opening_balance)).getText().toString());
                cashDrawerModel.setInAmount("0.00");
                cashDrawerModel.setOutAmount("0.00");
                cashDrawerModel.setNetRevenue("0.00");
                List<CashDrawerItems> cashDrawerItemsList = new ArrayList<>();
                cashDrawerModel.setCashDrawerItems(cashDrawerItemsList);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
                String date = simpleDateFormat.format(new Date());
                cashDrawerModel.setDate(date);
                AppSharedPref.setDate(context, date);
                DataBaseController.getInstanse().addOpeningBalance(context, cashDrawerModel);
                break;
            default:
                break;
        }
        dismiss();
    }
}