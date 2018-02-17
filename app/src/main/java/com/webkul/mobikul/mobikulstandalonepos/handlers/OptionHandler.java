package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.OptionsActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddOptionFragment;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 15/2/18. @Webkul Software Private limited
 */

public class OptionHandler {

    private Context context;

    public OptionHandler(Context context) {
        this.context = context;
    }

    public void addOption(Options options) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment;
        fragment = ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddOptionFragment.class.getSimpleName());
        if (fragment == null)
            fragment = new AddOptionFragment();
        if (!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("options", options);
            bundle.putBoolean("IS_EDIT", false);
            fragment.setArguments(bundle);
            fragmentTransaction.add(((OptionsActivity) context).binding.optionFl.getId(), fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    public void addOptionValue() {
        OptionValues optionValues = new OptionValues();
        AddOptionFragment fragment = (AddOptionFragment) ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddOptionFragment.class.getSimpleName());
        fragment.optionValues.add(optionValues);
        fragment.binding.optionValuesRv.getAdapter().notifyDataSetChanged();
    }

    public void removeOption(OptionValues data) {
        AddOptionFragment fragment = (AddOptionFragment) ((BaseActivity) context).mSupportFragmentManager.findFragmentByTag(AddOptionFragment.class.getSimpleName());
        fragment.optionValues.remove(data);
        fragment.binding.optionValuesRv.getAdapter().notifyDataSetChanged();
    }

    public void saveOption(Options options, boolean isEdit) {
        Log.d(TAG, "saveOption: " + new Gson().toJson(options));
        DataBaseController.getInstanse().addOption(context, options, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String successMsg) {
                ToastHelper.showToast(context, successMsg, Toast.LENGTH_LONG);
                ((BaseActivity) context).mSupportFragmentManager.popBackStackImmediate();
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                ToastHelper.showToast(context, errorMsg, Toast.LENGTH_LONG);
            }
        });

    }

}
