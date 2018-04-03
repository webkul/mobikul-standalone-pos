package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mynameismidori.currencypicker.CurrencyListAdapter;
import com.mynameismidori.currencypicker.CurrencyPreference;
import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CurrencyAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCurrencyBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;
import com.webkul.mobikul.mobikulstandalonepos.fragment.CurrencyPreferenceFragment;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CurrencyHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.connections.ApiUtils;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.model.OpenExchangeRate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.webkul.mobikul.mobikulstandalonepos.constants.OpenExchangeRatesConstants.APP_ID;
import static com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref.APP_PREF;

public class CurrencyActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityCurrencyBinding binding;
    SharedPreferences preferences;
    private String TAG = "CurrencyActivity";
    private CurrencyPreference currencyPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        AppSharedPref.getSharedPreference(this, APP_PREF).registerOnSharedPreferenceChangeListener(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("currency_config")) {
            this.getFragmentManager().beginTransaction().add(R.id.currency_fl, new CurrencyPreferenceFragment()).addToBackStack("CurrencyPreferenceFragment").commit();
        } else {
            SweetAlertBox.getInstance().showProgressDialog(this);
            if (AppSharedPref.getCurrencyRate(this, "").equalsIgnoreCase("")) {
                ApiUtils.getAPIService().getRates(APP_ID, "USD").enqueue(new Callback<OpenExchangeRate>() {
                    @Override
                    public void onResponse(Call<OpenExchangeRate> call, Response<OpenExchangeRate> response) {
                        AppSharedPref.setCurrencyRate(CurrencyActivity.this, new Gson().toJson(response.body()) + "");
                        setSelectedCurrencies(AppSharedPref.getSelectedCurrency(CurrencyActivity.this));
                        SweetAlertBox.getInstance().dissmissSweetAlert();
                    }

                    @Override
                    public void onFailure(Call<OpenExchangeRate> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                setSelectedCurrencies(AppSharedPref.getSelectedCurrency(CurrencyActivity.this));
                SweetAlertBox.getInstance().dissmissSweetAlert();
            }
            binding.setHandler(new CurrencyHandler(this));
        }
    }

    public void setSelectedCurrencies(String selectedCurrency) {
        Set<String> selectedCurrencies = preferences.getStringSet("selectedCurrencies", new HashSet<String>());

        CurrencyAdapter currencyAdapter = new CurrencyAdapter(this, getSelectedCurrencies(selectedCurrencies, selectedCurrency));
        binding.currencyRv.setAdapter(currencyAdapter);
    }

    List<Currency> getSelectedCurrencies(Set<String> selectedCurrencies, String selectedCurrency) {
        List<Currency> selectedCurrencyList = new ArrayList<>();
        try {
            JSONObject rateValues = new JSONObject(AppSharedPref.getCurrencyRate(this, ""));
            if (selectedCurrencies.size() == 0) {
                String code = "USD";
                ExtendedCurrency extendedCurrency = ExtendedCurrency.getCurrencyByISO(code);
                Currency currency = new Currency();
                currency.setCode(code);
                currency.setName(extendedCurrency.getName());
                currency.setFlag(extendedCurrency.getFlag());
                currency.setSymbol(extendedCurrency.getSymbol());
                JSONObject rates = rateValues.getJSONObject("rates");
                currency.setRate(Float.parseFloat(rates.getString(code)));
                currency.setFormatedRate(Helper.currencyFormater(Double.parseDouble(rates.getString(code)), code));
                if (code.equalsIgnoreCase(selectedCurrency))
                    currency.setSelected(true);
                else
                    currency.setSelected(false);
                selectedCurrencyList.add(currency);
            } else
                for (String code : selectedCurrencies) {
                    ExtendedCurrency extendedCurrency = ExtendedCurrency.getCurrencyByISO(code);
                    Currency currency = new Currency();
                    currency.setCode(code);
                    currency.setName(extendedCurrency.getName());
                    currency.setFlag(extendedCurrency.getFlag());
                    currency.setSymbol(extendedCurrency.getSymbol());
                    JSONObject rates = rateValues.getJSONObject("rates");
                    currency.setRate(Float.parseFloat(rates.getString(code)));
                    currency.setFormatedRate(Helper.currencyFormater(Double.parseDouble(rates.getString(code)), code));
                    if (code.equalsIgnoreCase(selectedCurrency))
                        currency.setSelected(true);
                    else
                        currency.setSelected(false);
                    selectedCurrencyList.add(currency);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return selectedCurrencyList;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged: " + s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        menu.findItem(R.id.menu_item_scan_barcode).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
