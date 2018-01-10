package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConvertor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aman.gupta on 9/1/18.
 */

@Entity(tableName = "Product")
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int pId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_s_deccription")
    private String productShortDes;

    @ColumnInfo(name = "sku")
    private String sku;

    @ColumnInfo(name = "is_enabled")
    private boolean isEnabled;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "special_price")
    private double specialPrice;

    @ColumnInfo(name = "is_taxable_goods_applied")
    private boolean isTaxableGoodsApplied;

    @ColumnInfo(name = "track_inventory")
    private boolean trackInventory;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "stock_availability")
    private boolean stock;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "weight")
    private int weight;

    @TypeConverters(DataConvertor.class)
    @ColumnInfo(name = "categoryIds")
    private List<Integer> categoryIds;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortDes() {
        return productShortDes;
    }

    public void setProductShortDes(String productShortDes) {
        this.productShortDes = productShortDes;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public boolean isTaxableGoodsApplied() {
        return isTaxableGoodsApplied;
    }

    public void setTaxableGoodsApplied(boolean taxableGoodsApplied) {
        isTaxableGoodsApplied = taxableGoodsApplied;
    }

    public boolean isTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
