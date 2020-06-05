package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference_main {

    private Context context;
    private static SharedPreference_main sharedPreference_main;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreference;

    public SharedPreference_main(Context context) {
        this.context = context;
        sharedPreference = context.getSharedPreferences("PREF_READ", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPreference_main getInstance(Context context) {
        if (sharedPreference_main == null) {
            sharedPreference_main = new SharedPreference_main(context);
        }
        return sharedPreference_main;
    }

    public String getcamera()
    {
        return sharedPreference.getString("cam","");
    }
    public void setcamera(String came)
    {
        editor.putString("cam",came);
        editor.commit();
    }
}