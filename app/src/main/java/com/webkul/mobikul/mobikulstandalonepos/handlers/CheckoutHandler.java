package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.Checkout;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.PlaceOrderActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.CashFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;
import com.webkul.mobikul.mobikulstandalonepos.model.TotalModel;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 24/1/18. @Webkul Software Private limited
 */

public class CheckoutHandler {

    private Context context;
    private Vibrator vibrateObject;

    public CheckoutHandler(Context context) {
        this.context = context;
    }

    public void cashPayment(TotalModel totalModel) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = fragmentManager.findFragmentByTag(CashFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new CashFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("total", totalModel);
        fragment.setArguments(bundle);
        fragmentTransaction.add(((Checkout) context).checkoutBinding.frameLayout.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    public void orderPlaced(CashModel cashData, TotalModel totalData) {
        if (isValidated(cashData)) {
            Intent i = new Intent(context, PlaceOrderActivity.class);
            cashData.setFormattedCollectedCash(Helper.currencyFormater(Double.parseDouble(cashData.getCollectedCash()), context));
            cashData.setFormattedChangeDue(Helper.currencyFormater(Double.parseDouble(cashData.getChangeDue()), context));
            i.putExtra("cashData", cashData);
            i.putExtra("totalData", totalData);
            context.startActivity(i);
            ((Checkout) context).finish();
        }
    }

    public boolean isValidated(CashModel cashData) {
        cashData.setDisplayError(true);
        Fragment fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(CashFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            CashFragment cashFragment = ((CashFragment) fragment);
            if (!cashData.getCollectedCashError().isEmpty()) {
                cashFragment.cashBinding.cashCollected.requestFocus();
                vibrateObject = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 200 milliseconds
                vibrateObject.vibrate(300);
                cashFragment.cashBinding.cashCollectedTil.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
                return false;
            }
            cashData.setDisplayError(false);
            return true;
        }
        return false;
    }
}
