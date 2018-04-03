package com.webkul.mobikul.mobikulstandalonepos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;

import java.util.List;

/**
 * Created by aman.gupta on 23/1/18. @Webkul Software Private limited
 */
@Dao
public interface CustomerDao {
    @Query("SELECT * FROM Customer")
    List<Customer> getAll();

    @Query("SELECT * FROM Customer WHERE customerId IN (:CustomerIds)")
    List<Customer> loadAllByIds(int[] CustomerIds);

    @Query("SELECT * FROM Customer WHERE email IN (:email)")
    Customer checkEmailExist(String email);

    @Query("SELECT * FROM Customer WHERE contact_number IN (:contactNumber)")
    Customer checkNumberExist(String contactNumber);

    @Query("UPDATE Customer SET customer_first_name = :firstName, customer_last_name = :lastName, email = :email, contact_number = :contactNumber, address_line = :addressLine, city = :city, postal_code = :postalCode, state = :state, country = :country WHERE customerId = :customerId")
    void updateCustomerById(String firstName, String lastName, String email, String contactNumber, String addressLine, String city, String postalCode, String state, String country, int customerId);

    @Insert
    void insertAll(Customer... Customers);

    @Delete
    void delete(Customer Customer);

    @Query("DELETE FROM Customer")
    void delete();

}
