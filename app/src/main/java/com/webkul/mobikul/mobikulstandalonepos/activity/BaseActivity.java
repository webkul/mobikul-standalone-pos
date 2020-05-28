package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.db.AppDatabase;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.webkul.mobikul.mobikulstandalonepos.db.AppDatabase.destroyDbInstance;
import static com.webkul.mobikul.mobikulstandalonepos.db.AppDatabase.getAppDatabase;

/**
 * Created by aman.gupta on 4/1/17. @Webkul Software Private limited
 */

public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    public static final String TAG = "BaseActivity";
    public static final int IMAGE_REQUEST = 125;
    public static final int READ_STORAGE_PERMISSION_REQUEST = 126;
    public SweetAlertDialog mSweetAlertDialog;
    public FragmentManager mSupportFragmentManager;
    public static AppDatabase db;
    public static Context context;
    public MutableLiveData<Boolean> storagePermissionResult = new MutableLiveData<>();
    public MutableLiveData<String> galleryImageRequestResult = new MutableLiveData<>();

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSupportFragmentManager = getSupportFragmentManager();
        getDb();
        context = this;
        showBackButton(true);
    }

    public AppDatabase getDb() {
        //Changed code to avoid creating new Db instance for each query
        if (db == null || !db.isOpen()) {
            db = getAppDatabase(this);
        }
        return db;
    }

    public static Context getContext() {
        return context;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    public void setActionbarTitle(@Nullable String title) {
        if (title == null) {
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        mItemBag = menu.findItem(R.id.menu_item_bag);
//        LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
////        Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
//        return true;
//    }


    @Override
    protected void onStop() {
        super.onStop();
        destroyDbInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /*
    * This method can be used from anywhere in the whole app to avoid duplicity of code.
    * One can observe storagePermissionResult for user response after requesting permission.
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            storagePermissionResult.setValue(((grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)));
        }
    }

    /*
     * This method handles the boiler code for getting image data and converting into uri.
     * One can observe galleryImageRequestResult for uri of requested image.
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    //Conversion of content uri to internal file uri for avoiding security exception when loading image in cart
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    galleryImageRequestResult.setValue(Helper.saveToInternalStorage(BaseActivity.this, bitmap, "" + System.currentTimeMillis()).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    galleryImageRequestResult.setValue("");
                }
            } else {
                Log.d("Gallery Intent", "Cancelled");
                //Setting it empty string to remove observer without any action
                galleryImageRequestResult.setValue("");
            }
        }
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mItemBag != null) {
//            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
////            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
//        }
//    }


//    public void updateCartBadge(int cartCount) {
//        AppSharedPref.setCartCount(this, cartCount);
//        if (mItemBag != null) {
//            LayerDrawable icon = (LayerDrawable) mItemBag.getIcon();
//            Helper.setBadgeCount(this, icon, AppSharedPref.getCartCount(this, 0));
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mCompositeDisposable.clear();
//        RetrofitClient.getDispatcher().cancelAll();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mSqLiteDatabase.close();
//        AlertDialogHelper.dismiss(this);
//    }
}