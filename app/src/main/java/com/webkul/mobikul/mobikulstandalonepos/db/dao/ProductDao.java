package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.webkul.mobikul.mobikulstandalonepos.db.converters.DataConverter;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.util.List;

/**
 * Created by aman.gupta on 9/1/18.
 */
@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE pId IN (:ProductIds)")
    List<Product> loadProductsByIds(int ProductIds);

    @Query("SELECT * FROM Product WHERE product_name LIKE (:searchText)")
    List<Product> getSearchData(String searchText);

    @Query("SELECT * FROM Product WHERE is_enabled = :isEnabled")
    List<Product> getEnabledProduct(boolean isEnabled);

    @Query("SELECT * FROM Product WHERE sku IN (:sku)")
    Product checkSkuExist(String sku);

    @Query("SELECT * FROM Product WHERE CAST(quantity as decimal)<= :quantity")
    List<Product> getLowStockProducts(int quantity);

    @Query("SELECT * FROM Product WHERE barCode = :barcode")
    Product getProductByBarcode(String barcode);

    @Query("UPDATE Product SET quantity = :qty WHERE pId = :pId")
    void updateProductQty(String qty, int pId);

    @Query("UPDATE Product SET image = :path WHERE pId = :pId")
    void updateProductImages(String path, long pId);

    @Query("UPDATE Product SET image = :imagePath, is_enabled = :isEnabled, product_name = :ProductName, sku = :sku, price = :price" +
            ", special_price = :specialPrice, is_taxable_goods_applied = :isTaxableGoodsApplied, track_inventory= :trackInventory" +
            ", quantity = :qty , stock_availability = :inStock, weight = :weight, productCategories = :productCategories" +
            ", options = :productOptions, product_tax = :productTax WHERE pId = :pId")

    @TypeConverters(DataConverter.class)
    void updateProduct(String imagePath
            , boolean isEnabled
            , String ProductName
            , String sku
            , String price
            , String specialPrice
            , boolean isTaxableGoodsApplied
            , boolean trackInventory
            , String qty
            , boolean inStock
            , String weight
            , String productCategories
            , String productOptions
            , String productTax
            , int pId);


    @Insert
    long[] insertAll(Product... Products);

    @Delete
    void delete(Product Product);

    @Query("DELETE FROM Product")
    void delete();
}

