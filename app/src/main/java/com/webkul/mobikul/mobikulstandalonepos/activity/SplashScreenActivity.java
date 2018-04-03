package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.connections.VersionChecker;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;

import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashScreenActivity extends AppCompatActivity {

    private String latestVersion;
    private String currentVersion;
    private boolean isInternetAvailable;
    private SweetAlertDialog sweetAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            latestVersion = new VersionChecker().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            latestVersion = "1.0";
        } catch (ExecutionException e) {
            latestVersion = "1.0";
            e.printStackTrace();
        }
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                if (AppSharedPref.isShowWalkThrough(SplashScreenActivity.this, false)) {
                    Intent i = new Intent(SplashScreenActivity.this, WalkthroughActivity.class);
                    startActivity(i);
                    SplashScreenActivity.this.finish();
                } else if (AppSharedPref.isLoggedIn(SplashScreenActivity.this, false)) {
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
