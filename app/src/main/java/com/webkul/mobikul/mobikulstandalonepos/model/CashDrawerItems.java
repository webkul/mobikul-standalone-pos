package com.webkul.mobikul.mobikulstandalonepos.model;

import java.io.Serializable;

/**
 * Created by aman.gupta on 16/2/18. @Webkul Software Private limited
 */

public class CashDrawerItems implements Serializable {
    private String orderId;
    private String total;
    private String collectedCash;
    private String changeDue;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCollectedCash() {
        return collectedCash;
    }

    public void setCollectedCash(String collectedCash) {
        this.collectedCash = collectedCash;
    }

    public String getChangeDue() {
        return changeDue;
    }

    public void setChangeDue(String changeDue) {
        this.changeDue = changeDue;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
