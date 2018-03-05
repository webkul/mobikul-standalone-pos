package com.webkul.mobikul.mobikulstandalonepos.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.webkul.mobikul.mobikulstandalonepos.BR;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman.gupta on 17/1/18. @Webkul Software Private limited
 */
public class CartModel extends BaseObservable implements Serializable {
    private List<Product> products;
    private TotalModel totals;
    private Customer customer;

    public List<Product> getProducts() {
        if (products == null)
            products = new ArrayList<>();
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public TotalModel getTotals() {
        if (totals == null)
            totals = new TotalModel();
        return totals;
    }

    public void setTotals(TotalModel totals) {
        this.totals = totals;
    }

    @Bindable
    public Customer getCustomer() {
        if (customer == null)
            customer = new Customer();
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        notifyPropertyChanged(BR.customer);
    }
}
