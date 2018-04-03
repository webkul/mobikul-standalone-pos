package com.webkul.mobikul.mobikulstandalonepos.model;

import android.arch.persistence.room.Ignore;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.webkul.mobikul.mobikulstandalonepos.BR;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by aman.gupta on 24/1/18. @Webkul Software Private limited
 */

public class CashModel extends BaseObservable implements Serializable {

    private String total;
    private String formatedTotal;
    private String collectedCash;
    private String formattedCollectedCash;
    private String note;
    private String formattedChangeDue;
    private String changeDue;
    private boolean changeDueVisibility;
    private boolean displayError;

    @Bindable
    public String getCollectedCash() {
        if (collectedCash == null)
            return "";
        return collectedCash;
    }

    @Bindable({"displayError", "collectedCash"})
    public String getCollectedCashError() {
        if (!isDisplayError()) {
            return "";
        }
        if (getCollectedCash().isEmpty()) {
            return "COLLECTED CASH CAN'T BE EMPTY!";
        }

        if (Double.parseDouble(getCollectedCash()) < Double.parseDouble(getTotal())) {
            return "COLLECTED CASH CAN'T LESS THEN TOTAL AMOUNT (" + getFormatedTotal() + ").    ";
        }
        return "";
    }

    public void setCollectedCash(String collectedCash) {
        this.collectedCash = collectedCash;
        if (!collectedCash.isEmpty()) {
            DecimalFormat df = new DecimalFormat("####0.00");
            Double change = Double.parseDouble(collectedCash) - Double.parseDouble(getTotal());
            if (change > 0) {
                setChangeDue(df.format(change) + "");
                setChangeDueVisibility(true);
            } else {
                setChangeDue(0.00 + "");
                setChangeDueVisibility(false);
            }
        }
        notifyPropertyChanged(BR.collectedCash);
    }

    @Bindable
    public String getNote() {
        if (note == null)
            return "";
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

    @Bindable
    public String getChangeDue() {
        if (changeDue == null)
            return "";
        return changeDue;
    }

    public void setChangeDue(String changeDue) {
        this.changeDue = changeDue;
        notifyPropertyChanged(BR.changeDue);
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Bindable
    public boolean isChangeDueVisibility() {
        return changeDueVisibility;
    }

    public void setChangeDueVisibility(boolean changeDueVisibility) {
        this.changeDueVisibility = changeDueVisibility;
        notifyPropertyChanged(BR.changeDueVisibility);
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public String getFormatedTotal() {
        return formatedTotal;
    }

    public void setFormatedTotal(String formatedTotal) {
        this.formatedTotal = formatedTotal;
    }

    @Bindable
    public String getFormattedCollectedCash() {
        if (formattedCollectedCash == null)
            return "";
        return formattedCollectedCash;
    }

    public void setFormattedCollectedCash(String formattedCollectedCash) {
        this.formattedCollectedCash = formattedCollectedCash;
        notifyPropertyChanged(BR.formattedCollectedCash);
    }

    @Bindable
    public String getFormattedChangeDue() {
        if (formattedChangeDue == null)
            return "";
        return formattedChangeDue;
    }

    public void setFormattedChangeDue(String formattedChangeDue) {
        this.formattedChangeDue = formattedChangeDue;
        notifyPropertyChanged(BR.formattedChangeDue);
    }
}
