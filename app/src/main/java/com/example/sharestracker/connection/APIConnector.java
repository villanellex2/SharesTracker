package com.example.sharestracker.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class APIConnector {
    private static String token = "token=c1c8oaf48v6scqmqo1u0";
    private static String apiKey = "apiKey=bdc5313d1dc45e4ab6b2";

    private static String doGet(String url)
            throws Exception {

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        Log.d(TAG,"Response string: " + response.toString());

        return response.toString();
    }

    public static String askTicket(String shareName) throws Exception {
        return doGet("https://finnhub.io/api/v1/quote?symbol="+shareName+"&"+token);
    }
    
    public static String getCompanyProfile(String shareName) throws Exception{
        return doGet("https://finnhub.io/api/v1/stock/profile2?symbol="+shareName+"&"+token);
    }

    public static String convertCurrency(String from, String to) throws Exception {
        return doGet("https://free.currconv.com/api/v7/convert?q=" + from + "_"+ to + "&compact=ultra&" + apiKey);
    }

    public static String searchForSymbol(String symbol) throws Exception {
        return doGet("https://finnhub.io/api/v1/search?q="+symbol+"&"+token);
    }
}
