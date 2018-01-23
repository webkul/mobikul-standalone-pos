package com.webkul.mobikul.mobikulstandalonepos.model;

import java.io.Serializable;

/**
 * Created by aman.gupta on 12/1/18. @Webkul Software Private limited
 */

public class ProductCategoryModel implements Serializable{

    private String cId;
    private String Name;

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
}
