package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.BR;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by aman.gupta on 15/2/18. @Webkul Software Private limited
 */
@Entity
public class OptionValues extends BaseObservable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int optionValueId;
    @ColumnInfo(name = "option_id")
    private int optionId;
    @ColumnInfo(name = "option_value_name")
    private String optionValueName;
    @ColumnInfo(name = "option_value_price")
    private String optionValuePrice;
//    @ColumnInfo(name = "formatted_option_value_price")
//    private String formattedOptionValuePrice;
    @ColumnInfo(name = "option_value_sort_order")
    private int sortOrder;
    @Ignore
    private boolean selected;
    @Ignore
    private boolean isAddToCart;

    @Bindable
    public String getOptionValueName() {
        if (optionValueName == null)
            return "";
        Log.d(TAG, "getOptionValueName: " + optionValueName);
        return optionValueName;
    }

    public void setOptionValueName(String optionValueName) {
        this.optionValueName = optionValueName;
        notifyPropertyChanged(BR.optionValueName);
    }


    public String getOptionValuePrice() {
        if (optionValuePrice == null)
            return "";
        return optionValuePrice;
    }

    public void setOptionValuePrice(String optionValuePrice) {
        this.optionValuePrice = optionValuePrice;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(int optionValueId) {
        this.optionValueId = optionValueId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
        notifyPropertyChanged(BR.selected);
    }

    public boolean isAddToCart() {
        return isAddToCart;
    }

    public void setAddToCart(boolean addToCart) {
        isAddToCart = addToCart;
    }

//    public String getFormattedOptionValuePrice() {
//        return formattedOptionValuePrice;
//    }
//
//    public void setFormattedOptionValuePrice(String formattedOptionValuePrice) {
//        this.formattedOptionValuePrice = formattedOptionValuePrice;
//    }
}
