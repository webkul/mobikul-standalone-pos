package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MyAccountInfo;
import com.webkul.mobikul.mobikulstandalonepos.activity.SignUpSignInActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Administrator;
import com.webkul.mobikul.mobikulstandalonepos.fragment.SignUpFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import static com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref.USER_PREF;

/**
 * Created by aman.gupta on 3/2/18. @Webkul Software Private limited
 */

public class MyAccountInfoHandler {

    private Context context;

    public MyAccountInfoHandler(Context context) {
        this.context = context;
    }

    public void onClickSubmit(Administrator administrator) {
        if (isFormValidated(administrator)) {
            DataBaseController.getInstanse().updateAdmin(context, administrator, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                    ((MyAccountInfo) context).finish();
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
                }
            });
        }

    }

    public boolean isFormValidated(Administrator administrator) {
        administrator.setDisplayError(true);
        MyAccountInfo myAccountInfo = ((MyAccountInfo) context);
        if (myAccountInfo != null) {
            if (!administrator.getFirstNameError().isEmpty()) {
                myAccountInfo.binding.firstName.requestFocus();
                Helper.shake(context, myAccountInfo.binding.customerFirstNameTnl);
                return false;
            }
            if (!administrator.getLastNameError().isEmpty()) {
                myAccountInfo.binding.lastName.requestFocus();
                Helper.shake(context, myAccountInfo.binding.customerLastNameTnl);
                return false;
            }
            administrator.setDisplayError(false);
            return true;
        }
        return false;
    }

//    public void signOut() {
//        AppSharedPref.getSharedPreferenceEditor(context, USER_PREF).clear().apply();
//        Intent i = new Intent(context, SignUpSignInActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        context.startActivity(i);
//        ((MyAccountInfo) context).finish();
//    }

}
