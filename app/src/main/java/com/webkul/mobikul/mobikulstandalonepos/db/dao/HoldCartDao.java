package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.HoldCart;

import java.util.List;

/**
 * Created by aman.gupta on 5/2/18. @Webkul Software Private limited
 */
@Dao
public interface HoldCartDao {
    @Query("SELECT * FROM HoldCart ORDER BY holdCartId DESC")
    List<HoldCart> getAll();

    @Query("SELECT * FROM HoldCart WHERE holdCartId IN (:holdCartIds)")
    List<HoldCart> loadAllByIds(int[] holdCartIds);

    @Query("SELECT * FROM HoldCart WHERE holdCartId LIKE (:searchText)")
    List<HoldCart> getSearchHoldCart(String searchText);

    @Insert
    long[] insertAll(HoldCart... holdCarts);

    //    @Delete
//    void delete(HoldCart HoldCart);
//
    @Query("DELETE FROM HoldCart")
    void delete();

    @Query("DELETE FROM HoldCart WHERE holdCartId IN (:holdCartId)")
    void delete(long holdCartId);


}
