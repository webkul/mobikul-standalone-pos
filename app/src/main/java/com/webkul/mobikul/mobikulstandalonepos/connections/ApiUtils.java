package com.webkul.mobikul.mobikulstandalonepos.connections;

import static com.webkul.mobikul.mobikulstandalonepos.constants.OpenExchangeRatesConstants.BASE_EXCHANGE_RATE_URL;

public class ApiUtils {

    private ApiUtils() {
    }

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_EXCHANGE_RATE_URL)
                .create(APIService.class);
    }
}