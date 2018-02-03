package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Patterns;

import com.webkul.mobikul.mobikulstandalonepos.BR;
import com.webkul.mobikul.mobikulstandalonepos.R;

import java.io.Serializable;

/**
 * Created by aman.gupta on 23/1/18. @Webkul Software Private limited
 */

@Entity
public class Customer extends BaseObservable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int customerId;

    @ColumnInfo(name = "customer_first_name")
    private String firstName;

    @ColumnInfo(name = "customer_last_name")
    private String lastName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "contact_number")
    private String contactNumber;

    @Ignore
    private boolean displayError;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Bindable
    public String getFirstName() {
        if (firstName == null)
            return "";
        return firstName;
    }

    @Bindable({"displayError", "firstName"})
    public String getFirstNameError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getFirstName().isEmpty()) {
            return "FIRST NAME IS EMPTY!";
        }
        return "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    @Bindable({"displayError", "email"})
    public String getEmailError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getEmail().isEmpty()) {
            return "EMAIL IS EMPTY!";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            return "PLEASE ENTER A VALID EMAIL!";
        }
        return "";
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getContactNumber() {
        if (contactNumber == null)
            return "";
        return contactNumber;
    }

    @Bindable({"displayError", "email"})
    public String getContactNumberError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getContactNumber().isEmpty()) {
            return "CONTACT NUMBER IS EMPTY!";
        }
        return "";
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        notifyPropertyChanged(BR.contactNumber);
    }

    @Bindable
    public String getLastName() {
        if (lastName == null)
            return "";
        return lastName;
    }

    @Bindable({"displayError", "lastName"})
    public String getLastNameError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getLastName().isEmpty()) {
            return "LAST NAME IS EMPTY!";
        }
        return "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

}