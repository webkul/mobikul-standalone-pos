package com.webkul.mobikul.mobikulstandalonepos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.util.Patterns;

import com.webkul.mobikul.mobikulstandalonepos.BR;

@Entity(tableName = "Administrator")
public class Administrator extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @Ignore
    private String newPassword;
    @Ignore
    private boolean displayError;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Bindable
    public String getFirstName() {
        if (firstName == null)
            return "";
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable({"displayError", "firstName"})
    public String getFirstNameError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getFirstName().isEmpty()) {
            return "FIRSTNAME IS EMPTY";
        }
        return "";
    }

    @Bindable
    public String getLastName() {
        if (lastName == null)
            return "";
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable({"displayError", "lastName"})
    public String getLastNameError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getLastName().isEmpty()) {
            return "LASTNAME IS EMPTY";
        }
        return "";
    }

    @Bindable
    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable({"displayError", "email"})
    public String getEmailError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getEmail().isEmpty()) {
            return "EMAIL IS EMPTY";
        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
//            return "PLEASE ENTER A VALID EMAIL!";
//        }
        return "";
    }

    @Bindable
    public String getPassword() {
        if (password == null)
            return "";
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"displayError", "password"})
    public String getPasswordError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getPassword().isEmpty()) {
            return "PASSWORD IS EMPTY";
        }
        return "";
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
    public String getUsername() {
        if (username == null)
            return "";
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getNewPassword() {
        if (newPassword == null)
            return "";
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }
}