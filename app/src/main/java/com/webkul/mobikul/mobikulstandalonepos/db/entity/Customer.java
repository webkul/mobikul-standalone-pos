package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by aman.gupta on 23/1/18. @Webkul Software Private limited
 */

@Entity
public class Customer extends BaseObservable implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int customerId;

    @ColumnInfo(name = "customer_name")
    private String customerName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "contact")
    private String contactNumber;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        if (customerName == null)
            return "";
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        if (contactNumber == null)
            return "";
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}