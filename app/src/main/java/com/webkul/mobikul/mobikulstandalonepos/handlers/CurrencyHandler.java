package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webkul.mobikul.mobikulstandalonepos.activity.CurrencyActivity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aman.gupta on 15/3/18. @Webkul Software Private limited
 */

public class CurrencyHandler {

    private Context context;

    public CurrencyHandler(Context context) {
        this.context = context;
    }

    public void onSelectCurrency(Currency data) {
        AppSharedPref.setTempSelectedCurrency(context, new Gson().toJson(data));
        ((CurrencyActivity) context).setSelectedCurrencies(data.getCode());
    }

    public void saveSelectedCurrency() {
        if (!AppSharedPref.getTempSelectedCurrency(context).equalsIgnoreCase("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Currency>() {
            }.getType();
            Currency currency = gson.fromJson(AppSharedPref.getTempSelectedCurrency(context), type);
            AppSharedPref.setSelectedCurrency(context, currency.getCode());
            AppSharedPref.setSelectedCurrencyRate(context, currency.getRate());
            AppSharedPref.setSelectedCurrencySymbol(context, currency.getSymbol());
            AppSharedPref.clearTempSelectedCurrency(context);
        }
        ((CurrencyActivity) context).finish();
    }
}
