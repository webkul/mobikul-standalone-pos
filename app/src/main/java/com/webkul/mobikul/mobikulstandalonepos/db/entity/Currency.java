package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;

/**
 * Created by aman.gupta on 14/3/18. @Webkul Software Private limited
 */
@Entity(tableName = "Currency")
public class Currency extends BaseObservable {
    @PrimaryKey
    @ColumnInfo(name = "code")
    private String code;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "symbol")
    private String symbol;
    @ColumnInfo(name = "flag")
    private int flag = -1;
    @ColumnInfo(name = "rate")
    private float rate;
    @ColumnInfo(name = "formated_rate")
    private String formatedRate;
    @ColumnInfo(name = "selected")
    private boolean isSelected;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFormatedRate() {
        return formatedRate;
    }

    public void setFormatedRate(String formatedRate) {
        this.formatedRate = formatedRate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}