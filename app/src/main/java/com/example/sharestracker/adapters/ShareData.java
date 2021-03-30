package com.example.sharestracker.adapters;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class ShareData {
    private String currencyCode;
    private BitmapDrawable logo;
    private String name;
    private String companyName;
    private float currentPrice;
    private float dayDelta;
    private boolean isInitializedCompany = false;
    private boolean isInitializedPrice = false;

    public ShareData(String name, float currentPrice, float dayBeforePrice, String currencyCode,
                     String companyName, BitmapDrawable logoURL) {
        this.currentPrice = currentPrice;
        this.name = name;
        this.companyName = companyName;
        this.dayDelta = currentPrice - dayBeforePrice;
        this.currencyCode = currencyCode;
        this.logo = logoURL;
        isInitializedCompany = true;
    }

    public ShareData(String name, String currencyCode, String companyName, BitmapDrawable logoURL) {
        this.name = name;
        this.companyName = companyName;
        this.currencyCode = currencyCode;
        this.logo = logoURL;
        currentPrice = -1;
        dayDelta = -1;
    }

    public ShareData(String shareName) {
        name = shareName;
    }

    public void setCompanyInfo(Resources res, BitmapDrawable logo, String companyInfo) {
        JSONObject companyJSON = null;
        try {
            companyJSON = new JSONObject(companyInfo);
            companyName = companyJSON.getString("name");
            currencyCode = companyJSON.getString("currency");

            if (logo != null) {
                this.logo = logo;
                isInitializedCompany = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setPrice(String price, double mod) {
        JSONObject priceJSON = null;
        try {
            priceJSON = new JSONObject(price);
            currentPrice = (float) priceJSON.getDouble("c");
            dayDelta = currentPrice - (float) priceJSON.getDouble("pc");
            isInitializedPrice = true;
            currentPrice *= mod;
            dayDelta *= mod;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isInitializedCompany() {
        return isInitializedCompany;
    }

    public boolean isInitializedPrice() {
        return isInitializedPrice;
    }

    public String getDayDelta() {
        if (isInitializedPrice) {
            float inPerc = (int) (dayDelta / currentPrice * 1000) / 1000.f;
            StringBuilder res = new StringBuilder();
            if (dayDelta > 0) res.append("+");
            res.append((int) (dayDelta * 100) / 100.f);
            res.append('(');
            res.append(Math.abs(inPerc));
            res.append("%)");
            return res.toString();
        } else return "";
    }

    public BitmapDrawable getLogo() {
        return logo;
    }

    public String getCurrentPrice() {

        if (isInitializedPrice) {
            return currencyCode + " " + (int)(currentPrice*100)/100.f;
        } else return "";
    }

    public String getCompanyName() {
        if (isInitializedCompany) {
            return companyName;
        } else return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
