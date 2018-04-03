package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;

import java.util.List;

/**
 * Created by aman.gupta on 14/3/18. @Webkul Software Private limited
 */

public interface CurrencyDao {

    @Query("SELECT * FROM Currency")
    List<Currency> getAll();

    @Query("SELECT * FROM Currency WHERE code IN (:currencyCode)")
    List<Currency> loadAllByIds(int[] currencyCode);

    @Query("UPDATE Currency SET rate = :rate WHERE code = :currencyCode")
    void updateCurrencyById(String rate, int currencyCode);

    @Insert
    void insertAll(Currency... Currencys);

    @Delete
    void delete(Currency Currency);

    @Query("DELETE FROM Currency")
    void delete();

}
