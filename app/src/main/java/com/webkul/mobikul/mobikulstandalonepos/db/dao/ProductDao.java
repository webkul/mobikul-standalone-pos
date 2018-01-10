package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

import java.util.List;

/**
 * Created by aman.gupta on 9/1/18.
 */
@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE pId IN (:ProductIds)")
    List<Product> loadProductsByIds(int[] ProductIds);

    @Query("SELECT * FROM Product WHERE is_enabled = :isEnabled")
    List<Product> getEnabledProduct(boolean isEnabled);

//    @Query("UPDATE Product SET Product_name = :ProductName, is_active = :isActive, is_include_in_drawer_menu = :isIncludeInDrawerMenu WHERE cId = :cId")
//    void updateProductById(String ProductName, boolean isActive, boolean isIncludeInDrawerMenu, int cId);

    @Insert
    void insertAll(Product... Products);

    @Delete
    void delete(Product Product);

}

