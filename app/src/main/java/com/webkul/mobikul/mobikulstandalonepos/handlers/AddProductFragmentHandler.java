package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CategoryActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.ManageCategoriesFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

/**
 * Created by aman.gupta on 10/1/18. @Webkul Software Private limited
 */

public class AddProductFragmentHandler {

    private Context context;

    public AddProductFragmentHandler(Context context) {
        this.context = context;
    }

    public void addNEditProfile() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ((ProductActivity) context).startActivityForResult(cameraIntent, ((ProductActivity) context).CAMERA_REQUEST);
    }

    public void saveProduct(Product product, boolean isEdit) {
        if (isValidated(product)) {
            if (!isEdit) {
                DataBaseController.getInstanse().addProduct(context, product, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
                        FragmentTransaction ft = ((BaseActivity) context).mSupportFragmentManager.beginTransaction();
                        ft.detach(fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();
                        ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                    }
                });
            } else {
                DataBaseController.getInstanse().updateProduct(context, product, new DataBaseCallBack() {
                    @Override
                    public void onSuccess(Object responseData, String successMsg) {
                        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
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
        } else {
            Toast.makeText(context, "Please check the form carefully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectCategories(Product product) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(ManageCategoriesFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new ManageCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((ProductActivity) context).binding.productFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();

    }

    public void deleteProduct(Product product) {
        if (product != null) {

            DataBaseController.getInstanse().deleteProduct(context, product, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
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

    public boolean isValidated(Product product) {
        product.setDisplayError(true);
        Fragment fragment = ((ProductActivity) context).mSupportFragmentManager.findFragmentByTag(AddProductFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            AddProductFragment productFragment = ((AddProductFragment) fragment);
            if (!product.getSkuError().isEmpty()) {
                productFragment.binding.sku.requestFocus();
                return false;
            }
            if (!product.getProductNameError().isEmpty()) {
                productFragment.binding.productName.requestFocus();
                return false;
            }
            if (!product.getPriceError().isEmpty()) {
                productFragment.binding.price.requestFocus();
                return false;
            }
            if (!product.getQuantityError().isEmpty()) {
                productFragment.binding.quantity.requestFocus();
                return false;
            }
            if (!product.getWeightError().isEmpty()) {
                productFragment.binding.weight.requestFocus();
                return false;
            }
            product.setDisplayError(false);
            return true;
        }
        return false;
    }


}
