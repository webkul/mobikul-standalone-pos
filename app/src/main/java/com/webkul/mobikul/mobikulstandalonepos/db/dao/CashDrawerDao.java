package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.CashDrawerModel;

import java.util.List;

/**
 * Created by aman.gupta on 15/2/18. @Webkul Software Private limited
 */
@Dao
public interface CashDrawerDao {
    @Query("SELECT * FROM CashDrawerModel ORDER BY date DESC")
    List<CashDrawerModel> getAll();

    @Query("SELECT * FROM CashDrawerModel WHERE date IN (:date)")
    CashDrawerModel loadAllByDate(String date);

    @Query("UPDATE CashDrawerModel SET closing_balance = :closingBalance, net_revenue = :netRevenue, in_amount = :inAmount, out_amount = :outAmount" +
            ", cash_drawer_items = :cashDrawerItemList, formatted_closing_balance = :formattedClosingBalance, formatted_net_revenue = :formattedNetRevenue" +
            ", formatted_in_amount = :formattedInAmount, formatted_out_amount = :formattedOutAmount WHERE date = :date")
    void updateCashDrawerModelByDate(String closingBalance, String netRevenue, String inAmount, String outAmount, String cashDrawerItemList, String formattedClosingBalance
            , String formattedNetRevenue, String formattedInAmount, String formattedOutAmount, String date);

    @Insert
    void insertAll(CashDrawerModel... CashDrawerModels);

    @Delete
    void delete(CashDrawerModel CashDrawerModel);

    @Query("DELETE FROM CashDrawerModel")
    void delete();
}
