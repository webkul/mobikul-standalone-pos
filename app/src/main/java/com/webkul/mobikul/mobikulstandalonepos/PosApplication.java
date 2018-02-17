package com.webkul.mobikul.mobikulstandalonepos;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by aman.gupta on 6/2/18. @Webkul Software Private limited
 */

public class PosApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
