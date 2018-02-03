package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Category;

import java.util.List;

/**
 * Created by aman.gupta on 8/1/18.
 */
@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Query("SELECT * FROM Category WHERE cId IN (:CategoryIds)")
    List<Category> loadAllByIds(int[] CategoryIds);

    @Query("SELECT * FROM Category WHERE is_include_in_drawer_menu = :isIncludeInDrawerMenu AND is_active = :isActive")
    List<Category> getCategoryIncludedInDrawerMenu(boolean isIncludeInDrawerMenu, boolean isActive);

    @Query("UPDATE Category SET category_name = :categoryName, is_active = :isActive, is_include_in_drawer_menu = :isIncludeInDrawerMenu WHERE cId = :cId")
    void updateCategoryById(String categoryName, boolean isActive, boolean isIncludeInDrawerMenu, int cId);

    @Insert
    void insertAll(Category... Categorys);

    @Delete
    void delete(Category Category);

    @Query("DELETE FROM Category")
    void delete();


}
