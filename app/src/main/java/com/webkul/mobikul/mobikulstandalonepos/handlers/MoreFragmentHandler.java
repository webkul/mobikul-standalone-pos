package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.CategoryActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CustomerActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.SignUpSignInActivity;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.model.MoreData;

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
            default:
                ToastHelper.showToast(context, "THIS OPTION IS UNDER DEVELOPMENT MODE!!", Toast.LENGTH_LONG);
        }
    }

    public void signOut() {
        AppSharedPref.getSharedPreferenceEditor(context, USER_PREF).clear().apply();
        Intent i = new Intent(context, SignUpSignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }
}
