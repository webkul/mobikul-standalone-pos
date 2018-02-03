package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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
import com.webkul.mobikul.mobikulstandalonepos.db.AppDatabase;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HoldFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.HomeFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.MoreFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.OrdersFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
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
    private long backPressedTime = 0;
    long networkTS = 0;
    private long storedTime;
    private LocationManager locMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initDrawerToggle();
        initBottomNavView();
        loadDrawerData();
        loadHomeFragment();
        getCurrentTime();
        storedTime = AppSharedPref.getTime(this, 0);
        Log.d(TAG, "onCreate: " + networkTS + "----" + storedTime + "---" + (networkTS - storedTime));
        if (storedTime == 0) {
            AppSharedPref.setTime(this, networkTS);
        } else {
            if ((networkTS - storedTime) >= (60 * 60 * 1000)) {
                destoryDbForDemoUser();
            }
        }
        categoriesWithFilteredById = new ArrayList<>();
    }

    private void getCurrentTime() {
        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            return;
        }
        networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void destoryDbForDemoUser() {
        DataBaseController.getInstanse().deleteAllTables(MainActivity.this);
        AppSharedPref.setTime(this, 0);
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
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) { //2 secs
                backPressedTime = t;
                ToastHelper.showToast(this, getResources().getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT);
            } else {
//                destoryDbForDemoUser();
                finish();
            }
        }
    }

    public void showLoader() {
        mMainBinding.loader.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        mMainBinding.loader.setVisibility(View.GONE);
    }
}