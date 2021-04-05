package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.adapters.ShareFieldsAdapter;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.SharesInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Stock extends Fragment {

    private final List<ShareData> list = Collections.synchronizedList(new ArrayList<>());
    private RecyclerView mRecyclerView;
    final public String[] sharesNames = {"YNDX", "AAPL", "GOOGL", "AMZN", "BAC", "MSFT", "TSLA", "MA"};
    private ShareFieldsAdapter mAdapter;
    private View animation;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_stock, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        animation = curr.findViewById(R.id.loading_animation);
        list.clear();
        mAdapter = new ShareFieldsAdapter(getContext(), list);
        ((MainActivity) getContext()).mAdapter = mAdapter;
        buildRecyclerView(curr);
        return curr;
    }

    private void buildRecyclerView(View view) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        SharesInitializer initializer = new SharesInitializer(getContext(), Arrays.asList(sharesNames), list, mAdapter, animation);
        initializer.fillSharesFiled();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.textFavourite).setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(Stock.this);
            nav.navigate(R.id.FavouriteFragment);
        });
        view.findViewById(R.id.editText).setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(Stock.this);
            nav.navigate(R.id.navigation2);
        });
        view.findViewById(R.id.editText).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                NavController nav = NavHostFragment.findNavController(Stock.this);
                nav.navigate(R.id.navigation2);
            }
        });
    }
}