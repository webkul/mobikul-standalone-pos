package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.OtherActivity;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.*;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.model.MoreData;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.*;
import static com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref.USER_PREF;

/**
 * Created by aman.gupta on 5/1/18.
 */

public class MoreFragmentHandler {

    private Context context;

    public MoreFragmentHandler(Context context) {
        this.context = context;
    }

    public void performAction(MoreData moreData) {
        Intent i;
        switch (moreData.getId()) {
            case MORE_MENU_CASH_DRAWER:
                i = new Intent(context, CashDrawerActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_SALES_AND_REPORTING:
                i = new Intent(context, SalesAndReportingActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_CUSTOMERS:
                i = new Intent(context, CustomerActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_CATEGORIES:
                i = new Intent(context, CategoryActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_PRODUCTS:
                i = new Intent(context, ProductActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_MY_ACCOUNT_INFO:
                i = new Intent(context, MyAccountInfo.class);
                context.startActivity(i);
                break;
            case MORE_MENU_OPTIONS:
                i = new Intent(context, OptionsActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_TAXES:
                i = new Intent(context, TaxActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_PAYMENT_METHODS:
                i = new Intent(context, PaymentMethodActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_OTHERS:
                i = new Intent(context, OtherActivity.class);
                context.startActivity(i);
                break;
            default:
                ToastHelper.showToast(context, "THIS OPTION IS UNDER DEVELOPMENT MODE!!", Toast.LENGTH_LONG);
        }
    }


    public void signOut() {
        SweetAlertDialog sweetAlert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlert.setTitleText(context.getString(R.string.warning))
                .setContentText("Do you want to logout?" /*+ " Do you want to see?"*/)
                .setConfirmText(context.getResources().getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        AppSharedPref.getSharedPreferenceEditor(context, USER_PREF).clear().apply();
                        Intent i = new Intent(context, SignUpSignInActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        ((MainActivity) context).finish();
                    }
                })
                .setCancelText(context.getResources().getString(R.string.no))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


}