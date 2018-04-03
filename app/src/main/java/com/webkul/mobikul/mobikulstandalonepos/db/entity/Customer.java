package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.util.Patterns;

import com.webkul.mobikul.mobikulstandalonepos.BR;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.db.DataBaseController;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;

import java.io.Serializable;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;
import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.getContext;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.SUCCESS_MSG_9_CUSTOMER_ALL_READY_EXIST;

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

    @ColumnInfo(name = "address_line")
    private String addressLine;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "postal_code")
    private String postalCode;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "country")
    private String country;

    @Ignore
    private boolean displayError;

    @Ignore
    private boolean isEmailExist;
    @Ignore
    private boolean isNumberExist;

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
        if (email == null) {
            return "";
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.d(TAG, "getEmail: " + getCustomerId());
            DataBaseController.getInstanse().checkEmailExist(getContext(), email, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    if (((Customer) responseData) != null && ((Customer) responseData).getCustomerId() != getCustomerId()) {
                        isEmailExist = true;
                    } else {
                        isEmailExist = false;
                    }
                    Log.d(TAG, "onSuccess: " + responseData);
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    isEmailExist = false;
                }
            });
        }
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
        if (isEmailExist)
            return SUCCESS_MSG_9_CUSTOMER_ALL_READY_EXIST;
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
        else if (!contactNumber.isEmpty()) {
            Log.d(TAG, "getContactNumber: " + contactNumber);
            DataBaseController.getInstanse().checkNumberExist(getContext(), contactNumber, new DataBaseCallBack() {
                @Override
                public void onSuccess(Object responseData, String successMsg) {
                    if (responseData != null && ((Customer) responseData).getCustomerId() != getCustomerId()) {
                        isNumberExist = true;
                    } else {
                        isNumberExist = false;
                    }
                }

                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    isNumberExist = false;
                }
            });
        }
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
        if (isNumberExist) {
            return SUCCESS_MSG_9_CUSTOMER_ALL_READY_EXIST;
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

    @Bindable
    public String getAddressLine() {
        if (addressLine == null)
            return "";
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    @Bindable
    public String getCity() {
        if (city == null)
            return "";
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Bindable
    public String getPostalCode() {
        if (postalCode == null)
            return "";
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Bindable
    public String getState() {
        if (state == null)
            return "";
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Bindable
    public String getCountry() {
        if (country == null)
            return "";
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}