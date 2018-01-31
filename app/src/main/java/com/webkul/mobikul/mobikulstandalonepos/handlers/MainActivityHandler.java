package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CategoryActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HomeFragment;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

/**
 * Created by aman.gupta on 16/1/18. @Webkul Software Private limited
 */

public class MainActivityHandler {

    private Context context;

    public MainActivityHandler(Context context) {
        this.context = context;
    }

    public void onClickCategory(Category category) {
        ((MainActivity) context).mMainBinding.drawerLayout.closeDrawer(Gravity.START);
        HomeFragment homeFragment = (HomeFragment) ((MainActivity) context).mSupportFragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        if (!(((MainActivity) context).mSupportFragmentManager.findFragmentById(R.id.main_frame) instanceof HomeFragment)) {
            ((MainActivity) context).mMainBinding.bottomNavView.setSelectedItemId(R.id.bottom_nav_item_home);
            ((MainActivity) context).mSupportFragmentManager.popBackStack();
        }
        homeFragment.showCategorySelectedProducts(category.getCId() + "");
    }
}
