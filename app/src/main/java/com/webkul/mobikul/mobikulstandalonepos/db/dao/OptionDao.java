package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Options;

import java.util.List;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */
@Dao
public interface OptionDao {

    @Query("SELECT * FROM Options")
    List<Options> getAll();

    @Query("SELECT * FROM Options WHERE option_id IN (:optionsIds)")
    List<Options> loadAllByIds(int[] optionsIds);

//    @Query("UPDATE Options SET category_name = :categoryName, is_active = :isActive, is_include_in_drawer_menu = :isIncludeInDrawerMenu WHERE cId = :cId")
//    void updateOptionsById(String categoryName, boolean isActive, boolean isIncludeInDrawerMenu, int cId);

    @Insert
    Long[] insertAll(Options... Optionss);

    @Delete
    void delete(Options Options);

    @Query("DELETE FROM Options")
    void delete();

}
