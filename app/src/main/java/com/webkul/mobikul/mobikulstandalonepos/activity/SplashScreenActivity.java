package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 3000);

    }
}
