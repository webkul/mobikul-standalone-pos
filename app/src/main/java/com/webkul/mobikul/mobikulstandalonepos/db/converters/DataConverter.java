package com.webkul.mobikul.mobikulstandalonepos.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aman.gupta on 10/1/18. @Webkul Software Private limited
 */
public class DataConverter implements Serializable{

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
}
