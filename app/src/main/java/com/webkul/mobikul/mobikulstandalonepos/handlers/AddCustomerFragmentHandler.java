package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 24/1/18. @Webkul Software Private limited
 */

public class AddCustomerFragmentHandler {

    private Context context;

    public AddCustomerFragmentHandler(Context context) {
        this.context = context;
    }

    public void saveCustomer(Customer customer) {
        Toast.makeText(context, customer + "adad", Toast.LENGTH_SHORT).show();
        DataBaseController.getInstanse().addCustomer(context, customer, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(com.webkul.mobikul.mobikulstandalonepos.fragment.AddCustomerFragment.class.getSimpleName());
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
    }

    public void deleteCustomer(Customer customer) {
    }
}
