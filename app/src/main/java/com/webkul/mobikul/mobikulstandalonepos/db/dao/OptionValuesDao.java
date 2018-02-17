package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.OptionValues;

import java.util.List;

/**
 * Created by aman.gupta on 17/2/18. @Webkul Software Private limited
 */
@Dao
public interface OptionValuesDao {

    @Query("SELECT * FROM OptionValues")
    List<OptionValues> getAll();

    @Query("SELECT * FROM OptionValues WHERE option_id IN (:optionsIds)")
    List<OptionValues> loadAllByIds(int[] optionsIds);

//    @Query("UPDATE OptionValues SET category_name = :categoryName, is_active = :isActive, is_include_in_drawer_menu = :isIncludeInDrawerMenu WHERE cId = :cId")
//    void updateOptionValuesById(String categoryName, boolean isActive, boolean isIncludeInDrawerMenu, int cId);

    @Insert
    Long[] insertAll(OptionValues... OptionValuess);

    @Delete
    void delete(OptionValues OptionValues);

    @Query("DELETE FROM OptionValues")
    void delete();
}
