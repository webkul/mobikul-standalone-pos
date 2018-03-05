package com.webkul.mobikul.mobikulstandalonepos.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aman.gupta on 10/1/18. @Webkul Software Private limited
 */
public class DataConverter implements Serializable {

    @TypeConverter
    public String fromProductCategoriesList(List<ProductCategoryModel> productCategories) {
        if (productCategories == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductCategoryModel>>() {
        }.getType();
        String json = gson.toJson(productCategories, type);
        return json;
    }

    @TypeConverter
    public List<ProductCategoryModel> toProductCategoriesList(String productCategoriesString) {
        if (productCategoriesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductCategoryModel>>() {
        }.getType();
        List<ProductCategoryModel> productCategoriesList = gson.fromJson(productCategoriesString, type);
        return productCategoriesList;
    }

    @TypeConverter
    public String fromOptionValuesList(List<OptionValues> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OptionValues>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter
    public List<OptionValues> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OptionValues>>() {
        }.getType();
        List<OptionValues> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter
    public String fromOptionList(List<Options> options) {
        if (options == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Options>>() {
        }.getType();
        String json = gson.toJson(options, type);
        return json;
    }

    @TypeConverter
    public List<Options> toOptionsList(String optionString) {
        if (optionString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Options>>() {
        }.getType();
        List<Options> productOptionList = gson.fromJson(optionString, type);
        return productOptionList;
    }

    @TypeConverter
    public String fromCartModelToString(CartModel cartData) {
        if (cartData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        String json = gson.toJson(cartData, type);
        return json;
    }

    @TypeConverter
    public CartModel fromStringToCartModel(String cartDataString) {
        if (cartDataString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        CartModel cartData = gson.fromJson(cartDataString, type);
        return cartData;
    }

    @TypeConverter
    public String fromCashModelToString(CashModel cashData) {
        if (cashData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CashModel>() {
        }.getType();
        String json = gson.toJson(cashData, type);
        return json;
    }

    @TypeConverter
    public Tax fromStringToTaxModel(String taxDataString) {
        if (taxDataString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Tax>() {
        }.getType();
        Tax taxData = gson.fromJson(taxDataString, type);
        return taxData;
    }

    @TypeConverter
    public String fromTaxModelToString(Tax taxData) {
        if (taxData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Tax>() {
        }.getType();
        String json = gson.toJson(taxData, type);
        return json;
    }

    @TypeConverter
    public CashModel fromStringToCashModel(String cashDataString) {
        if (cashDataString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CashModel>() {
        }.getType();
        CashModel cashData = gson.fromJson(cashDataString, type);
        return cashData;
    }

    @TypeConverter
    public String fromCashDrawerItemToString(List<CashDrawerItems> cashDrawerItems) {
        if (cashDrawerItems == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CashDrawerItems>>() {
        }.getType();
        String json = gson.toJson(cashDrawerItems, type);
        return json;
    }

    @TypeConverter
    public List<CashDrawerItems> fromStringToCashDrawerItem(String cashDrawerItemsString) {
        if (cashDrawerItemsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CashDrawerItems>>() {
        }.getType();
        List<CashDrawerItems> cashDrawerItems = gson.fromJson(cashDrawerItemsString, type);
        return cashDrawerItems;
    }
}
