package com.webkul.mobikul.mobikulstandalonepos.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.webkul.mobikul.mobikulstandalonepos.BR;

/**
 * Created by aman.gupta on 17/1/18. @Webkul Software Private limited
 */

public class TotalModel extends BaseObservable {
    private String subTotal;
    private String formatedSubTotal;
    private String qty;
    private String tax;
    private String formatedTax;
    private String discount;
    private String formatedDiscount;
    private String grandTotal;
    private String formatedGrandTotal;
    private String roundTotal;
    private String formatedRoundTotal;

    public TotalModel() {
    }

    @Bindable
    public String getSubTotal() {
        if (subTotal == null)
            return "0.00";
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
        notifyPropertyChanged(BR.subTotal);
    }

    @Bindable
    public String getTax() {
        if (tax == null)
            return "0.00";
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
        notifyPropertyChanged(BR.tax);
    }

    @Bindable
    public String getDiscount() {
        if (discount == null)
            return "0.00";
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
        notifyPropertyChanged(BR.discount);
    }

    @Bindable
    public String getGrandTotal() {
        if (grandTotal == null)
            return "0.00";
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
        notifyPropertyChanged(BR.grandTotal);
    }

    @Bindable
    public String getRoundTotal() {
        if (roundTotal == null)
            return "0.00";
        return roundTotal;
    }

    public void setRoundTotal(String roundTotal) {
        this.roundTotal = roundTotal;
        notifyPropertyChanged(BR.roundTotal);
    }

    @Bindable
    public String getQty() {
        if (qty == null)
            return "0";
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
        notifyPropertyChanged(BR.qty);
    }
    @Bindable
    public String getFormatedSubTotal() {
        if (formatedSubTotal == null) {
            return "0.00";
        }
        return formatedSubTotal;
    }

    public void setFormatedSubTotal(String formatedSubTotal) {
        this.formatedSubTotal = formatedSubTotal;
        notifyPropertyChanged(BR.formatedSubTotal);
    }

    public String getFormatedGrandTotal() {
        if (formatedGrandTotal == null) {
            return "0.00";
        }
        return formatedGrandTotal;
    }

    public void setFormatedGrandTotal(String formatedGrandTotal) {
        this.formatedGrandTotal = formatedGrandTotal;
    }

    public String getFormatedRoundTotal() {
        if (formatedRoundTotal == null) {
            return "0.00";
        }
        return formatedRoundTotal;
    }

    public void setFormatedRoundTotal(String formatedRoundTotal) {
        this.formatedRoundTotal = formatedRoundTotal;
    }

    public String getFormatedDiscount() {
        if (formatedDiscount == null) {
            return "0.00";
        }
        return formatedDiscount;
    }

    public void setFormatedDiscount(String formatedDiscount) {
        this.formatedDiscount = formatedDiscount;
    }

    public String getFormatedTax() {
        if (formatedTax == null) {
            return "0.00";
        }
        return formatedTax;
    }

    public void setFormatedTax(String formatedTax) {
        this.formatedTax = formatedTax;
    }
}