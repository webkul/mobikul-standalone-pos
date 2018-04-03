package com.webkul.mobikul.mobikulstandalonepos.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.mynameismidori.currencypicker.CurrencyPreference;
import com.mynameismidori.currencypicker.MultiCurrencyPreference;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.CurrencyActivity;

import java.util.Currency;
import java.util.HashSet;

public class CurrencyPreferenceFragment extends PreferenceFragment implements CurrencyPickerListener, SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;
    private CurrencyPicker mCurrencyPicker;
    private CurrencyPreference currencyPreference;
    private static final String TAG = "CurrencyPreference";
    HashSet<String> currencyHash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        currencyPreference = (CurrencyPreference) findPreference("selectedCurrency");
        currencyHash = new HashSet<>();
        currencyHash.add("USD");

        //remove default currency method...
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removePreference((ListPreference) findPreference("selectedCurrency"));

        currencyPreference.setCurrenciesList(preferences.getStringSet("selectedCurrencies", currencyHash));
        mCurrencyPicker = CurrencyPicker.newInstance("Select Currency");
        mCurrencyPicker.setListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.white));
        view.setPadding(0, 20, 0, 0);
    }

    @Override
    public void onResume() {
        preferences.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "onSharedPrefeenceChanged: " + currencyPreference);
        super.onResume();
    }

    @Override
    public void onPause() {
        preferences.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "onSharedPreferenceChanged: " + currencyPreference);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("selectedCurrencies")) {
            currencyPreference.setCurrenciesList(preferences.getStringSet("selectedCurrencies", currencyHash));
        }
    }

    @Override
    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
        currencyPreference.setValue(code);
        Log.d(TAG, "onSelectCurrency: " + currencyPreference);
    }
}