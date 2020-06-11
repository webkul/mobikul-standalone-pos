package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Aman Gupta on 29/12/17. @Webkul Software Private limited
 */

public class AppSharedPref {

    /* Shared Preferences keys */
    @SuppressWarnings("WeakerAccess")
    public static final String USER_PREF = "USER_PREF";
    public static final String APP_PREF = "APP_PREF";
    @SuppressWarnings("WeakerAccess")
    private static final String TAG = "AppSharedPref";
    @SuppressWarnings("unused")
    private static final String CONFIGURATION_PREF = "CONFIGURATION_PREF";

    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_CART_COUNT = "CART_COUNT";
    private static final String KEY_CURRENCIES_RATES = "KEY_CURRENCIES_RATES";
    private static final String KEY_SELECTED_CURRENCY = "KEY_SELECTED_CURRENCY";
    private static final String KEY_SELECTED_CURRENCY_SYMBOL = "KEY_SELECTED_CURRENCY_SYMBOL";
    private static final String KEY_SELECTED_CURRENCY_RATE = "KEY_SELECTED_CURRENCY_RATE";
    private static final String KEY_TEMP_SELECTED_CURRENCY = "KEY_TEMP_SELECTED_CURRENCY";
    private static final String IS_SIGNED_UP = "IS_SIGNED_UP";
    private static final String IS_RETURN_CART = "IS_RETURN_CART";
    private static final String RETURN_ORDER_ID = "RETURN_ORDER_ID";
    private static final String IS_LOGGED_IN = "IS_LOGGED_in";
    private static final String IS_SHOW_WALKTHROUGH = "IS_SHOW_WALKTHROUGH";
    private static final String KEY_CART_DATA = "CART_DATA";
    private static final String KEY_TIME = "time";
    private static final String KEY_REMINDER = "REMINDER_MSG";
    private static final String KEY_CASH = "IS_CASH";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_OPTION_VALUE_IDS = "OPTION_VALUE_IDS";

    /*SHARED PREF AND EDITOR*/
    @SuppressWarnings("WeakerAccess")
    public static SharedPreferences getSharedPreference(Context context, String preferenceFile) {
        return context.getSharedPreferences(preferenceFile, MODE_PRIVATE);
    }

    @SuppressWarnings("WeakerAccess")
    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context, String preferenceFile) {
        return context.getSharedPreferences(preferenceFile, MODE_PRIVATE).edit();
    }

    /*USER NAME*/
    public static String getUserName(Context context) {
        return getSharedPreference(context, USER_PREF).getString(KEY_USER_NAME, "");
    }

    public static void setUserName(Context context, String userName) {
        getSharedPreferenceEditor(context, USER_PREF).putString(KEY_USER_NAME, userName).apply();
    }

    /*User Email*/
    public static String getUserEmail(Context context) {
        return getSharedPreference(context, USER_PREF).getString(KEY_USER_EMAIL, "");
    }

    public static void setUserEmail(Context context, String email) {
        getSharedPreferenceEditor(context, USER_PREF).putString(KEY_USER_EMAIL, email).apply();
    }

    /*User Id*/
    public static int getUserId(Context context) {
        return getSharedPreference(context, USER_PREF).getInt(KEY_USER_ID, 0);
    }

    public static void setUserId(Context context, int userId) {
        getSharedPreferenceEditor(context, USER_PREF).putInt(KEY_USER_ID, userId).apply();
    }

    /*CART COUNT -- OF A USER*/

    public static int getCartCount(Context context, int defaultValue) {
        return getSharedPreference(context, USER_PREF).getInt(KEY_CART_COUNT, defaultValue);
    }

    public static void setCartCount(Context context, int cartCount) {
        getSharedPreferenceEditor(context, USER_PREF).putInt(KEY_CART_COUNT, cartCount).apply();
    }

    public static boolean isSignedUp(Context context, boolean defaultValue) {
        return getSharedPreference(context, APP_PREF).getBoolean(IS_SIGNED_UP, defaultValue);
    }

    public static void setSignedUp(Context context, boolean isSignUp) {
        getSharedPreferenceEditor(context, APP_PREF).putBoolean(IS_SIGNED_UP, isSignUp).apply();
    }

    public static boolean isLoggedIn(Context context, boolean defaultValue) {
        return getSharedPreference(context, USER_PREF).getBoolean(IS_LOGGED_IN, defaultValue);
    }

    public static void setLoggedIn(Context context, boolean isLogIn) {
        getSharedPreferenceEditor(context, USER_PREF).putBoolean(IS_LOGGED_IN, isLogIn).apply();
    }

    public static boolean isShowWalkThrough(Context context, boolean defaultValue) {
        return getSharedPreference(context, APP_PREF).getBoolean(IS_SHOW_WALKTHROUGH, defaultValue);
    }

    public static void setShowWalkThrough(Context context, boolean isShowWalkThrough) {
        getSharedPreferenceEditor(context, APP_PREF).putBoolean(IS_SHOW_WALKTHROUGH, isShowWalkThrough).apply();
    }

    public static String getCartData(Context context) {
        return getSharedPreference(context, APP_PREF).getString(KEY_CART_DATA + getUserId(context), "");
    }

    public static void setCartData(Context context, String cartData) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_CART_DATA + getUserId(context), cartData).apply();
    }

    public static void deleteCartData(Context context) {
        getSharedPreferenceEditor(context, APP_PREF).remove(KEY_CART_DATA + getUserId(context)).apply();
    }

    public static void deleteSignUpdata(Context context) {
        getSharedPreferenceEditor(context, APP_PREF).remove(IS_SIGNED_UP).apply();
    }

    public static void setTime(Context context, long time) {
        getSharedPreferenceEditor(context, APP_PREF).putLong(KEY_TIME, time).apply();
    }

    public static long getTime(Context context, long time) {
        return getSharedPreference(context, APP_PREF).getLong(KEY_TIME, time);
    }

    public static void setReminderMsgShown(Context context, boolean isReminder) {
        getSharedPreferenceEditor(context, APP_PREF).putBoolean(KEY_REMINDER, isReminder).apply();
    }

    public static boolean isReminderMsgShown(Context context, boolean isReminder) {
        return getSharedPreference(context, APP_PREF).getBoolean(KEY_REMINDER, isReminder);
    }

    public static void setCashEnabled(Context context, boolean isCashEnabled) {
        getSharedPreferenceEditor(context, APP_PREF).putBoolean(KEY_CASH, isCashEnabled).apply();
    }

    public static boolean isCashEnabled(Context context, boolean isCashEnabled) {
        return getSharedPreference(context, APP_PREF).getBoolean(KEY_CASH, isCashEnabled);
    }

    public static String getDate(Context context) {
        return getSharedPreference(context, APP_PREF).getString(KEY_DATE, "");
    }

    public static void setDate(Context context, String date) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_DATE, date).apply();
    }

    public static void removeAllPref(Context context) {
        getSharedPreferenceEditor(context, APP_PREF).clear();
        getSharedPreferenceEditor(context, USER_PREF).clear();
    }


    public static String getCurrencyRate(Context context, String defaultValue) {
        return getSharedPreference(context, APP_PREF).getString(KEY_CURRENCIES_RATES, defaultValue);
    }

    public static void setCurrencyRate(Context context, String currenciesRate) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_CURRENCIES_RATES, currenciesRate).apply();
    }

    public static float getSelectedCurrencyRate(Context context) {
        return getSharedPreference(context, APP_PREF).getFloat(KEY_SELECTED_CURRENCY_RATE, 1.00f);
    }

    public static void setSelectedCurrencyRate(Context context, float currencyRate) {
        getSharedPreferenceEditor(context, APP_PREF).putFloat(KEY_SELECTED_CURRENCY_RATE, currencyRate).apply();
    }

    public static String getSelectedCurrency(Context context) {
        return getSharedPreference(context, APP_PREF).getString(KEY_SELECTED_CURRENCY, "USD");
    }

    public static void setSelectedCurrencySymbol(Context context, String currency) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_SELECTED_CURRENCY_SYMBOL, currency).apply();
    }

    public static String getSelectedCurrencySymbol(Context context) {
        return getSharedPreference(context, APP_PREF).getString(KEY_SELECTED_CURRENCY_SYMBOL, "$");
    }

    public static void setSelectedCurrency(Context context, String currency) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_SELECTED_CURRENCY, currency).apply();
    }

    public static String getTempSelectedCurrency(Context context) {
        return getSharedPreference(context, APP_PREF).getString(KEY_TEMP_SELECTED_CURRENCY, "");
    }

    public static void setTempSelectedCurrency(Context context, String currency) {
        getSharedPreferenceEditor(context, APP_PREF).putString(KEY_TEMP_SELECTED_CURRENCY, currency).apply();
    }

    public static void clearTempSelectedCurrency(Context context) {
        getSharedPreferenceEditor(context, APP_PREF).remove(KEY_TEMP_SELECTED_CURRENCY).apply();
    }

    public static boolean isReturnCart(Context context) {
        return getSharedPreference(context, APP_PREF).getBoolean(IS_RETURN_CART, false);
    }

    public static void setReturnCart(Context context, Boolean isReturnCart) {
        getSharedPreferenceEditor(context, APP_PREF).putBoolean(IS_RETURN_CART, isReturnCart).apply();
    }

    public static String getReturnOrderId(Context context) {
        return getSharedPreference(context, APP_PREF).getString(RETURN_ORDER_ID, "0");
    }

    public static void setReturnOrderId(Context context, String orderId) {
        getSharedPreferenceEditor(context, APP_PREF).putString(RETURN_ORDER_ID, orderId).apply();
    }
}
