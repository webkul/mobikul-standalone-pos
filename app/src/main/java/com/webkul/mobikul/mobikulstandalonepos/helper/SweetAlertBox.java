package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.mobikulstandalonepos.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetAlertBox {

    public static SweetAlertBox mInstance;
    public static SweetAlertDialog sweetAlertDialog;

    public void dissmissSweetAlert() {
        if (sweetAlertDialog != null) {
            sweetAlertDialog.dismissWithAnimation();
        }
    }

    public void showProgressDialog(Context context, String title, String message) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        if (title.isEmpty())
            sweetAlertDialog.setTitleText(context.getResources().getString(R.string.loading));
        else
            sweetAlertDialog.setTitleText(title);
        if (message.isEmpty())
            sweetAlertDialog.setContentText(context.getResources().getString(R.string.wait_for_a_moment));
        else
            sweetAlertDialog.setContentText(message);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public void showProgressDialog(Context context) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.loading));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.wait_for_a_moment));
        sweetAlertDialog.setCancelable(false);
        try {
            sweetAlertDialog.show();
        } catch (Exception e) {
            sweetAlertDialog.dismiss();
        }
    }

    public void showSuccessPopUp(final Context context, String title, String message) {
        sweetAlertDialog = new SweetAlertDialog(context);
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
    }

    public void showWarningPopUp(final Context context, String title, String message, final String id) {
        sweetAlertDialog = new SweetAlertDialog(context);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.warning));
        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }

    public void showWarningPopUpPaymentMethod(Context context, String title, String message) {
        sweetAlertDialog = new SweetAlertDialog(context);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.warning));
        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }

    public void showErrorPopUp(Context context, String title, String message) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .show();
    }

    public void showAreYouSurePopUp(Context context, String title, String message) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(title)
                .setContentText(message)
                .setCancelText(context.getString(R.string.no))
                .setConfirmText(context.getString(R.string.yes))
                .show();
    }


    public static SweetAlertBox getInstance() {
        if (mInstance == null) {
            mInstance = new SweetAlertBox();
        }
        return mInstance;
    }

    public SweetAlertDialog getSweetAlertDialog() {
        return sweetAlertDialog;
    }
}