package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.connections.VersionChecker;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.DB_NAME;
import static com.webkul.mobikul.mobikulstandalonepos.helper.Helper.DB_PATH;
import static com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager.unzip;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";
    private String latestVersion = "1.0";
    private String currentVersion = "1.0";
    private boolean isInternetAvailable;
    private SweetAlertDialog sweetAlert;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean isFetched = false;
    //The Android's default system path of your application database.

    public void initializeFirebase() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [END get_remote_config_instance]

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        // README for more information.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]
    }

    void fetch() {
        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(SplashScreenActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();
                            isFetched = true;
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
//                            fetch();
//                            Toast.makeText(SplashScreenActivity.this, "Fetch Failed",
//                                    Toast.LENGTH_SHORT).show();
                        }
                        displayWelcomeMessage();
                    }
                });
    }

    private void displayWelcomeMessage() {
        latestVersion = FirebaseRemoteConfig.getInstance().getString(
                "android_latest_version_name");
        Log.d(TAG, "onCreate: " + latestVersion);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        flow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initializeFirebase();
        checkDataBase();
        //            latestVersion = new VersionChecker().execute().get();
    }

    private boolean checkDataBase() {
        Log.d(TAG, "checkDataBase: Enter");
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                    SQLiteDatabase.OPEN_READWRITE);
            checkDB.close();
            Log.d(TAG, "checkDataBase: loaded");
        } catch (SQLiteException e) {
            Log.d(TAG, "checkDataBase: SQLiteException---" + e);
            e.printStackTrace();
            Helper.setDefaultDataBase(this);
            AppSharedPref.setSignedUp(this, true);
        } catch (Exception e) {
            Log.d(TAG, "checkDataBase: Exception " + e);
            e.printStackTrace();
            Helper.setDefaultDataBase(this);
            AppSharedPref.setSignedUp(this, true);
        }
        return checkDB != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch();
    }

    void flow() {
        isOnline();
        if (isInternetAvailable) {
            if (Float.parseFloat(latestVersion) > Float.parseFloat(currentVersion)) {
                if (sweetAlert != null) {
                    sweetAlert.dismissWithAnimation();
                }
                sweetAlert = new SweetAlertDialog(SplashScreenActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlert.setTitleText(getString(R.string.warning))
                        .setContentText(getResources().getString(R.string.new_version_available))
                        .setConfirmText(getResources().getString(R.string.update))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en"));
                                startActivity(i);
                                AppSharedPref.deleteSignUpdata(SplashScreenActivity.this);
                                isFetched = false;
                            }
                        })
                        .setCancelText(getResources().getString(R.string.later))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                connection(500);
                            }
                        })
                        .show();
                sweetAlert.setCancelable(false);
            } else
                connection(2000);
        } else {
            connection(2000);
        }
    }

    void connection(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (AppSharedPref.isShowWalkThrough(SplashScreenActivity.this, false)) {
//                    Intent i = new Intent(SplashScreenActivity.this, WalkthroughActivity.class);
//                    startActivity(i);
//                    SplashScreenActivity.this.finish();
//                } else
                Log.d(TAG, "run: " + AppSharedPref.isLoggedIn(SplashScreenActivity.this, false));
                if (AppSharedPref.isLoggedIn(SplashScreenActivity.this, false)) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    SplashScreenActivity.this.finish();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, SignUpSignInActivity.class);
                    startActivity(i);
                    SplashScreenActivity.this.finish();
                }
            }
        }, time);
    }

    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        return Integer.signum(vals1.length - vals2.length);
    }

    public void isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        isInternetAvailable = !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }
}