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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.adapters.FieldsAdapter;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.APIConnector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    private RecyclerView mRecyclerView;
    private FieldsAdapter mAdapter;
    private List<ShareData> states = new ArrayList<>();;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        mAdapter = new FieldsAdapter(getContext(), states);
        ((MainActivity) getContext()).mAdapter = mAdapter;
        buildRecyclerView(curr);
        EditText search = curr.findViewById(R.id.editText);
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    try {
                        new findSymbols().execute(search.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        return curr;
    }

    private class findSymbols extends AsyncTask<String, String, List<ShareData>> {
        private String shareName;

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
                String url = (new JSONObject(companyProfile)).getString("logo");
                InputStream in = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                BitmapDrawable logo = new BitmapDrawable(getResources(), bitmap);
                ShareData data = new ShareData(share[0]);
                data.setCompanyInfo(getResources(), logo, companyProfile);
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

        view.findViewById(R.id.favorite3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Search.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.stock3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Search.this)
                        .navigate(R.id.action_ThirdFragment_to_FirstFragment);
            }
        });
    }
}