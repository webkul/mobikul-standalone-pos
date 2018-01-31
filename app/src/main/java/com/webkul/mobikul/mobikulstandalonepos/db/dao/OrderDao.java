package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

import java.util.List;

/**
 * Created by aman.gupta on 25/1/18. @Webkul Software Private limited
 */
@Dao
public interface OrderDao {
    @Query("SELECT * FROM OrderEntity")
    List<OrderEntity> getAll();

    @Query("SELECT * FROM OrderEntity WHERE orderId IN (:OrderIds)")
    List<OrderEntity> loadAllByIds(int[] OrderIds);

    @Query("SELECT * FROM OrderEntity WHERE orderId LIKE (:searchText)")
    List<OrderEntity> getSearchOrders(String searchText);

    @Insert
    long[] insertAll(OrderEntity... orderEntities);

    @Delete
    void delete(OrderEntity OrderEntity);

}
