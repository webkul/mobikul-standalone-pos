package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.CashModel;

import java.io.Serializable;

/**
 * Created by aman.gupta on 25/1/18. @Webkul Software Private limited
 */
@Entity(tableName = "OrderEntity")
public class OrderEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long orderId;

    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "date")
    private String date;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "cart_data")
    private CartModel cartData;

    @ColumnInfo(name = "qty")
    private String qty;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "cash_data")
    private CashModel cashData;

    @ColumnInfo(name = "is_synced")
    private String isSynced;

    @ColumnInfo(name = "is_return")
    private boolean isReturn;

    @ColumnInfo(name = "refunded_order_id")
    private String refundedOrderId;

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public CashModel getCashData() {
        return cashData;
    }

    public void setCashData(CashModel cashData) {
        this.cashData = cashData;
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        orderId = orderId + 10000;
        this.orderId = orderId;
    }

    public boolean getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    public String getRefundedOrderId() {
        if (refundedOrderId == null)
            return "";
        return refundedOrderId;
    }

    public void setRefundedOrderId(String refundedOrderId) {
        this.refundedOrderId = refundedOrderId;
    }
}
