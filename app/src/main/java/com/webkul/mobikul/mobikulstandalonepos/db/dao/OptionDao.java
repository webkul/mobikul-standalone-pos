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

    @Query("SELECT * FROM Options ORDER BY option_id DESC")
    List<Options> getAll();

    @Query("SELECT * FROM Options WHERE option_id IN (:optionsIds)")
    List<Options> loadAllByIds(int[] optionsIds);

    @Query("UPDATE Options SET option_name = :optionName, option_type = :optionType, option_values = :optionValues WHERE option_id = :oId")
    void updateOptionsById(String optionName
            , String optionType
            , String optionValues
            , int oId);

    @Insert
    Long[] insertAll(Options... Optionss);

    @Delete
    void delete(Options Options);

    @Query("DELETE FROM Options")
    void delete();

}
