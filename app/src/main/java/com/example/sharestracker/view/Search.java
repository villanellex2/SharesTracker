package com.example.sharestracker.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.APIConnector;
import com.example.sharestracker.connection.CurrencyStock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search extends Fragment {
    private RecyclerView mRecyclerView;
    private ShareFieldsAdapter mAdapter;
    private final List<ShareData> states = Collections.synchronizedList(new ArrayList<>());

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View curr = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        mAdapter = new ShareFieldsAdapter(getContext(), states);

        buildRecyclerView(curr);
        EditText search = curr.findViewById(R.id.editText);
        search.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                try {
                    new findSymbols().execute(search.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("input") &&
                !arguments.getString("input").equals("")){
            search.setText(arguments.getString("input"));
            new findSymbols().execute(search.getText().toString());
        }
        return curr;
    }

    class findSymbols extends AsyncTask<String, String, List<ShareData>> {

        @Override
        protected List<ShareData> doInBackground(String ... symbol) {
            states.clear();
            try {
                String searchResults = APIConnector.searchForSymbol(symbol[0]);
                JSONArray objects = new JSONObject(searchResults).getJSONArray("result");
                for (int i = 0; i < objects.length(); ++i) {
                    JSONObject obj = (JSONObject)objects.get(i);
                    String name = obj.getString("symbol");
                    new CompanyGetter().execute(name);
                }
            } catch (Exception e) {
                return null;
            }
            return states;
        }
    }

    private class CompanyGetter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String ... share) {
            try {
                String companyProfile = APIConnector.getCompanyProfile(share[0]);
                String price;
                ShareData data = new ShareData(share[0]);
                String url = (new JSONObject(companyProfile)).getString("logo");
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                BitmapDrawable logo = new BitmapDrawable(getResources(), bitmap);
                data.setCompanyInfo(getResources(), logo, companyProfile);
                try {
                    price = APIConnector.askTicket(share[0]);
                    String currency = data.getCurrencyCode();
                    data.setPrice(price, CurrencyStock.getCurrencyToUSD(currency));
                }
                catch (FileNotFoundException e){
                    data.setHasNo();
                }
                states.add(data);
                return companyProfile;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void buildRecyclerView(View view) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}