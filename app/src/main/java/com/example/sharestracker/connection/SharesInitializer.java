package com.example.sharestracker.connection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.sharestracker.JSON.FileHandler;
import com.example.sharestracker.adapters.FieldsAdapter;
import com.example.sharestracker.adapters.ShareData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class SharesInitializer {
    private final List<String> sharesName;
    private final Resources res;
    private final List<ShareData> states;
    FieldsAdapter mAdapter;
    FileHandler handler;

    public SharesInitializer(Context context, List<String> sharesNames, Resources res, List<ShareData> states, FieldsAdapter mAdapter) {
        this.sharesName = sharesNames;
        this.mAdapter = mAdapter;
        this.states = states;
        this.res = res;
        handler = new FileHandler(context);
    }

    public void fillSharesFiled() {
        for (String share : sharesName) {
            ShareData shareData = new ShareData(share);
            states.add(shareData);
            if (handler.isCached(share)) {
                String companyInfo = handler.getCompanyInfo(share);
                BitmapDrawable bitmap = handler.readDrawable(res, share);
                shareData.setCompanyInfo(res, bitmap, companyInfo);
            }
            mAdapter.notifyDataSetChanged();
        }

        for (ShareData shareData : states) {
            if (shareData.isInitializedCompany()) {
                continue;
            }
            CompanyGetter getter = new CompanyGetter();
            getter.execute(shareData);
        }
        for (ShareData shareData : states) {
            PriceGetter getter = new PriceGetter();
            getter.execute(shareData);
        }
    }

    private class PriceGetter extends AsyncTask<ShareData, String, String> {
        private String shareName;

        @Override
        protected String doInBackground(ShareData... data) {
            try {
                shareName = data[0].getName();
                String prices = APIConnector.askTicket(data[0].getName());
                double mod = 1;
                if (!data[0].getCurrencyCode().equals("USD")){
                    mod = new JSONObject(APIConnector.convertCurrency
                            ("USD", data[0].getCurrencyCode())).getDouble("USD_" + data[0].getCurrencyCode());
                }
                data[0].setPrice(prices, mod);
                return prices;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CompanyGetter extends AsyncTask<ShareData, String, String> {
        private String shareName;

        @Override
        protected String doInBackground(ShareData... data) {
            try {
                shareName = data[0].getName();
                String companyProfile = APIConnector.getCompanyProfile(data[0].getName());
                String url = (new JSONObject(companyProfile)).getString("logo");
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                BitmapDrawable logo = new BitmapDrawable(res, bitmap);
                handler.storeImage(logo, shareName);
                data[0].setCompanyInfo(res, logo, companyProfile);
                return companyProfile;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            handler.cacheShare(s, shareName);
            mAdapter.notifyDataSetChanged();
        }
    }
}
