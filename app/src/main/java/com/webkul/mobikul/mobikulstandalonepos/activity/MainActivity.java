package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.DrawerAdapter;
import com.webkul.mobikul.mobikulstandalonepos.adapter.HomePageProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityMainBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HoldFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HomeFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.MoreFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.OrdersFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aman.gupta on 27/12/17. @Webkul Software Private limited
 */

public class MainActivity extends BaseActivity {

    public ActivityMainBinding mMainBinding;
    private ActionBarDrawerToggle mDrawerToggle;
    public List<Product> products;
    private List<Category> categories;
    private List<Category> categoriesWithFilteredById;
    private DrawerAdapter drawerAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        mMainBinding.cate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "asdad", Toast.LENGTH_SHORT).show();
//            }
//        });
        initDrawerToggle();
        initBottomNavView();
        loadDrawerData();
        loadHomeFragment();
        categoriesWithFilteredById = new ArrayList<>();
    }

    public void loadDrawerData() {
        categories = new ArrayList<>();
        setCategories();
    }

    public void setCategories() {
        DataBaseController.getInstanse().getIncludedCategoryForDrawer(this, new DataBaseCallBack() {
            @Override
            public void onSuccess(Object responseData, String msg) {
                if (!(categories.toString().equalsIgnoreCase(responseData.toString()))) {
                    if (categories.size() > 0)
                        categories.clear();
                    categories.addAll((List<Category>) responseData);
                    if (drawerAdapter == null) {
                        drawerAdapter = new DrawerAdapter(MainActivity.this, categories);
                        mMainBinding.categoryRvDrawer.setAdapter(drawerAdapter);
                    } else {
                        drawerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                Toast.makeText(MainActivity.this, errorMsg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCategories();
    }

    private void initDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mMainBinding.drawerLayout
                , R.string.drawer_open, R.string.drawer_close);
        mMainBinding.drawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void initBottomNavView() {
        mMainBinding.bottomNavView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_item_home:
                                loadHomeFragment();
                                break;
                            case R.id.bottom_nav_item_orders:
                                loadOrdersFragment();
                                break;
                            case R.id.bottom_nav_item_hold:
//                                loadHoldFragment();
                                break;
                            case R.id.bottom_nav_item_more:
                                loadMoreFragment();
                                break;
                        }
                        return true;
                    }
                });
        removeBottomNavShiftMode();
    }

    private void loadHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, HomeFragment.newInstance()
                , HomeFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void loadOrdersFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, OrdersFragment.newInstance()
                , OrdersFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void loadHoldFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, HoldFragment.newInstance()
                , HoldFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void loadMoreFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, MoreFragment.newInstance()
                , MoreFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @SuppressLint("RestrictedApi")
    public void removeBottomNavShiftMode() {
        try {
            BottomNavigationMenuView menuView =
                    (BottomNavigationMenuView) mMainBinding.bottomNavView.getChildAt(0);
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
//        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchItem.expandActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        final HomeFragment fragment = (HomeFragment) mSupportFragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
//        products = new ArrayList<>();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.length() > 0) {
//                    newText = "%" + newText + "%";
//                    DataBaseController.getInstanse().getSearchData(MainActivity.this, newText, new DataBaseCallBack() {
//                        @Override
//                        public void onSuccess(Object responseData, String successMsg) {
//                            if (!(products.toString().equalsIgnoreCase(responseData.toString()))) {
//                                if (products.size() > 0) {
//                                    products.clear();
//                                    fragment.productAdapter = null;
//                                }
//                                products.addAll((List<Product>) responseData);
//                                if (fragment.productAdapter == null) {
//                                    fragment.productAdapter = new HomePageProductAdapter(MainActivity.this, products);
//                                    fragment.binding.productRv.setAdapter(fragment.productAdapter);
//                                } else {
//                                    fragment.productAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(int errorCode, String errorMsg) {
//
//                        }
//                    });
//                    return true;
//                } else {
//                    fragment.productAdapter = new HomePageProductAdapter(MainActivity.this, fragment.products);
//                    fragment.binding.productRv.setAdapter(fragment.productAdapter);
//                }
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mMainBinding.drawerLayout != null &&
                mMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!(getSupportFragmentManager().findFragmentById(R.id.main_frame) instanceof HomeFragment)) {
            mMainBinding.bottomNavView.setSelectedItemId(R.id.bottom_nav_item_home);
        } else {
            super.onBackPressed();
        }
    }

    public void showLoader() {
        mMainBinding.loader.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        mMainBinding.loader.setVisibility(View.GONE);
    }
}