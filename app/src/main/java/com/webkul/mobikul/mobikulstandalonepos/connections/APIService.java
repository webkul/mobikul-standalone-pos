package com.webkul.mobikul.mobikulstandalonepos.connections;

import com.webkul.mobikul.mobikulstandalonepos.model.OpenExchangeRate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("/latest.json")
    Call<OpenExchangeRate> getRates(@Query("app_id") String app_id,
                                    @Query("base") String base_currency);
}