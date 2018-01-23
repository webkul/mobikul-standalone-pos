package com.webkul.mobikul.mobikulstandalonepos.interfaces;

/**
 * Created by aman.gupta on 5/1/18.
 */

public interface DataBaseCallBack {
    void onSuccess(Object responseData, String successMsg);

    void onFailure(int errorCode, String errorMsg);
}
