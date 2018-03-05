package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCustomerFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;
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

    public void saveCustomer(Customer customer, boolean isEdit) {

        if (isValidated(customer)) {
            if (!isEdit) {
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
            } else {
                DataBaseController.getInstanse().updateCustomer(context, customer, new DataBaseCallBack() {
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
        }
    }

    public boolean isValidated(Customer customer) {
        customer.setDisplayError(true);
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddCustomerFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            AddCustomerFragment categoryFragment = ((AddCustomerFragment) fragment);
            if (!customer.getFirstNameError().isEmpty()) {
                categoryFragment.binding.firstName.requestFocus();
                return false;
            }
            if (!customer.getLastNameError().isEmpty()) {
                categoryFragment.binding.lastName.requestFocus();
                return false;
            }
            if (!customer.getEmailError().isEmpty()) {
                categoryFragment.binding.customerEmail.requestFocus();
                return false;
            }
            if (!customer.getContactNumberError().isEmpty()) {
                categoryFragment.binding.customerContactNo.requestFocus();
                return false;
            }
            customer.setDisplayError(false);
            return true;
        }
        return false;
    }

    public void deleteCustomer(Customer customer) {
        if (customer != null) {
            DataBaseController.getInstanse().deleteCustomer(context, customer, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddCustomerFragment.class.getSimpleName());
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
    }
}
