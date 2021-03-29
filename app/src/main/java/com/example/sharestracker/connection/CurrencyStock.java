package com.example.sharestracker.connection;

import org.json.JSONObject;

import java.util.HashMap;

public class CurrencyStock {
    private static HashMap<String, Double> currencyStock = new HashMap<>();

    public static double getCurrencyToUSD(String currency) throws Exception {
        if (currencyStock.containsKey(currency)){
            return currencyStock.get(currency);
        }

        JSONObject convertedCurrency = new JSONObject(APIConnector.convertCurrency("USD", currency));
        return Double.parseDouble(convertedCurrency.getString("USD_"+currency));
    }
}
