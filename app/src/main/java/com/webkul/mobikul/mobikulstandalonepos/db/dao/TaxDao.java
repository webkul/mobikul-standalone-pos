package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Tax;

import java.util.List;

/**
 * Created by aman.gupta on 27/2/18. @Webkul Software Private limited
 */
@Dao
public interface TaxDao {
    @Query("SELECT * FROM Tax ORDER BY taxId DESC")
    List<Tax> getAll();

    @Query("SELECT * FROM Tax WHERE taxId IN (:TaxIds)")
    List<Tax> loadAllByIds(int[] TaxIds);

    @Query("SELECT * FROM Tax WHERE is_enabled = :isEnabled")
    List<Tax> getEnabledTax(boolean isEnabled);

    @Query("UPDATE Tax SET tax_name = :TaxName, is_enabled = :isActive, tax_rate = :taxRate WHERE taxId = :taxId")
    void updateTaxById(String TaxName, boolean isActive, String taxRate, int taxId);

    @Insert
    Long[] insertAll(Tax... Taxs);

    @Delete
    void delete(Tax Tax);

    @Query("DELETE FROM Tax")
    void delete();

}
