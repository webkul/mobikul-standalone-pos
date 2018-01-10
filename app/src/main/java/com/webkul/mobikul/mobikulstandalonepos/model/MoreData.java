package com.webkul.mobikul.mobikulstandalonepos.model;

/**
 * Created by aman.gupta on 4/1/18.
 */

public class MoreData {

    private String label;
    private int icon;
    private int id;

    public MoreData(String label, int icon, int id) {
        this.label = label;
        this.icon = icon;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
