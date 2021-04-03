package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sharestracker.adapters.FieldsAdapter;
import com.example.sharestracker.connection.FileHandler;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.SharesInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stock extends Fragment {

    private List<ShareData> list;
    final public String[] sharesNames = {"YNDX", "AAPL", "GOOGL", "AMZN", "BAC", "MSFT", "TSLA", "MA"};
    private FieldsAdapter mAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_stock, container, false);
        mAdapter = ((MainActivity)getContext()).mAdapter;
        list = ((MainActivity)getContext()).dataList;
        list.clear();
        buildRecyclerView(curr);
        return curr;
    }

    private void buildRecyclerView(View view) {
        SharesInitializer initializer = new SharesInitializer(getContext(), Arrays.asList(sharesNames),
                getResources(), list, mAdapter);
        initializer.fillSharesFiled();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.favoritesTextLittle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Stock.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

    }
}