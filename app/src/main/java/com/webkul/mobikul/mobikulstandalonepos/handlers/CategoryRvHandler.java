package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ItemsCategoryBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddCategoryFragment;

/**
 * Created by aman.gupta on 9/1/18.
 */

public class CategoryRvHandler {

    private final Context context;
    private final ItemsCategoryBinding binding;

    public CategoryRvHandler(Context context, ItemsCategoryBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onClickCategory(Category category) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        Fragment fragment = new AddCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        bundle.putBoolean("edit", true);
        fragment.setArguments(bundle);
        Log.d("name", fragment.getClass().getSimpleName() + "");
//        fragmentTransaction.add(((ActivityCategoryBinding) binding).categoryFl.getId(), fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }
}
