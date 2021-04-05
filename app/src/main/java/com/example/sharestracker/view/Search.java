package com.example.sharestracker.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.File.LastSearchedStorage;
import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.APIConnector;
import com.example.sharestracker.connection.SharesInitializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search extends Fragment {
    private RecyclerView mRecyclerView;
    private ShareFieldsAdapter mAdapter;
    private View animation;
    private TextView results;
    private final List<ShareData> states = Collections.synchronizedList(new ArrayList<>());

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View curr = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = curr.findViewById(R.id.lastSearchedRecyclerView);
        mAdapter = new ShareFieldsAdapter(getContext(), states);
        ((MainActivity) getContext()).mAdapter = mAdapter;
        animation = curr.findViewById(R.id.loading_animation);
        results = curr.findViewById(R.id.results);
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
                !arguments.getString("input").equals("")) {
            search.setText(arguments.getString("input"));
            new findSymbols().execute(search.getText().toString());
        }
        return curr;
    }

    class findSymbols extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... symbol) {
            LastSearchedStorage.addToLastSearched(getContext(), symbol[0]);
            states.clear();
            try {
                String searchResults = APIConnector.searchForSymbol(symbol[0]);
                JSONObject objects = new JSONObject(searchResults);
                return objects;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject obj) {
            try {
                SharesInitializer initializer = new SharesInitializer(getContext(), obj, states, mAdapter, animation, results);
                initializer.fillSharesFiled();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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