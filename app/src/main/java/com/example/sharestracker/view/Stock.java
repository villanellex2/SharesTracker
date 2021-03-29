package com.example.sharestracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharestracker.JSON.FileHandler;
import com.example.sharestracker.adapters.FieldsAdapter;
import com.example.sharestracker.R;
import com.example.sharestracker.adapters.ShareData;
import com.example.sharestracker.connection.SharesInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stock extends Fragment {

    private final List<ShareData> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    final public String[] sharesNames = {"YNDX", "AAPL", "GOOGL", "AMZN", "BAC", "MSFT", "TSLA", "MA"};
    private FileHandler handler;
    private FieldsAdapter mAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View curr = inflater.inflate(R.layout.fragment_first, container, false);
        mRecyclerView = curr.findViewById(R.id.recyclerView);
        mAdapter = new FieldsAdapter(getContext(), list);
        ((MainActivity) getContext()).mAdapter = mAdapter;
        buildRecyclerView(curr);
        handler = new FileHandler(getContext());
        return curr;
    }

    private void buildRecyclerView(View view) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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