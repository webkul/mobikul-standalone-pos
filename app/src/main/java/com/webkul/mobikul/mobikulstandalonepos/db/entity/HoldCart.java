package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

/**
 * Created by aman.gupta on 5/2/18. @Webkul Software Private limited
 */
@Entity
public class HoldCart {
    @PrimaryKey(autoGenerate = true)
    private long holdCartId;

    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "date")
    private String date;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "cart_data")
    private CartModel cartData;

    @ColumnInfo(name = "qty")
    private String qty;

    @ColumnInfo(name = "is_synced")
    private String isSynced;

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public CartModel getCartData() {
        return cartData;
    }

    public void setCartData(CartModel cartData) {
        this.cartData = cartData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getHoldCartId() {
        return holdCartId;
    }

    public void setHoldCartId(long holdCartId) {
        holdCartId = holdCartId + 12000;
        this.holdCartId = holdCartId;
    }
}
