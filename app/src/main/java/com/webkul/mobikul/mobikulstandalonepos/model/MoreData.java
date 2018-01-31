package com.webkul.mobikul.mobikulstandalonepos.model;

/**
 * Created by aman.gupta on 4/1/18.
 */

public class MoreData {

    private String label;
    private int icon;
    private int id;
    private boolean enabled;

    public MoreData(String label, int icon, int id, boolean enabled) {
        this.label = label;
        this.icon = icon;
        this.id = id;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
