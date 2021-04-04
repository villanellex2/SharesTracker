package com.example.sharestracker.connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class CurrencyStock {
    private static final HashMap<String, Double> currencyStock = new HashMap<>();
    static {
        currencyStock.put("USD_USD", 1.0);
    }

    public static double getCurrencyToUSD(String currency) throws IOException, JSONException {
        if (!currencyStock.containsKey(currency)) {
            JSONObject convertedCurrency = new JSONObject(APIConnector.convertCurrency("USD", currency));
            currencyStock.put(currency, Double.parseDouble(convertedCurrency.getString("USD_" + currency)));
        }
        return currencyStock.get(currency);
    }
}
