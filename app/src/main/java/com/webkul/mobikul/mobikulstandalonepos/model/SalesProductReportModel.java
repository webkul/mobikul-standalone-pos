package com.webkul.mobikul.mobikulstandalonepos.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;

import java.util.List;

/**
 * Created by aman.gupta on 16/3/18. @Webkul Software Private limited
 */

public class SalesProductReportModel {

    private int pId;

    private String productName;

    private String sku;

    private String price;

    private String formattedPrice;

    private List<Options> options;

    private String soldQty;

    private String time;

    private String date;

    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public String getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(String soldQty) {
        this.soldQty = soldQty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
