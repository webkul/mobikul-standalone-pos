package com.webkul.mobikul.mobikulstandalonepos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by aman.gupta on 12/1/18. @Webkul Software Private limited
 */

public class ProductCategoryModel implements Serializable, Parcelable {

    private String cId;
    private String Name;

    protected ProductCategoryModel(Parcel in) {
        cId = in.readString();
        Name = in.readString();
    }

    public ProductCategoryModel(){}

    public static final Creator<ProductCategoryModel> CREATOR = new Creator<ProductCategoryModel>() {
        @Override
        public ProductCategoryModel createFromParcel(Parcel in) {
            return new ProductCategoryModel(in);
        }

        @Override
        public ProductCategoryModel[] newArray(int size) {
            return new ProductCategoryModel[size];
        }
    };

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cId);
        dest.writeString(Name);
    }
}
