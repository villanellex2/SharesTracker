package com.example.sharestracker.connection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.sharestracker.File.FileHandler;
import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.adapters.ShareData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SharesInitializer {
    private List<String> sharesName;
    private final Resources res;
    private final List<ShareData> states;
    private final List<ShareData> preInitialized;
    private final ShareFieldsAdapter mAdapter;
    private final FileHandler handler;
    private final View animation;
    private TextView result;

    public SharesInitializer(Context context, List<String> sharesNames, List<ShareData> states, ShareFieldsAdapter mAdapter,
                             View animation) {
        this.sharesName = sharesNames;
        this.mAdapter = mAdapter;
        this.states = states;
        this.res = context.getResources();
        handler = new FileHandler(context);
        this.animation = animation;
        preInitialized = new ArrayList<>();
    }

    public SharesInitializer(Context context, JSONObject findResult, List<ShareData> states,
                             ShareFieldsAdapter mAdapter, View animation, TextView result) throws JSONException {
        fillTickers(findResult);
        this.mAdapter = mAdapter;
        this.states = states;
        this.res = context.getResources();
        handler = new FileHandler(context);
        this.animation = animation;
        preInitialized = new ArrayList<>();
        this.result = result;
    }


    public void fillSharesFiled() {
        animation.setVisibility(View.VISIBLE);
        for (String share : sharesName) {
            ShareData shareData = new ShareData(share);
            if (handler.isCached(share)) {
                String companyInfo = handler.getCompanyInfo(share);
                BitmapDrawable bitmap = handler.readDrawable(res, share);
                shareData.setCompanyInfo(res, bitmap, companyInfo);
                if (result == null) {
                    states.add(shareData);
                }
            }
            preInitialized.add(shareData);
            mAdapter.notifyDataSetChanged();
        }
        new CompanyGetter().execute();
    }

    private class CompanyGetter extends AsyncTask<Void, String, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int counter = 0;
            for (ShareData shareData: preInitialized) {
                try {
                    String shareName = shareData.getName();
                    String prices = null;
                    try {
                        prices = APIConnector.askTicket(shareName);
                    }catch (FileNotFoundException ignored){}
                    if (!shareData.isInitializedCompany()) {
                        String companyProfile = APIConnector.getCompanyProfile(shareName);
                        String url = (new JSONObject(companyProfile)).getString("logo");
                        InputStream in = new java.net.URL(url).openStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        BitmapDrawable logo = new BitmapDrawable(res, bitmap);
                        handler.storeImage(logo, shareName);
                        shareData.setCompanyInfo(res, logo, companyProfile);
                        handler.cacheShare(shareData.toString(), shareName);
                    }
                    if (prices != null) {
                        shareData.setPrice(prices, CurrencyStock.getCurrencyToUSD((shareData.getCurrencyCode())));
                    }
                    counter++;
                    if (!states.contains(shareData)) {
                        states.add(shareData);
                    }
                } catch (Exception ignored) {}
            }
            return counter;
        }

        @Override
        protected void onPostExecute(Integer s) {
            mAdapter.notifyDataSetChanged();
            animation.setVisibility(View.INVISIBLE);
            if (result != null){
                if (s == 0){
                    result.setText("no result");
                }
                else {
                    result.setText("results: " + s);
                }
            }
        }
    }

    private void fillTickers(JSONObject result) throws JSONException {
        JSONArray objects = result.getJSONArray("result");
        sharesName = new ArrayList<>();
        for (int i = 0; i < objects.length(); ++i) {
            JSONObject obj = (JSONObject)objects.get(i);
            String name = obj.getString("symbol");
            sharesName.add(name);
        }
    }
}
