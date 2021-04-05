package com.example.sharestracker.connection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

import com.example.sharestracker.File.FileHandler;
import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.view.MainActivity;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class SharesInitializer {
    private final List<String> sharesName;
    private final Resources res;
    private final Context context;
    private final List<ShareData> states;
    private ShareFieldsAdapter mAdapter;
    private FileHandler handler;
    private View animation;

    public SharesInitializer(Context context, List<String> sharesNames, List<ShareData> states, ShareFieldsAdapter mAdapter, View animation) {
        this.sharesName = sharesNames;
        this.mAdapter = mAdapter;
        this.states = states;
        this.res = context.getResources();
        handler = new FileHandler(context);
        this.context = context;
        this.animation = animation;
    }

    public void fillSharesFiled() {
        animation.setVisibility(View.VISIBLE);
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
        new CompanyGetter().execute();
    }

    private class CompanyGetter extends AsyncTask<Void, String, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int counter = 0;
            for (ShareData shareData: states) {
                try {
                    String shareName = shareData.getName();
                    String prices = APIConnector.askTicket(shareName);
                    shareData.setPrice(prices, CurrencyStock.getCurrencyToUSD((shareData.getCurrencyCode())));
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
                    counter++;
                } catch (Exception ignored) {}
            }
            return counter;
        }

        @Override
        protected void onPostExecute(Integer s) {
            mAdapter.notifyDataSetChanged();
            animation.setVisibility(View.INVISIBLE);
        }
    }
}
