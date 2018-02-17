package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashDrawerItems;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman.gupta on 15/2/18. @Webkul Software Private limited
 */
@Entity
public class CashDrawerModel implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "date")
    @NotNull
    private String date;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "cash_drawer_items")
    private List<CashDrawerItems> cashDrawerItems;

    @ColumnInfo(name = "opening_balance")
    private String openingBalance;

    @ColumnInfo(name = "closing_balance")
    private String closingBalance;

    @ColumnInfo(name = "net_revenue")
    private String netRevenue;

    @ColumnInfo(name = "in_amount")
    private String inAmount;

    @ColumnInfo(name = "out_amount")
    private String outAmount;

    @ColumnInfo(name = "is_synced")
    private String isSynced;


    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(String netRevenue) {
        this.netRevenue = netRevenue;
    }

    public String getInAmount() {
        if (inAmount == null)
            return "0.00";
        return inAmount;
    }

    public void setInAmount(String inAmount) {
        this.inAmount = inAmount;
    }

    public String getOutAmount() {
        if (outAmount == null)
            return "0.00";
        return outAmount;
    }

    public void setOutAmount(String outAmount) {
        this.outAmount = outAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public List<CashDrawerItems> getCashDrawerItems() {
        if (cashDrawerItems == null)
            cashDrawerItems = new ArrayList<>();
        return cashDrawerItems;
    }

    public void setCashDrawerItems(List<CashDrawerItems> cashDrawerItems) {
        this.cashDrawerItems = cashDrawerItems;
    }
}
