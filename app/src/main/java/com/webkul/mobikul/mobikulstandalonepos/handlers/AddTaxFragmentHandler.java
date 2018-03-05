package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddTaxFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 27/2/18. @Webkul Software Private limited
 */

public class AddTaxFragmentHandler {

    private Context context;

    public AddTaxFragmentHandler(Context context) {
        this.context = context;
    }

    public void save(Tax data, boolean isEdit) {
        if (isValidated(data)) {
            if (!isEdit) {
                DataBaseController.getInstanse().addTaxRate(context, data, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddTaxFragment.class.getSimpleName());
                        FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                        ft.detach(fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();
                        ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                        ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                    }
                });
            } else {
                DataBaseController.getInstanse().updateTax(context, data, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddTaxFragment.class.getSimpleName());
                        FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                        ft.detach(fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();
                        ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                        ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(context, errorMsg + "", Toast.LENGTH_LONG);
                    }
                });
            }
        } else
            Toast.makeText(context, "Please check the form carefully!!", Toast.LENGTH_SHORT).show();
    }

    public void deleteTax(Tax data) {
        if (data != null) {
            DataBaseController.getInstanse().deleteTax(context, data, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                }
            });
        }
    }

    public boolean isValidated(Tax tax) {
        tax.setDisplayError(true);
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddTaxFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            AddTaxFragment taxFragment = ((AddTaxFragment) fragment);
            if (!tax.getTaxNameError().isEmpty()) {
                taxFragment.binding.taxName.requestFocus();
                return false;
            }
            if (!tax.getTaxRateError().isEmpty()) {
                taxFragment.binding.taxRate.requestFocus();
                return false;
            }
            tax.setDisplayError(false);
            return true;
        }
        return false;
    }
}
